package com.example.suitlink;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PasswdFound extends AppCompatActivity {

    static final String TAG = "Passwd_Found";

    private ImageView pre;
    private Button found_bt;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private EditText edit_id, edit_name, edit_number;
    private String email, name, number;
    private Boolean check=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passward_found);
        pre = findViewById(R.id.imagePrevious);
        found_bt = findViewById(R.id.found);
        edit_id = findViewById(R.id.input_id);
        edit_name = findViewById(R.id.input_name);
        edit_number = findViewById(R.id.input_number);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //찾기 버튼 눌렀을 때
        found_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(edit_id.length()==0) {
                    Toast.makeText(PasswdFound.this,"이메일을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    email = edit_id.getText().toString();
                }
                if(edit_name.length()==0){
                    Toast.makeText(PasswdFound.this,"이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    name = edit_name.getText().toString();
                }
                if(edit_number.length()==0){
                    Toast.makeText(PasswdFound.this,"휴대폰 번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    number=edit_number.getText().toString();
                }
                ValueEventListener userListner = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Log.e(TAG, "onDataChange: "+snapshot);
                                User user = snapshot.getValue(User.class);
                                if(email.equals(user.email)&&name.equals(user.name)&&number.equals(user.number)){
                                    check=true;
                                    break;
                                }else{
                                    check=false;
                                }
                            }
                            if(check){
                                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(PasswdFound.this,"해당 이메일로 비밀번호 재설정 메일을 보냈습니다.",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(PasswdFound.this,"확인되지 않는 이메일입니다.",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    databaseReference.child("users").addListenerForSingleValueEvent(userListner);
                }
        });

    }
}
