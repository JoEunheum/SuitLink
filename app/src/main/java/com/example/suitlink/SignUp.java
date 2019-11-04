package com.example.suitlink;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class SignUp extends BaseActivity  implements DatePickerDialog.OnDateSetListener {
    public static final String BIRTH = "birth";
    private EditText ipname,  ippassword, ippasswordcheck, ipphonenumber, ipid;
    private Button ok,birth,checkid;
    private String name;
    private String id;
    private String pw;
    private String birthday;
    private String sex;
    private String number;
    private Date currentDate;
    private String DateString;
    private String TodayDateString;
    private SharedPreferences sign;
    private SharedPreferences.Editor editor;
    private RadioGroup man_or_girl;
    private ArrayList<User> userList;
    private String json;
    private Boolean idcheck=false;
    private ImageView previous;
    //이메일 비밀번호 로그인 모듈 변수
    private FirebaseAuth mAuth;
    //현재 로그인 된 유저 정보를 담을 변수
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private CheckTypesTask lodingtask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SignUp","onCreate_ok");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        userList = new ArrayList<>(); // 유저리스트를 배열리스트로 만듬

        ipname = (EditText)findViewById(R.id.input_name);
        ipid = (EditText)findViewById(R.id.input_id);
        ippassword = (EditText)findViewById(R.id.input_password);
        ippasswordcheck = (EditText)findViewById(R.id.input_password_check);
        ipphonenumber = (EditText)findViewById(R.id.input_phone);
        ok = (Button)findViewById(R.id.ok);
        birth = (Button)findViewById(R.id.input_birth);
        man_or_girl = (RadioGroup)findViewById(R.id.man_or_girl);
        previous = (ImageView) findViewById(R.id.imagePrevious);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

//        sign = getSharedPreferences("sign",MODE_PRIVATE);
//        editor = sign.edit();
    }

    // 로딩 어싱크테스크
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        private CustomProgressDialog asyncDialog;

        public CheckTypesTask(SignUp activity){
            asyncDialog = new CustomProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show dialog
            asyncDialog.getWindow().setBackgroundDrawable(null);
            asyncDialog.show();
        }

        @Override
        protected Void doInBackground(Void... result) {

            try {
                for (int i = 0; i < 4; i++) {
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }

    //현재 날짜
    protected void getDateToday(){
        currentDate = new Date();
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfMon = new SimpleDateFormat("MM");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        TodayDateString = sdfYear.format(currentDate)+"년 "+sdfMon.format(currentDate)+"월 "+sdfDay.format(currentDate)+"일 ";
        birth.setText(TodayDateString);
//        editor.putString("today",TodayDateString);
//        editor.commit();
//        birth.setText(sign.getString("today",""));
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDateToday();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //이전버튼 클릭했을 때
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    // 생년월일 버튼 클릭 시 이벤트
        birth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date Picker");
            }
        });

        //성별 선택
        man_or_girl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.man){
                    sex = "남자";
                }else if(checkedId == R.id.girl){
                    sex = "여자";
                }else if(checkedId != R.id.man || checkedId != R.id.girl){ // 이걸 어떻게 반영을 해줘야할지 찾아봐야할듯
                    Toast.makeText(SignUp.this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    man_or_girl.requestFocus();
                    return;
                }
            }
        });

    // 가입 버튼 클릭 시 이벤트
        ok.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //이름 입력 X
                if (ipname.getText().toString().length() == 0) {
                    Toast.makeText(SignUp.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    ipname.requestFocus();
                    return;
                } else if (!Pattern.matches("^[가-힣]*$", ipname.getText().toString())) { //이름 영어나 숫자로 입력했을 때
                    Toast.makeText(SignUp.this, "한글만 입력해주세요.", Toast.LENGTH_SHORT).show();
                    ipname.requestFocus();
                    return;
                } else {
                    name = ipname.getText().toString(); // name이라는 String에 저장
                }
                //아이디 확인
                if (ipid.getText().toString().length() == 0) {
                    Toast.makeText(SignUp.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                    ipid.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(ipid.getText().toString()).matches()) { //아이디 한글 및 특수문자 입력했을 경우
                    Toast.makeText(SignUp.this, "이메일 형식으로 입력해주세요.", Toast.LENGTH_SHORT).show();
                    ipid.requestFocus();
                    return;
                } else {
                    id = ipid.getText().toString();
                }
                //비밀번호 확인
                if(ippassword.getText().toString().length()==0){
                    Toast.makeText(SignUp.this,"비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    ippassword.requestFocus();
                    return;
                }else if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$",ippassword.getText().toString())){ //비밀번호 형식 이상할 때
                    Toast.makeText(SignUp.this,"비밀번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show();
                    ippassword.requestFocus();
                    return;
                }
                if(ippasswordcheck.getText().toString().length()==0){
                    Toast.makeText(SignUp.this,"비밀번호 확인을 입력하세요.", Toast.LENGTH_SHORT).show();
                    ippasswordcheck.requestFocus();
                    return;
                }
                if(!ippassword.getText().toString().equals(ippasswordcheck.getText().toString())){
                    Toast.makeText(SignUp.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    ippassword.setText("");
                    ippasswordcheck.setText("");
                    ippassword.requestFocus();
                    return;
                }else {
                    pw = ippassword.getText().toString(); //일치 한다면 비밀번호 값 저장
                }
                //생년월일 확인
                if(birth.getText().toString().equals(TodayDateString)){
                    Toast.makeText(SignUp.this,"생년월일을 다시 선택해주세요.", Toast.LENGTH_SHORT).show();
                    birth.requestFocus();
                    return;
                }else{
                    birthday = birth.getText().toString(); // 오늘 날짜가 아니면 다른 날짜 저장 가능 다른 날짜는 어떻게 처리해줘야할지..
                }
                //번호 확인
                if(ipphonenumber.getText().toString().length()==0){
                    Toast.makeText(SignUp.this,"휴대폰 번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    ipphonenumber.requestFocus();
                    return;
                }else if(!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$",ipphonenumber.getText().toString())) { //휴대폰번호 형식
                    Toast.makeText(SignUp.this, "올바른 핸드폰 번호가 아닙니다.", Toast.LENGTH_SHORT).show();
                    ipphonenumber.requestFocus();
                    return;
                }else{
                    number = ipphonenumber.getText().toString(); // 제대로 저장이 되었다면
                }

                lodingtask = new CheckTypesTask(SignUp.this);
                lodingtask.execute();

                //drawable 에 저장된 이미지 uri로 저장시키기
                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + getResources().getResourcePackageName(R.drawable.photo)
                        + '/' + getResources().getResourceTypeName(R.drawable.photo) + '/' + getResources().getResourceEntryName(R.drawable.photo) );

                final String imageString = imageUri.toString();

                //파이어베이스 authentication 테스트
                mAuth.createUserWithEmailAndPassword(id,pw).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        lodingtask.cancel(true);
                        if(task.isSuccessful()){
                            currentUser = mAuth.getCurrentUser();
                            String uid=currentUser.getUid();
                            Toast.makeText(SignUp.this,"회원가입 되었습니다.",Toast.LENGTH_SHORT).show();
                            User user = new User(name , birthday, sex, number, imageString,currentUser.getEmail());
                            databaseReference.child("users").child(uid).setValue(user);
                            User size = new User(" ", " ", " ", " ");//사이즈
                            databaseReference.child("users").child(uid).child("size").setValue(size);
                            finish();
                        }else{
                            Toast.makeText(SignUp.this,"이메일 중복입니다.",Toast.LENGTH_SHORT).show();
                            ipid.requestFocus();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
       // Calendar c = Calendar.getInstance();
        //c.set(Calendar.YEAR,year);
       // c.set(Calendar.MONTH,month);
        //c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        //String currentDateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        //birth.setText(currentDateString);
        DateString = year+"년 "+(month+1)+"월 "+dayOfMonth+"일 ";
        birth.setText(DateString);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){ // 저장
        //outState.putString(TODAY,TodayDateString);
        outState.putString(BIRTH,DateString);
        birth.setText(DateString);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) { // 복구
        //TodayDateString = savedInstanceState.getString(TODAY);
        DateString = savedInstanceState.getString(BIRTH);
        birth.setText(DateString);
        super.onRestoreInstanceState(savedInstanceState);
    }
}
