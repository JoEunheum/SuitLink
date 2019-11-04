package com.example.suitlink;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class Personal_Suit_Status extends BaseActivity {
    private static final String TAG= "Personal_suit_status";
    private ImageView previous,addsuitimage;
    private String key, post_uid, post_name;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference suitReference;
    private DatabaseReference userReference;
    private String division="",rental_time, uid, address, du, si, dong, oup;
    private TextView textDivision, textColor, textPrice, textAddress, textNumber, textRental_time, textRental_how,textSize,textLent;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_suit_status);

        key= getIntent().getStringExtra("post_key");
        post_uid = getIntent().getStringExtra("post_uid");
        post_name = getIntent().getStringExtra("post_name");

        //xml 연결
        previous = findViewById(R.id.imagePrevious);
        addsuitimage = findViewById(R.id.suits_image);
        textDivision = findViewById(R.id.textDivision);
        textColor = findViewById(R.id.textcolor);
        textPrice = findViewById(R.id.textPrice);
        textAddress = findViewById(R.id.textAddress);
        textNumber = findViewById(R.id.textNumber);
        textRental_time = findViewById(R.id.textRental_time);
        textRental_how = findViewById(R.id.textRental_how);
        textSize = findViewById(R.id.textSize);
        textLent = findViewById(R.id.textRentHow);

        //뷰페이저 세팅팅
        pager = findViewById(R.id.pager);

        //기본 이미지 사라지게 함
        addsuitimage.setVisibility(View.INVISIBLE);

        //파이어베이스
        firebaseDatabase = FirebaseDatabase.getInstance();
        suitReference = firebaseDatabase.getReference().child("posts").child(key);
        userReference = firebaseDatabase.getReference();

        SharedPreferences userinfo = getSharedPreferences("userinfo",MODE_PRIVATE);
        uid = userinfo.getString("uid","");

        Log.e(TAG, "onCreate: postuid : "+post_uid );
        Log.e(TAG, "onCreate: uid : "+uid );

        ValueEventListener postsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: "+dataSnapshot);
                Post post = dataSnapshot.getValue(Post.class);

                //뷰페이저에 사진 세팅
                SharedPreferences SuitUri = getSharedPreferences("SuitUri",MODE_PRIVATE);
                SharedPreferences.Editor editor = SuitUri.edit();
                Gson gson = new Gson();
                String json = gson.toJson(post.suitimage);
                editor.putString("imageList",json);
                editor.commit();
                CustomAdapter adapter = new CustomAdapter(getLayoutInflater(),Personal_Suit_Status.this,true);
                pager.setAdapter(adapter);
                CircleIndicator indicator = findViewById(R.id.indicator);
                indicator.setViewPager(pager);

                //구분 보여주기
                String[] array = post.division.split("_");
                for(int i = 0;i<array.length;i++){
                    division+=array[i]+" ";
                }

                array = post.address.split(" ");
                du = array[0];
                si = array[1];
                dong = array[2];
                oup = array[3];
                Log.e(TAG, "onDataChange: du : "+du+si+dong+oup);

                address = du+" "+si+" "+dong+" "+oup;
                //텍스트 초기화
                textDivision.setText(division.trim());
                textColor.setText(post.color);
                textSize.setText(post.size);
                textPrice.setText(post.price+" 원");
                textAddress.setText(address);
                rental_time = post.rental_time+" ~ "+post.return_time;
                textRental_time.setText(rental_time);
                textNumber.setText(post.number);
                textRental_how.setText(post.rental_how);

                if(uid.equals(post.uid)){
                    textLent.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        suitReference.addListenerForSingleValueEvent(postsListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //이전 버튼 눌렀을 때
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //대여 버튼 눌렀을 때
        textLent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Personal_Suit_Status.this,textRental_how.getText()+"로 대여되었습니다.",Toast.LENGTH_SHORT).show();

                //사진 저장시켜야함
                SharedPreferences suituri = getSharedPreferences("SuitUri", Context.MODE_PRIVATE);
                String json = suituri.getString("imageList","");
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                ArrayList<String> suitUris = gson.fromJson(json,type);

                String size = textSize.getText().toString();
                String colo = textColor.getText().toString();
                String price = textPrice.getText().toString();
                String number = textNumber.getText().toString();
                rental_time = textRental_time.getText().toString();
                String rental_how = textRental_how.getText().toString();
                division = textDivision.getText().toString();
                address = textAddress.getText().toString();

                Post post = new Post(size , colo, price, address, number,rental_time, rental_how, uid, division, post_name, suitUris,"rent");
                userReference.child("rents").child(uid).child(key).setValue(post);
                suitReference.child("rental_check").setValue("rent");
                suitReference.child("uid").setValue(uid);
                userReference.child("user-posts").child(post_uid).child(key).child("rental_check").setValue("rent");
                userReference.child("user-posts").child(post_uid).child(key).child("uid").setValue(uid);
                Intent intent = new Intent(getApplicationContext(), MainChoice.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences SuitUri = getSharedPreferences("SuitUri",MODE_PRIVATE);
        SharedPreferences.Editor editor = SuitUri.edit();
        editor.clear();
        editor.apply();
        super.onDestroy();
    }

}
