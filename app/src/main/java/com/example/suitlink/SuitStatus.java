package com.example.suitlink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SuitStatus extends AppCompatActivity {

    private Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.befor_rental_status);
        next = (Button)findViewById(R.id.next);
    }
    @Override
    protected void onResume() {
        super.onResume();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SuitDate.class);
                startActivity(intent);
            }
        });
    }
}
