package com.example.suitlink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Male_or_Female extends BaseActivity {

    private ImageView imageMale;
    private ImageView imageFemale;
    private ImageView previous;
    private String office_name,office_address,office_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.male_or_female);

        office_name = getIntent().getStringExtra("office_name");
        office_address=getIntent().getStringExtra("office_address");
        office_number= getIntent().getStringExtra("office_number");

        SharedPreferences name = getSharedPreferences("office_name",MODE_PRIVATE);
        SharedPreferences.Editor editor = name.edit();
        editor.putString("office_name", office_name);
        editor.putString("office_address",office_address);
        editor.putString("office_number",office_number);
        editor.apply();

        imageMale = findViewById(R.id.imageMale);
        imageFemale = findViewById(R.id.imageFemale);

        previous = findViewById(R.id.imagePrevious);
    }

    @Override
    protected void onResume() {
        super.onResume();

        imageMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Male_or_Female.this, OpenMaleSuit.class);
                startActivity(intent);
            }
        });

        imageFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Male_or_Female.this, OpenFeMaleSuit.class);
                startActivity(intent);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
