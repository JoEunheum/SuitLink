package com.example.suitlink;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import java.util.List;


public class MyPage extends BaseActivity {
    private static final String TAG= "MyPage";
    private ImageView setting;
    private TextView id;
    private String textid,imagephoto;
    private ImageView profile_image;
    private  String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final int MULTIPLE_PERMISSIONS=101; // 권한 동의 여부 문의 후 CallBack 메소드에 쓰일 변수수
    private Uri imageUri;
    private ConstraintLayout rent,self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);

        checkPermissions();//권한 묻기

        Log.d(TAG, "onCreate_OK");
        setting = findViewById(R.id.imageView_Setting);
        id = findViewById(R.id.id);
        profile_image = findViewById(R.id.imageView_photo);

        SharedPreferences userinfo = getSharedPreferences("userinfo",MODE_PRIVATE);
        textid = userinfo.getString("name","");
        imagephoto = userinfo.getString("imageUri","");
        Log.d("text id",textid);
        Log.d("imageUri",imagephoto);
        id.setText(textid);
        imageUri=Uri.parse(imagephoto);
        Glide.with(this).load(imageUri).apply(RequestOptions.circleCropTransform()).into(profile_image);
        Log.d(TAG, "onStart_OK");

        rent = findViewById(R.id.rent_check);
        self = findViewById(R.id.self_check);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume_OK");

        //세팅 버튼 눌렀을 때
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPage.this, MyPage_Setting.class);
                startActivityForResult(intent,100);
            }
        });

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: rent : Ok" );
                Intent intent = new Intent(MyPage.this, RentCheck.class);
                startActivity(intent);
            }
        });

        self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: self : Ok");
                Intent intent = new Intent(MyPage.this, SelfCheck.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == 100) {
            SharedPreferences userinfo = getSharedPreferences("userinfo", MODE_PRIVATE);
            imagephoto = userinfo.getString("imageUri", "");
            imageUri = Uri.parse(imagephoto);
            Glide.with(this).load(imageUri).apply(RequestOptions.circleCropTransform()).into(profile_image);
        }
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

    //권한 물어서 참 거짓으로 판별
    private boolean checkPermissions(){
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) { //사용자가 해당 권한을 가지고 있지 않을 경우 리스트에 해당 권한명 추가
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) { //권한이 추가되었으면 해당 리스트가 empty가 아니므로 request 즉 권한을 요청합니다.
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    //PERMISSION_GRANTED로 권한을 획득했는지 확인할 수 있습니다.
    //권한 사용에 동의를 안했을 경우
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    //권한 획득에 동의를 하지 않았을 경우 아래 Toast 메세지를 띄우며 해당 Activity를 종료시킵니다.
    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
