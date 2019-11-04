package com.example.suitlink;

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

import me.relex.circleindicator.CircleIndicator;

public class Rent_Suit_Status extends BaseActivity {
    private static final String TAG= "Personal_suit_status";
    private ImageView previous,addsuitimage;
    private String key, post_uid;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference suitReference;
    private DatabaseReference userReference;
    private String uid;
    private TextView textDivision, textColor, textPrice, textAddress, textNumber, textRental_time, textRental_how,textSize, textReturn, textStatus;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_suit_status);

        key= getIntent().getStringExtra("post_key");
        post_uid = getIntent().getStringExtra("post_uid");

        Log.e(TAG, "onCreate: post_uid"+post_uid);
        Log.e(TAG, "onCreate: post_key"+key);

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
        textReturn = findViewById(R.id.textRentHow);
        textStatus = findViewById(R.id.profile_edit);

        textReturn.setText("반납");
        textStatus.setText("정장확인");



        if(!(post_uid.equals("employment_wing")||post_uid.equals("open_closet")||post_uid.equals("Jjin_suit"))){
            textReturn.setVisibility(View.INVISIBLE);
        }

        //뷰페이저 세팅
        pager = findViewById(R.id.pager);

        //기본 이미지 사라지게 함
        addsuitimage.setVisibility(View.INVISIBLE);

        SharedPreferences userinfo = getSharedPreferences("userinfo",MODE_PRIVATE);
        uid = userinfo.getString("uid","");

        //파이어베이스
        firebaseDatabase = FirebaseDatabase.getInstance();
        suitReference = firebaseDatabase.getReference().child("rents").child(uid).child(key);
        userReference = firebaseDatabase.getReference();

        ValueEventListener postsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e(TAG, "onDataChange: "+dataSnapshot);
                    Post post = dataSnapshot.getValue(Post.class);
                    if(post.rental_check.equals("return")){
                        textReturn.setVisibility(View.INVISIBLE);
                    }
                    textNumber.setText(post.number);
                    textDivision.setText(post.division);
                    textColor.setText(post.color);
                    textSize.setText(post.size);
                    textAddress.setText(post.address);
                    textRental_time.setText(post.rental_time);
                    textRental_how.setText(post.rental_how);
                    textPrice.setText(post.price);

                    SharedPreferences SuitUri = getSharedPreferences("SuitUri",MODE_PRIVATE);
                    SharedPreferences.Editor editor = SuitUri.edit();
                    Gson gson = new Gson();

                    String json = gson.toJson(post.suitimage);
                    editor.putString("imageList",json);
                    editor.commit();
                    Log.e(TAG, "onDataChange: Json : "+json);
                    if(json.equals("[]")){
                        SharedPreferences office_suit = getSharedPreferences("office_suit",MODE_PRIVATE);
                        SharedPreferences.Editor editorr = office_suit.edit();
                        editorr.putInt("office_suit",post.office_suit);
                        editorr.commit();
                        RentAdapter adapter = new RentAdapter(getLayoutInflater(),Rent_Suit_Status.this,true);
                        pager.setAdapter(adapter);
                        CircleIndicator indicator = findViewById(R.id.indicator);
                        indicator.setViewPager(pager);
                    }else {
                        RentImageAdapter adapter = new RentImageAdapter(getLayoutInflater(), Rent_Suit_Status.this, true);
                        pager.setAdapter(adapter);
                        CircleIndicator indicator = findViewById(R.id.indicator);
                        indicator.setViewPager(pager);
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
        textReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Rent_Suit_Status.this,"반납 되었습니다.",Toast.LENGTH_SHORT).show();
                if(post_uid.equals("employment_wing")||post_uid.equals("open_closet")||post_uid.equals("Jjin_suit")) {
                    suitReference.child("rental_check").setValue("return");
                }
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
        editor.commit();

        SharedPreferences office_suit = getSharedPreferences("office_suit",MODE_PRIVATE);
        SharedPreferences.Editor editorr = office_suit.edit();
        editorr.clear();
        editorr.commit();

        super.onDestroy();
    }
}
