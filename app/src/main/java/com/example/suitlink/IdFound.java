package com.example.suitlink;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class IdFound extends AppCompatActivity {
    private ImageView previous;
    private EditText inputNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_found);

        inputNumber = findViewById(R.id.input_phone);
        previous = findViewById(R.id.imagePrevious);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
        };
    }

    @Override
    protected void onStart(){
        super.onStart();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                PhoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
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

    private boolean validatePhoneNumber() {
        String phoneNumber = inputNumber.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            inputNumber.setError("Invalid phone number.");
            return false;
        }

        return true;
    }
}
