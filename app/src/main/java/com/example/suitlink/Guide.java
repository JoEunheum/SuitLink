package com.example.suitlink;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Guide extends Activity {

    protected Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.app_guide);

        exit = (Button) findViewById(R.id.exit);

        ImageView iv = (ImageView)findViewById(R.id.img1);
        iv.setImageResource(R.drawable.suit);
        iv = (ImageView)findViewById(R.id.img2);
        iv.setImageResource(R.drawable.suit);
        iv = (ImageView)findViewById(R.id.img3);
        iv.setImageResource(R.drawable.suit);
        iv = (ImageView)findViewById(R.id.img4);
        iv.setImageResource(R.drawable.suit);

    }

    @Override
    protected void onResume() {
        super.onResume();
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
