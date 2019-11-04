package com.example.suitlink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Personal_Lental extends BaseActivity {
    private static String TAG = "Personal_Lental";
    private RecyclerView mRecyclerView; // 리사이클러뷰 변수 지정해준듯
    private LinearLayoutManager mLayoutManager; // 리니어로 표현해주기 위해 LinearLayoutManager 를 이용
    private RecyclerViewAdapter mAdapter;   // 만들어둔 어뎁터 클래스 전역변수로 만들기
    private ArrayList<Personal_Item> mArrayList; // 아이템 리스트를 mArrayList 로 객체 생성
    private FloatingActionButton addView; // 플로팅 액션 버튼을 클릭하면 카드뷰를 추가시켜준다.
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference suitReference;
    private String userprofile;
    private Uri dermy;
    private String check;
    private String name;
    private int location;
    private ImageView previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_lental);

        previous = findViewById(R.id.imagePrevious);

        mRecyclerView = findViewById(R.id.recyclerView); // 리사클러뷰 만든 것을 여기다가 지정
        mLayoutManager = new LinearLayoutManager(this); // 이 클래스는 리니어레이아웃형태로 만들겠다라는 것 같음.
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // 수직으로 만들겠다!
        mRecyclerView.setHasFixedSize(true); // 각 아이템이 보여지는 것을 일정하게
        mRecyclerView.setLayoutManager(mLayoutManager); // 앞서 선언한 리싸이클러뷰를 레이아웃매니저에 붙인다.

        // Personal_Lental에서 RecyclerView의 데이터에 접근 시 사용된다.
        mArrayList = new ArrayList<>();

        // RecyclerView를 위해 customAdapter를 사용함.
        mAdapter = new RecyclerViewAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        addView = findViewById(R.id.addbt);   // 플로팅 액션버튼

        firebaseDatabase = FirebaseDatabase.getInstance();
        suitReference = firebaseDatabase.getReference().child("posts");

        ChildEventListener postListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e(TAG, "onChildAdded: "+dataSnapshot );
                Post post;
                try{
                    post = dataSnapshot.getValue(Post.class);
                }catch(DatabaseException e){
                    Log.e("에러확인", "onDataChange: "+e);
                    return;
                }
                String key = dataSnapshot.getKey();
                name = post.name;
                userprofile = post.profileUri;
                check = post.rental_check;
                String postuid = post.uid;
                dermy = Uri.parse(post.suitimage.get(0));

                if(check.equals("true")||check.equals("edit")){
                    mArrayList.add(new Personal_Item(userprofile,name,R.drawable.ic_more_horiz_black_24dp,dermy,key,postuid,name)); // 리스트에 추가하여 보여준다.
                    mAdapter.notifyDataSetChanged();
                    mAdapter.notifyItemInserted(0);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e(TAG, "onChildChanged: ok");
                Post post;
                try{
                    post = dataSnapshot.getValue(Post.class);
                }catch(DatabaseException e){
                    Log.e("에러확인", "onDataChange: "+e);
                    return;
                }
                String key = dataSnapshot.getKey();
                String post_uid = post.uid;
                name = post.name;
                userprofile=post.profileUri;
                check = post.rental_check;
                dermy = Uri.parse(post.suitimage.get(0));

                if(check.equals("true")){ // 추가 했을 때
                    mArrayList.add(new Personal_Item(userprofile,name,R.drawable.ic_more_horiz_black_24dp,dermy, key,post_uid,name)); // 리스트에 추가하여 보여준다.
                    mAdapter.notifyDataSetChanged();
                    mAdapter.notifyItemInserted(0);
                }else if(check.equals("edit")){ // 수정 했을 때!
                    SharedPreferences position = getSharedPreferences("position",MODE_PRIVATE);
                    location=position.getInt("position",2000);
                    if(location!=2000){
                        mArrayList.set(location,new Personal_Item(userprofile,name,R.drawable.ic_more_horiz_black_24dp,dermy,key,post_uid,name)); // 리스트에 추가하여 보여준다.
                        Log.e(TAG, "onChildChanged: "+location);
                        mAdapter.notifyItemChanged(location);
                    }
                }else if(check.equals("rent")){
                    Log.e(TAG, "onChildChanged: rent 반응 확인 테스트");
                    key = dataSnapshot.getKey();
                    for(int i=0;i<mArrayList.size();i++){
                        if(mArrayList.get(i).getPost_key().equals(key)){
                            mArrayList.remove(i);
                            break;
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "onChildRemoved: "+dataSnapshot);
                String key = dataSnapshot.getKey();

                for(int i=0;i<mArrayList.size();i++){
                if(mArrayList.get(i).getPost_key().equals(key)){
                    mArrayList.remove(i);
                    break;
                }
            }
                mAdapter.notifyDataSetChanged();
        }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e(TAG, "onChildMoved: Ok" );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: Ok" );
            }
        };
        suitReference.addChildEventListener(postListener);
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();

        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Personal_Add.class);
                startActivityForResult(intent, 100);
            }
        });

        //이전 버튼 눌렀을 때
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences position = getSharedPreferences("position", MODE_PRIVATE);
        SharedPreferences.Editor editor = position.edit();
        editor.clear();
        editor.apply();
        super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mAdapter.notifyDataSetChanged();
    }
}
