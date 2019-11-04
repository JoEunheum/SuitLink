package com.example.suitlink;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SelfCheck extends BaseActivity {
    private static final String TAG = "SelfCheck";
    private ImageView previous;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SelfcyclerViewAdapter mAdapter;
    private ArrayList<Rent_Check_Item> mSelfCheckList;

    private TextView text_list;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String price, color, size, rental_time,division,rental_check, name, suitimage;
    private String uid;
    private String post_key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_check);

        previous = findViewById(R.id.imagePrevious);

        mRecyclerView = findViewById(R.id.selfcyclerView);
        mLayoutManager = new LinearLayoutManager(this); // 이 클래스는 리니어레이아웃형태로 만들겠다라는 것 같음.
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // 수직으로 만들겠다!
        mRecyclerView.setHasFixedSize(true); // 각 아이템이 보여지는 것을 일정하게
        mRecyclerView.setLayoutManager(mLayoutManager); // 앞서 선언한 리싸이클러뷰를 레이아웃매니저에 붙인다.

        mSelfCheckList = new ArrayList<>();

        mAdapter = new SelfcyclerViewAdapter(this, mSelfCheckList);
        mRecyclerView.setAdapter(mAdapter);


        SharedPreferences userinfo = getSharedPreferences("userinfo",MODE_PRIVATE);
        uid = userinfo.getString("uid","");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("user-posts").child(uid);

        text_list = findViewById(R.id.text_list);

        ChildEventListener selfListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e(TAG, "onChildAdded: "+dataSnapshot );
                try{
                    text_list.setVisibility(View.INVISIBLE);
                    if(dataSnapshot.getValue().equals(null)){
                    }
                }catch (NullPointerException e){
                    text_list.setVisibility(View.VISIBLE);
                }

                post_key = dataSnapshot.getKey();
                Post post = dataSnapshot.getValue(Post.class);
                rental_time = post.rental_time+" ~ "+post.return_time;
                name = post.name;
                try{
                    suitimage = post.suitimage.get(0);
                }catch (IndexOutOfBoundsException e){
                    Log.e(TAG, "onChildAdded: "+e);
                }
                division = post.division;
                color = post.color;
                size = post.size;
                price = post.price;
                if(post.rental_check.equals("rent")){
                    rental_check = "대여중";
                }else if(post.rental_check.equals("return")){
                    rental_check = "반납완료";
                }else if(post.rental_check.equals("edit")){
                    rental_check="";
                }else if(post.rental_check.equals("true")){
                    rental_check="";
                }
                //아이템 세팅
                mSelfCheckList.add(new Rent_Check_Item(rental_time, name, suitimage, division, color, size, price, rental_check, post_key, post.uid));
                mAdapter.notifyItemInserted(0);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e(TAG, "onChildChanged: "+dataSnapshot );
                post_key = dataSnapshot.getKey();
                Post post = dataSnapshot.getValue(Post.class);
                rental_time = post.rental_time+" ~ "+post.return_time;
                name = post.name;
                suitimage = post.suitimage.get(0);
                division = post.division;
                color = post.color;
                size = post.size;
                price = post.price;
                if(post.rental_check.equals("rent")){
                    rental_check = "대여중";
                }else if(post.rental_check.equals("return")){
                    rental_check = "반납완료";
                }else if(post.rental_check.equals("edit")){
                    rental_check="";
                }else if(post.rental_check.equals("true")){
                    rental_check="";
                }
                //편집 했을 때
                SharedPreferences position = getSharedPreferences("position",MODE_PRIVATE);
                int location=position.getInt("position",2000);
                if(location!=2000) {
                    mSelfCheckList.set(location, new Rent_Check_Item(rental_time, name, suitimage, division, color, size, price, rental_check, post_key, post.uid));
                    mAdapter.notifyDataSetChanged();
                }
                // 반납완료 눌렀을 때
                SharedPreferences loca = getSharedPreferences("location",MODE_PRIVATE);
                int posi = loca.getInt("location",100);
                if(post.rental_check.equals("return")){
                    try{
                        mSelfCheckList.set(posi, new Rent_Check_Item(rental_time, name, suitimage, division, color, size, price, rental_check, post_key, post.uid));
                    }catch (IndexOutOfBoundsException e){
                        Log.e(TAG, "onChildChanged: "+e );
                        Log.e(TAG, "onChildChanged: 사진 바꿨는데 너가 왜 반응하는거야?" );
                    }
                    mAdapter.notifyDataSetChanged();
                }
                //다른 사람이 상품을 렌탈을 했을 때
//                if(post.rental_check.equals("rent")){
//                    post_key = dataSnapshot.getKey();
//                    for(int i = 0;i<mSelfCheckList.size();i++){
//                        try{
//                            mSelfCheckList.set(i,new Rent_Check_Item(rental_time, name, suitimage, division, color, size, price, rental_check, post_key, post.uid));
//
//                        }catch (IndexOutOfBoundsException e) {
//                            Log.e(TAG, "onChildChanged: " + e);
//                            Log.e(TAG, "onChildChanged: 사진 바꿨는데 너가 왜 반응하는거야?");
//                        }
//                        mAdapter.notifyDataSetChanged();
//                        break;
//                    }
//                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                post_key = dataSnapshot.getKey();
                for(int i=0;i<mSelfCheckList.size();i++){
                    if(mSelfCheckList.get(i).getPost_key().equals(post_key)){
                        mSelfCheckList.remove(i);
                        break;
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.orderByChild("rental_check").addChildEventListener(selfListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
