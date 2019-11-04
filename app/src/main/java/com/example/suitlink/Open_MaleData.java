package com.example.suitlink;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Open_MaleData extends BaseActivity implements DatePickerDialog.OnDateSetListener{
    private final String TAG = "Open_MaleData";

    private Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    private String user_uid;

    private Boolean datecheck=false;
    private int suitimage;
    private ImageView imageSuit;
    private CheckBox jacket, chest, pents, shoes, tie, belt;
    private TextView textprice;
    private int stackprice=0;

    private ImageView previous;
    private TextView rent;
    private Button rental_time_bt, return_time_bt;
    private Date currentDate;
    private String TodayDateString;
    private EditText editsize;
    private RadioButton radioHome, radioDelivery;

    private String color;
    private String size;
    private String price;
    private String rental_time;
    private String rental_how;
    private ArrayList<String> division;
    private String office_name;
    private String office_address;
    private String office_number;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference openRefrerence;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_male_data);

        suitimage = getIntent().getIntExtra("image",100);
        imageSuit = findViewById(R.id.suits_image);
        Glide.with(this).load(suitimage).into(imageSuit);

        previous = findViewById(R.id.imagePrevious);

        jacket = findViewById(R.id.radiojacket);
        chest = findViewById(R.id.radiochest);
        pents = findViewById(R.id.radiopents);
        shoes = findViewById(R.id.radioshoes);
        tie = findViewById(R.id.radiotie);
        belt = findViewById(R.id.radiobelt);

        editsize =findViewById(R.id.textSize);
        editsize.setText("");

        textprice = findViewById(R.id.textprice);
        textprice.setText(stackprice+" 원");

        arrayList = new ArrayList<>();
        arrayList.add("블랙");
        arrayList.add("네이비");

        arrayAdapter = new ArrayAdapter<>(Open_MaleData.this,android.R.layout.simple_spinner_item,arrayList);
        spinner = findViewById(R.id.spinner);
        spinner.setGravity(Gravity.RIGHT);
        spinner.setAdapter(arrayAdapter);

        rent = findViewById(R.id.textRentHow);

        rental_time_bt = findViewById(R.id.textRental_time);
        return_time_bt = findViewById(R.id.textReturn_time);
        radioHome = findViewById(R.id.radiohome);
        radioDelivery = findViewById(R.id.radiodelivery);

        firebaseDatabase = FirebaseDatabase.getInstance();
        openRefrerence = firebaseDatabase.getReference();

        SharedPreferences userinfo = getSharedPreferences("userinfo",MODE_PRIVATE);
        user_uid = userinfo.getString("uid","");

        SharedPreferences office = getSharedPreferences("office_name",MODE_PRIVATE);
        office_name = office.getString("office_name", "");
        office_address = office.getString("office_address","");
        office_number = office.getString("office_number","");

        division = new ArrayList<>();

        getDateToday();
    }

    //현재 날짜
    protected void getDateToday(){
        currentDate = new Date();
        SimpleDateFormat sdfYear = new SimpleDateFormat("yy");
        SimpleDateFormat sdfMon = new SimpleDateFormat("M");
        SimpleDateFormat sdfDay = new SimpleDateFormat("d");
        TodayDateString = sdfYear.format(currentDate)+". "+sdfMon.format(currentDate)+". "+sdfDay.format(currentDate)+".";
        rental_time_bt.setText(TodayDateString);
        long daydate = currentDate.getTime();
        long adddate = daydate+(24*(60*(60*1000)))*3; // daydate+(24*(60*(60*1000))) 현재 날짜에 1일 추가, x3 했으니 3일 추가
        currentDate.setTime(adddate);
        TodayDateString = sdfYear.format(currentDate)+". "+sdfMon.format(currentDate)+". "+sdfDay.format(currentDate)+".";
        return_time_bt.setText(TodayDateString);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //구분 체크되었을 때 동작할 메소드 구현
        jacket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(stackprice==0) {
                    if (isChecked == true) {
                        stackprice += 10000;
                    }else
                        stackprice = 0;
                }else if(stackprice!=0){
                    if(isChecked==true){
                        stackprice += 10000;
                    }else
                        stackprice -= 10000;
                }
                Log.e(TAG, "onResume: "+stackprice );
                textprice.setText(stackprice+" 원");
            }
        });

        chest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(stackprice==0) {
                    if (isChecked == true) {
                        stackprice += 5000;
                    }else
                        stackprice = 0;
                }else if(stackprice!=0){
                    if(isChecked==true){
                        stackprice += 5000;
                    }else
                        stackprice -= 5000;
                }
                Log.e(TAG, "onResume: "+stackprice );
                textprice.setText(stackprice+" 원");
            }
        });

        pents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(stackprice==0) {
                    if (isChecked == true) {
                        stackprice += 10000;
                    }else
                        stackprice = 0;
                }else if(stackprice!=0){
                    if(isChecked==true){
                        stackprice += 10000;
                    }else
                        stackprice -= 10000;
                }
                Log.e(TAG, "onResume: "+stackprice );
                textprice.setText(stackprice+" 원");
            }
        });

        shoes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(stackprice==0) {
                    if (isChecked == true) {
                        stackprice += 5000;
                    }else
                        stackprice = 0;
                }else if(stackprice!=0){
                    if(isChecked==true){
                        stackprice += 5000;
                    }else
                        stackprice -= 5000;
                }
                Log.e(TAG, "onResume: "+stackprice );
                textprice.setText(stackprice+" 원");
            }
        });

        tie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(stackprice==0) {
                    if (isChecked == true) {
                        stackprice += 2000;
                    }else
                        stackprice = 0;
                }else if(stackprice!=0){
                    if(isChecked==true){
                        stackprice += 2000;
                    }else
                        stackprice -= 2000;
                }
                Log.e(TAG, "onResume: "+stackprice );
                textprice.setText(stackprice+" 원");
            }
        });

        belt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(stackprice==0) {
                    if (isChecked == true) {
                        stackprice += 2000;
                    }else
                        stackprice = 0;
                }else if(stackprice!=0){
                    if(isChecked==true){
                        stackprice += 2000;
                    }else
                        stackprice -= 2000;
                }
                Log.e(TAG, "onResume: "+stackprice );
                textprice.setText(stackprice+" 원");
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0) {
                    color = "블랙";
                }else
                    color = "네이비";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //이전버튼 클릭했을 때
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //대여버튼 클릭했을 때
        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: Ok");
                //날짜에 대해서도 조건 처리 해줘하는데..일단 메인기능부터

                if(textprice.getText().toString().equals("0 원")){
                    Toast.makeText(Open_MaleData.this,"대여 할 상품을 선택해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    price= textprice.getText().toString();
                }
                if(editsize.getText().toString().equals("")){
                    Toast.makeText(Open_MaleData.this,"사이즈를 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    size=editsize.getText().toString();
                }
                if(jacket.isChecked()==true){
                    division.add("자켓");
                }if(chest.isChecked()==true){
                    division.add("상의");
                }if(pents.isChecked()==true){
                    division.add("하의");
                }if(shoes.isChecked()==true){
                    division.add("구두");
                }if(tie.isChecked()==true){
                    division.add("타이");
                }if(belt.isChecked()==true){
                    division.add("벨트");
                }
                rental_time=rental_time_bt.getText().toString()+" ~ "+return_time_bt.getText().toString();
                if (radioHome.isChecked() == false && radioDelivery.isChecked() == false) {
                    Toast.makeText(Open_MaleData.this, "대여방법을 정해주세요.", Toast.LENGTH_SHORT).show();
                    radioHome.requestFocus();
                    return;
                }else if(radioHome.isChecked()==true){
                    rental_how = radioHome.getText().toString();
                }else{
                    rental_how = radioDelivery.getText().toString();
                }
                Post office = new Post(size , color, price, office_address, office_number, rental_time, rental_how, office_name, division, office_name , suitimage, "rent");
                String office_key=openRefrerence.child("rents").child(user_uid).push().getKey(); // 주문번호처럼 키를 생성시키자!
                openRefrerence.child("rents").child(user_uid).child(office_key).setValue(office);
                Log.e(TAG, "onClick:"+office_key );
                Toast.makeText(Open_MaleData.this,rental_how+"로 대여되었습니다.",Toast.LENGTH_SHORT).show();


                SharedPreferences office_name = getSharedPreferences("office_name",MODE_PRIVATE);
                SharedPreferences.Editor editor = office_name.edit();
                editor.clear();
                editor.commit();

                //잠시 주석
                Intent intent = new Intent(getApplicationContext(), MainChoice.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        //날짜 보여주기
        rental_time_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DialogFragment datePicker = new DatePickerFragment();
                datecheck=false;
                datePicker.show(getSupportFragmentManager(),"date Picker");
            }
        });

        return_time_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment datePricker = new DatePickerFragment();
                datecheck=true;
                datePricker.show(getSupportFragmentManager(),"date Picker");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(cal.getTime());
        if(datecheck==false) {
            rental_time_bt.setText(currentDateString);
        }else{
            return_time_bt.setText(currentDateString);
        }
    }
}
