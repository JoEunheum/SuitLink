package com.example.suitlink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends BaseActivity {
    private BackPressCloseHandler backPressCloseHandler;
    private EditText input_id,input_password;
    private String login_id, login_password;
    private Button login, idfound, passfound;
    private TextView signup;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private CheckTypesTask lodingtask;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference sizedataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainAcitvity","onCreate_Ok");

        //돌아가기 버튼 눌렀을 경우
        backPressCloseHandler = new BackPressCloseHandler(this);
        setContentView(R.layout.login);

        //setting
        input_id = findViewById(R.id.input_id);
        input_password = findViewById(R.id.input_password);
        login = findViewById(R.id.button_login);
        idfound = findViewById(R.id.id_found);
        passfound = findViewById(R.id.password_found);
        signup = findViewById(R.id.sign_up_come);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        //파이어베이스 자동로그인
        if(firebaseAuth.getCurrentUser()!=null){
                Intent intent = new Intent(MainActivity.this,MainChoice.class);
                startActivity(intent);
                finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainAcitvity","onStart_Ok");
    }

    @Override
    protected void onStop() {
        Log.d("MainAcitvity","onStop_Ok");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("MainAcitvity","onDestroy_Ok");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d("MainAcitvity","onPause_Ok");
        super.onPause();
    }

    // 로딩 어싱크테스크
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        private CustomProgressDialog asyncDialog;

        private CheckTypesTask(MainActivity activity){
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


    public void loginStart(String email,String password) {

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            lodingtask = new CheckTypesTask(MainActivity.this);
            lodingtask.execute();
            Log.d("else 확인","들어왔다.");
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            Toast.makeText(MainActivity.this, "존재하지 않는 이메일 입니다.", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(MainActivity.this, "이메일과 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseNetworkException e) {
                            Toast.makeText(MainActivity.this, "Firebase NetworkException", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        currentUser = firebaseAuth.getCurrentUser();
                        databaseReference = firebaseDatabase.getReference().child("users").child(currentUser.getUid()); // 유저 정보
                        sizedataReference = firebaseDatabase.getReference().child("users").child(currentUser.getUid()).child("size"); // 사이즈
                        SharedPreferences userinfo = getSharedPreferences("userinfo",MODE_PRIVATE);
                        final SharedPreferences.Editor editor = userinfo.edit();

                        ValueEventListener userListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.e("유저 데이터 스냅샷", "onDataChange: "+dataSnapshot);
                                User user = dataSnapshot.getValue(User.class);
                                Log.e("유저 데이터", "onDataChange: "+user.name);
                                editor.putString("name",user.name);
                                editor.putString("imageUri",user.imageUri);
                                editor.putString("email",currentUser.getEmail());
                                editor.putString("uid",currentUser.getUid());
                                editor.putString("passwd",input_password.getText().toString());
                                editor.apply();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        ValueEventListener sizeListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                editor.putString("chest", user.chest);
                                editor.putString("pents",user.pents);
                                editor.putString("shoes", user.shoes);
                                editor.putString("status", user.status);
                                editor.apply();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        };
                        databaseReference.addListenerForSingleValueEvent(userListener);
                        sizedataReference.addListenerForSingleValueEvent(sizeListener);
                        theEnd();
                    }
                }
            });

        }
    }

    void theEnd(){
        lodingtask.cancel(true);
        Intent intent = new Intent(MainActivity.this, MainChoice.class);
        startActivity(intent);
        finish();
    }

    @Override
        protected void onResume() {
            super.onResume();
            Log.d("MainAcitvity", "onResume_Ok");

            //로그인 버튼 눌렀을 때
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login_id = input_id.getText().toString().trim();
                    login_password = input_password.getText().toString().trim();
                    loginStart(login_id, login_password);
                }
            });

        //아이디 찾기
        idfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IdFound.class);
                startActivity(intent);
            }
        });

        //비밀번호 찾기
        passfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PasswdFound.class);
                startActivity(intent);
            }
        });

        //회원가입
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });
    }

    //돌아가기 버튼 눌렀을 시, onBackPressed 메소드 호출
    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }
}
