package com.example.suitlink;

import android.content.Intent;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainChoice extends BaseActivity {

    private static final String TAG= "MainChoice";
    private BackPressCloseHandler backPressCloseHandler;
    private TextView firstcome;
    private Button mypage,rentalstore,personalLental,tipbt;
    private ImageView ganggo1, ganggo2, ganggo3;
    private int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate_OK");
        setContentView(R.layout.main_choice);

        backPressCloseHandler = new BackPressCloseHandler(this);
        personalLental = findViewById(R.id.individual);
        firstcome = findViewById(R.id.firstcome);
        mypage = findViewById(R.id.mypage_bt);
        rentalstore = findViewById(R.id.rentalstore);

        ganggo1 = findViewById(R.id.ganggo1);
        ganggo2 = findViewById(R.id.ganggo2);
        ganggo3 = findViewById(R.id.ganggo3);

        tipbt = findViewById(R.id.tipbt);

        startAdvertising();

    }

    private  void startAdvertising(){
        MyRunnable myRunnable = new MyRunnable();
        Thread myThred = new Thread(myRunnable);
        myThred.setDaemon(true);
        myThred.start();
    }

    android.os.Handler mainHandler = new android.os.Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
                if (msg.what == 0) {
                    ganggo1.setVisibility(View.VISIBLE);
                    ganggo2.setVisibility(View.INVISIBLE);
                    ganggo3.setVisibility(View.INVISIBLE);
                } else if (msg.what == 1) {
                    ganggo1.setVisibility(View.INVISIBLE);
                    ganggo2.setVisibility(View.VISIBLE);
                    ganggo3.setVisibility(View.INVISIBLE);
                } else if (msg.what == 2) {
                    ganggo1.setVisibility(View.INVISIBLE);
                    ganggo2.setVisibility(View.INVISIBLE);
                    ganggo3.setVisibility(View.VISIBLE);
                    i = 0;
                }
            }
    };

    public class MyRunnable implements Runnable{

        @Override
        public void run() {
            while(true){
                Message msg = Message.obtain();
                msg.what = i;
                mainHandler.sendMessage(msg);
                i++;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart_OK");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume_OK");

        //개인대여 버튼 클릭
        personalLental.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Personal_Lental.class);
                startActivity(intent);
            }
        });

        //매장찾기 버튼 클릭
        rentalstore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);//OpenRental.class 매장 옷들 진열
                startActivity(intent);
            }
        });

        //처음오셨나요 버튼 클릭
        firstcome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Guide.class);
                startActivity(intent);
            }
        });

        //마이페이지 버튼 클릭
        mypage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MyPage.class);
                startActivity(intent);
            }
        });

        tipbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainTip.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause_OK");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop_OK");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart_OK");

        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy_OK");
        super.onDestroy();
    }

}
