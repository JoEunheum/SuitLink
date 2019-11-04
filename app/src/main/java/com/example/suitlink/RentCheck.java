package com.example.suitlink;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RentCheck extends BaseActivity {
    private static final String TAG = "RentCheck";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RentcyclerViewAdapter mAdapter;
    private ArrayList<Rent_Check_Item> mRentCheckList;

    private ImageView previous;
    private TextView text_list;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String uid;
    private String post_key; // 작성자
    private String price, color, size, rental_time,division,rental_check, name, suitimage;
    private int office_suit;
    private Boolean office=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rent_check);

        mRecyclerView = findViewById(R.id.rentcyclerView);
        mLayoutManager = new LinearLayoutManager(this); // 이 클래스는 리니어레이아웃형태로 만들겠다라는 것 같음.
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // 수직으로 만들겠다!
        mRecyclerView.setHasFixedSize(true); // 각 아이템이 보여지는 것을 일정하게
        mRecyclerView.setLayoutManager(mLayoutManager); // 앞서 선언한 리싸이클러뷰를 레이아웃매니저에 붙인다.

        mRentCheckList = new ArrayList<>();

        mAdapter = new RentcyclerViewAdapter(this, mRentCheckList);
        mRecyclerView.setAdapter(mAdapter);

        SharedPreferences userinfo = getSharedPreferences("userinfo",MODE_PRIVATE);
        uid = userinfo.getString("uid","");

        previous = findViewById(R.id.imagePrevious);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("rents").child(uid);

        text_list = findViewById(R.id.text_list);

        ValueEventListener rentListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try{
                    text_list.setVisibility(View.INVISIBLE);
                    if(dataSnapshot.getValue().equals(null)){
                    }
                }catch (NullPointerException e){
                    text_list.setVisibility(View.VISIBLE);
                }
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.e(TAG, "onDataChange: "+snapshot );
                    post_key = snapshot.getKey();
                    Post post = snapshot.getValue(Post.class);
                    rental_time = post.rental_time;

                    if(post.name.equals("employment_wing")){
                        name="취업날개";
                    }else if(post.name.equals("open_closet")){
                        name="열린옷장";
                    }else if(post.name.equals("Jjin_suit")){
                        name="제이진슈트";
                    }else {
                        name = post.name;
                    }
                    try{
                        suitimage = post.suitimage.get(0);
                    }catch (IndexOutOfBoundsException e){
                        Log.e(TAG, "onDataChange: "+e);
                        office_suit = post.office_suit; // 배열이 아니면 저장
                        office = true;
                    }

                    division = post.division;
                    color = post.color;
                    size = post.size;
                    price = post.price;
                    if(post.rental_check.equals("rent")){
                        rental_check = "대여중";
                    }else if(post.rental_check.equals("return")){
                        rental_check = "반납완료";
                    }
                    //아이템 세팅
                    if(!office) {
                        Log.e(TAG, "onDataChange: 업체사진 아니야" );
                        mRentCheckList.add(new Rent_Check_Item(rental_time, name, suitimage, division, color, size, price, rental_check, post_key, post.uid));
                        mAdapter.notifyItemInserted(0);
                        mAdapter.notifyDataSetChanged();
                    }else{
                        Log.e(TAG, "onDataChange: 업체사진 맞아" );
                        mRentCheckList.add(new Rent_Check_Item(rental_time, name, office_suit, division, color, size, price, rental_check, post_key, post.uid));
                        mAdapter.notifyItemInserted(0);
                        mAdapter.notifyDataSetChanged();
                    }
                    office = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.orderByChild("rental_check").addListenerForSingleValueEvent(rentListener);
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
