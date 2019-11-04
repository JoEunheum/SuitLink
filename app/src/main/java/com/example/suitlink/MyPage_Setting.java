package com.example.suitlink;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


public class MyPage_Setting extends BaseActivity {

    private static final String TAG= "MyPage_Setting";
    private static final String TEMP_FILE_NAME = "tempFile.jpg";
    final int REQ_CODE_SELECT_IMAGE=200;
    final int REQUEST_IMAGE_CROP = 300;
    private TextView logout,save,textid,leave,password_change;
    private ImageView gallery,previous;
    private EditText edit_status, edit_chest, edit_pents, edit_shoes;
    private  String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private String id,imagephoto;
    private static final int MULTIPLE_PERMISSIONS=101; // 권한 동의 여부 문의 후 CallBack 메소드에 쓰일 변수수
    private Uri imageUri;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String uid;
    private String status, chest,pents,shoes;
    private Uri downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page_setting);

        checkPermissions();//권한 묻기
        Log.d(TAG, "onCreate_OK");
        gallery = findViewById(R.id.photo);
        logout = findViewById(R.id.logout);
        previous = findViewById(R.id.imagePrevious);
        save = findViewById(R.id.textSave);
        edit_status = findViewById(R.id.textStatus);
        edit_chest = findViewById(R.id.textchest);
        edit_pents = findViewById(R.id.textpents);
        edit_shoes = findViewById(R.id.textshoes);
        textid = findViewById(R.id.textid);
        leave = findViewById(R.id.leave);
        password_change = findViewById(R.id.password_change);

        //파이어베이스
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        //uid 저장
        currentUser = firebaseAuth.getCurrentUser();
        uid = currentUser.getUid();

        //아이디 보여주게 하는 곳
        SharedPreferences userinfo = getSharedPreferences("userinfo",MODE_PRIVATE);
        id = userinfo.getString("email","");
        textid.setText(id);

        //사진 보여주게 하는 곳
        imagephoto=userinfo.getString("imageUri","");
        imageUri = Uri.parse(imagephoto);
        Glide.with(this).load(imageUri).apply(RequestOptions.circleCropTransform()).into(gallery);
        //gallery.setImageURI(imageUri); // 첫 화면 프로필 사진 보여주기

        //신체사이즈 보여주게 하는 곳
        status = userinfo.getString("status","");
        edit_status.setText(status);
        chest = userinfo.getString("chest","");
        edit_chest.setText(chest);
        pents = userinfo.getString("pents","");
        edit_pents.setText(pents);
        shoes = userinfo.getString("shoes","");
        edit_shoes.setText(shoes);
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

   //회원탈퇴 다이얼로그
    void leaveshow()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원탈퇴");
        builder.setMessage("탈퇴 하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                SharedPreferences userinfo = getSharedPreferences("userinfo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = userinfo.edit();
                                editor.clear();
                                editor.commit();
                                databaseReference.child("users").child(uid).removeValue();
                                Toast.makeText(getApplicationContext(),"탈퇴되었습니다.",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MyPage_Setting.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"취소 되었습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    //로그아웃 다이얼로그
    void logoutshow()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그아웃");
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences userinfo = getSharedPreferences("userinfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = userinfo.edit();
                        editor.clear();
                        editor.commit();
                        Toast.makeText(getApplicationContext(),"로그아웃 되었습니다.",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MyPage_Setting.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"취소 되었습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    //이미지 선택창 띄우기
    void imageshow()
    {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("앨범에서 가져오기");
        ListItems.add("기본 이미지로 설정");
        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이미지 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();

                if(selectedText.equals("앨범에서 가져오기")){
                    goToAlbum();
                }else if(selectedText.equals("기본 이미지로 설정")){
                    //drawable 에 저장된 이미지 uri로 저장시키기
                    imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + getResources().getResourcePackageName(R.drawable.photo)
                            + '/' + getResources().getResourceTypeName(R.drawable.photo) + '/' + getResources().getResourceEntryName(R.drawable.photo) );
                    Log.d("기본",""+imageUri);
                    gallery.setImageURI(imageUri);
                }
            }
        });
        builder.show();
    }

    //앨범으로 이동
    private void goToAlbum(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent,REQ_CODE_SELECT_IMAGE);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart_OK");
        super.onStart();
    }

    void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MyPage_Setting.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.password_change,null);
        builder.setView(view);

        Button change = view.findViewById(R.id.buttonChange);
        final EditText password = view.findViewById(R.id.edittextPassword);
        final EditText newpassword = view.findViewById(R.id.edittextNewPassword);
        final EditText newpassword_check = view.findViewById(R.id.edittextNewPasswordCheck);

        final AlertDialog dialog = builder.create();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strpasswd = password.getText().toString();
                SharedPreferences userinfo = getSharedPreferences("userinfo",MODE_PRIVATE);
                if(strpasswd.length()==0){
                    Toast.makeText(MyPage_Setting.this,"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }
                if(!(strpasswd.equals(userinfo.getString("passwd","")))){
                    Toast.makeText(MyPage_Setting.this,"기존 비밀번호가 다릅니다.",Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }
                if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$",newpassword.getText().toString())){ //비밀번호 형식 이상할 때
                    Toast.makeText(MyPage_Setting.this,"비밀번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show();
                    newpassword.requestFocus();
                    return;
                }else if(!(newpassword.getText().toString().equals(newpassword_check.getText().toString()))){
                    Toast.makeText(MyPage_Setting.this,"비밀번호를 다시 확인해주세요.",Toast.LENGTH_SHORT).show();
                    newpassword.requestFocus();
                    return;
                }else if(newpassword.getText().toString().equals(strpasswd)){
                    Toast.makeText(MyPage_Setting.this,"기존 비밀번호와 동일합니다.",Toast.LENGTH_SHORT).show();
                    newpassword.requestFocus();
                    return;
                }
                currentUser.updatePassword(newpassword_check.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.e(TAG, "비밀번호 변경 되었음?");
                            Toast.makeText(MyPage_Setting.this,"비밀번호 변경 되었습니다.",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else
                            Log.e(TAG, "onComplete: 왜 안돼?" );
                    }
                });
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume_OK");

        password_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: ok?" );
                show();
            }
        });

        //탈퇴 버튼 눌렀을 때
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveshow();
            }
        });

        //완료 버튼 눌렀을 때, 신체사이즈 및 이미지사진 uri 세이브
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = edit_status.getText().toString().trim();
                chest=edit_chest.getText().toString().trim();
                pents=edit_pents.getText().toString().trim();
                shoes=edit_shoes.getText().toString().trim();
                save(status,chest,pents,shoes);
            }
        });

        //이전 버튼 눌렀을 때
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //로그아웃 눌렀을때
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutshow();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                imageshow();
            }
        });
    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        private CustomProgressDialog asyncDialog;

        public CheckTypesTask(MyPage_Setting activity){
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
                Thread.sleep(500);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            switch (requestCode) {
                case REQ_CODE_SELECT_IMAGE :
                    dispatchCropPictureIntent(data.getData());
                    break;

                case REQUEST_IMAGE_CROP :
                    Bitmap bitmap = data.getParcelableExtra("data");
                    imageUri = getImageUri(bitmap);
                    Log.e("크롭Uri확인", "onActivityResult: "+imageUri);
                    Glide.with(this).load(bitmap).apply(RequestOptions.circleCropTransform()).into(gallery);
                    break;
            }
        }
    }
    //비트맵을 Uri로 바꾸는 방식
    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void save(String status, String chest, String pents, String shoes){
        Log.d("사이즈",status);
        Log.d("사이즈",chest);
        Log.d("사이즈",pents);
        Log.d("사이즈",shoes);
        if(TextUtils.isEmpty(status)||TextUtils.isEmpty(chest)|| TextUtils.isEmpty(pents)||TextUtils.isEmpty(shoes)){
            Toast.makeText(MyPage_Setting.this,"신체 사이즈를 입력해주세요.",Toast.LENGTH_SHORT).show();
        }else {
            Intent result = new Intent();
            User size = new User(status, chest, pents, shoes);//사이즈
            databaseReference.child("users").child(uid).child("size").setValue(size);
            databaseReference.child("users").child(uid).child("imageUri").setValue(imageUri.toString());
            uploadFile();
            SharedPreferences userinfo = getSharedPreferences("userinfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = userinfo.edit();
            editor.putString("imageUri", imageUri.toString());
            editor.putString("status",status);
            editor.putString("chest",chest);
            editor.putString("pents",pents);
            editor.putString("shoes",shoes);
            editor.commit();
//                task = new CheckTypesTask(MyPage_Setting.this);
//                task.execute();
            setResult(100, result);
            finish();
        }
    }

    //upload the file
    private void uploadFile() {

        final DatabaseReference suitReference = firebaseDatabase.getReference().child("user-posts").child(uid);

        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    Log.e(TAG, "onDataChange: "+key);
                    databaseReference.child("posts").child(key).child("profileUri").setValue(downloadUri.toString());
                    suitReference.child(key).child("profileUri").setValue(downloadUri.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: "+databaseError);
            }
        };

        if(imageUri.toString().equals("android.resource://com.example.suitlink/drawable/photo")){
            databaseReference.child("users").child(uid).child("imageUri").setValue(imageUri.toString());
        }else if (!imageUri.toString().equals("android.resource://com.example.suitlink/drawable/photo") && !imageUri.toString().isEmpty()) {
            final FirebaseStorage storage = FirebaseStorage.getInstance();
            //날짜와 시간으로 Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".jpeg";
            //storage 주소와 폴더 파일명을 지정해 준다.
            final StorageReference storageRef = storage.getReferenceFromUrl("gs://suitlink-a202d.appspot.com").child("images/" + filename);

            //스토리지의 url을 실시간데이터베이스에도 url형식으로 받을 수 있게 해준다.
            storageRef.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(Task<Uri> task) {
                    if(task.isSuccessful()) {
                        downloadUri = task.getResult();
                        suitReference.addListenerForSingleValueEvent(postListener);
                        databaseReference.child("users").child(uid).child("imageUri").setValue(downloadUri.toString());
                        SharedPreferences userinfo = getSharedPreferences("userinfo",MODE_PRIVATE);
                        SharedPreferences.Editor editor = userinfo.edit();
                        editor.putString("imageUri",downloadUri.toString());
                        editor.apply();
                        //업로드 완료
                    }else{
                        //업로드 실패
                    }
                }
            });
        }else {
            Toast.makeText(getApplicationContext(), "사진을 선택하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void dispatchCropPictureIntent(Uri uri) {
        Intent cropPictureIntent = new Intent("com.android.camera.action.CROP");
        cropPictureIntent.setDataAndType(uri, "image/*");

        //crop한 이미지를 저장할 때, 100*100
        cropPictureIntent.putExtra("outputX", 100); // crop한 이미지의 x축 크기 (integer)
        cropPictureIntent.putExtra("outputY", 100); // crop한 이미지의 y축 크기 (integer)
        cropPictureIntent.putExtra("aspectX", 1); // crop 박스의 x축 비율 (integer)
        cropPictureIntent.putExtra("aspectY", 1); // crop 박스의 y축 비율 (integer)
        cropPictureIntent.putExtra("scale", true);
        cropPictureIntent.putExtra("return-data", true);
        if (cropPictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cropPictureIntent, REQUEST_IMAGE_CROP);
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
        //task.cancel(true);
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
