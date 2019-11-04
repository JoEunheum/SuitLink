package com.example.suitlink;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

public class Personal_Add extends BaseActivity implements DatePickerDialog.OnDateSetListener{

    private static final String TAG= "Personal_Add_Activity";
    final int REQ_CODE_SELECT_IMAGE=100;
    public ImageView gallery;
    private  String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE/*, Manifest.permission.CAMERA*/};
    private static final int MULTIPLE_PERMISSIONS=101; // 권한 동의 여부 문의 후 CallBack 메소드에 쓰일 변수수
    private RadioButton radioHome, radioDelivery;
    private CheckBox radioJacket, radioChest, radioPents, radioShoes, radioEtc;
    private EditText editColor, editSize, editPrice, editAddress, editNumber;
    private Button upLoad, rental_Time,return_Time;
    private Boolean datecheck;
    private String username;
    private String profileUri;
    private Uri imageUri;
    private Date currentDate;
    private String TodayDateString;
    private String currentDateString;
    private ViewPager pager;
    private Button addsuitimage;
    private ArrayList<String> suituris;
    private TextView division;
    private String size, color, price, address, number, rental_time, return_time, rental_how, uid;
    private ArrayList<String> divi;
    private String key;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String rental_check;
    private CheckTypesTask lodingtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_add);
        checkPermissions();//권한 묻기

         //xml 과 연결
        gallery = (ImageView) findViewById(R.id.suits_image);
        radioJacket = (CheckBox) findViewById(R.id.radiojacket);
        radioChest = (CheckBox)findViewById(R.id.radiochest);
        radioPents = (CheckBox)findViewById(R.id.radiopents);
        radioShoes = (CheckBox)findViewById(R.id.radioshoes);
        radioEtc = (CheckBox)findViewById(R.id.radiotie);
        radioHome = (RadioButton)findViewById(R.id.radiohome);
        radioDelivery = (RadioButton)findViewById(R.id.radiodelivery);
        editColor = (EditText)findViewById(R.id.textcolor);
        editSize = (EditText)findViewById(R.id.textSize);
        editPrice = (EditText)findViewById(R.id.textprice);
        editAddress = (EditText)findViewById(R.id.textAddress);
        editNumber = (EditText)findViewById(R.id.textNumber);
        upLoad = (Button)findViewById(R.id.update);
        rental_Time = (Button)findViewById(R.id.textRental_time);
        return_Time = (Button)findViewById(R.id.textReturn_time);
        addsuitimage = (Button)findViewById(R.id.add_photo);
        division = (TextView)findViewById(R.id.division);

        pager=(ViewPager)findViewById(R.id.pager);
        divi = new ArrayList<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        uid = currentUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

//        suitUris = new ArrayList<>();
        suituris = new ArrayList<>();

        getDateToday();
    }


    //현재 날짜
    protected void getDateToday(){
        currentDate = new Date();
        SimpleDateFormat sdfYear = new SimpleDateFormat("yy");
        SimpleDateFormat sdfMon = new SimpleDateFormat("M");
        SimpleDateFormat sdfDay = new SimpleDateFormat("d");
        TodayDateString = sdfYear.format(currentDate)+". "+sdfMon.format(currentDate)+". "+sdfDay.format(currentDate)+".";
        rental_Time.setText(TodayDateString);
        long daydate = currentDate.getTime();
        long adddate = daydate+(24*(60*(60*1000)))*3; // daydate+(24*(60*(60*1000))) 현재 날짜에 1일 추가, x3 했으니 3일 추가
        currentDate.setTime(adddate);
        TodayDateString = sdfYear.format(currentDate)+". "+sdfMon.format(currentDate)+". "+sdfDay.format(currentDate)+".";
        return_Time.setText(TodayDateString);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume_OK");

        editAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true) {
                    Intent intent = new Intent(Personal_Add.this, AddressActivity.class);
                    startActivityForResult(intent,500);
                }
            }
        });

        //날짜 보여주기
        rental_Time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                datecheck=false;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date Picker");
            }
        });

        return_Time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                datecheck=true;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date Picker");
            }
        });

        // 올리기 버튼 눌렀을 때 반응
       upLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gallery.getDrawable() == null) {
                    Toast.makeText(Personal_Add.this, "사진을 넣어주세요.", Toast.LENGTH_SHORT).show();
                    gallery.requestFocus();
                    return;
                }
                if (radioJacket.isChecked() == false && radioChest.isChecked() == false && radioPents.isChecked() == false && radioShoes.isChecked() == false && radioEtc.isChecked() == false) {
                    Toast.makeText(Personal_Add.this, "구분을 정해주세요.", Toast.LENGTH_SHORT).show();
                    division.requestFocus();
                    return;
                }
                if(radioJacket.isChecked()==true){
                    divi.add("자켓");
                    Log.e(TAG, "onClick: "+divi );
                }if(radioChest.isChecked()==true){
                    divi.add("상의");
                    Log.e(TAG, "onClick: "+divi );
                }if(radioPents.isChecked()==true){
                    divi.add("하의");
                    Log.e(TAG, "onClick: "+divi );
                }if(radioShoes.isChecked()==true){
                    divi.add("구두");
                    Log.e(TAG, "onClick: "+divi );
                }if(radioEtc.isChecked()==true){
                    divi.add("기타");
                    Log.e(TAG, "onClick: "+divi );
                }
                if (editColor.getText().toString().length() == 0) {
                    Toast.makeText(Personal_Add.this, "색상을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    editColor.requestFocus();
                    return;
                }else {
                    color=editColor.getText().toString();
                }
                if (editSize.getText().toString().length() == 0) {
                    Toast.makeText(Personal_Add.this, "사이즈를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    editSize.requestFocus();
                    return;
                }else{
                    size = editSize.getText().toString();
                }
                if (editPrice.getText().toString().length() == 0) {
                    Toast.makeText(Personal_Add.this, "가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    editPrice.requestFocus();
                    return;
                }else{
                    price = editPrice.getText().toString();
                }
                if (editNumber.getText().toString().length() == 0) {
                    Toast.makeText(Personal_Add.this, "연락처를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    editNumber.requestFocus();
                    return;
                }else{
                    number=editNumber.getText().toString();
                }
                if (editAddress.getText().toString().length() == 0) {
                    Toast.makeText(Personal_Add.this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    editAddress.requestFocus();
                    return;
                }else{
                    address = editAddress.getText().toString();
                }
                if (rental_Time.getText().equals(return_Time.getText())) {
                    Toast.makeText(Personal_Add.this, "대여기간을 다시 정해주세요.", Toast.LENGTH_SHORT).show();
                    rental_Time.requestFocus();
                    return;
                }else{
                    rental_time = rental_Time.getText().toString();
                    return_time = return_Time.getText().toString();
                }
                if (radioHome.isChecked() == false && radioDelivery.isChecked() == false) {
                    Toast.makeText(Personal_Add.this, "대여방법을 정해주세요.", Toast.LENGTH_SHORT).show();
                    radioHome.requestFocus();
                    return;
                }else if(radioHome.isChecked()==true){
                    rental_how = radioHome.getText().toString();
                }else{
                    rental_how = radioDelivery.getText().toString();
                }

                SharedPreferences userinfo = getSharedPreferences("userinfo",MODE_PRIVATE);
                username = userinfo.getString("name","");
                profileUri = userinfo.getString("imageUri","");
                save(size, color, price, address, number, rental_time, return_time, rental_how, uid, divi,username,profileUri);
                uploadFile();
                //인텐트로 값을 넘겨주고 그 다음 저장시켜주는게 좋을듯 하다.

                Intent result = new Intent();
//                result.putExtra("suit_image",imageUri); // 이미지 url값 넘기기
                setResult(100, result);
            }
        });

        // 사진 추가 눌렀을 때 반응
        addsuitimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                goToAlbum();
                gallery.setVisibility(View.INVISIBLE);
            }
        });

        //자켓을 눌렀을 때 반응
        radioJacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checked_Jacket(v);
            }
        });

        //상의를 눌렀을 때 반응
        radioChest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checked_Chest(v);
            }
        });

        //하의를 눌렀을 때 반응
        radioPents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checked_Pents(v);
            }
        });

        //구두를 눌렀을 때 반응
        radioShoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checked_Shoes(v);
            }
        });

        //기타를 눌렀을 때 반응
        radioEtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checked_Etc(v);
            }
        });

        //방문대여를 눌렀을 때 반응
        radioHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checked_how(v);
            }
        });
        //택배대여를 눌렀을 때 반응
        radioDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checked_how(v);
            }
        });
    }

    //구분 체크되었을 때 동작할 메소드 구현
    public String Checked_Jacket(View v) {

        String resultText = ""; // 체크되었을 때 저장할 스트링 값
        if (radioJacket.isChecked()) {
            resultText = "자켓";
        }
        return resultText;
    }

    public String Checked_Chest(View v) {

        String resultText = ""; // 체크되었을 때 저장할 스트링 값
        if(radioChest.isChecked()){
            resultText = "상의";
        }
        return resultText;
    }

    public String Checked_Pents(View v) {

        String resultText = ""; // 체크되었을 때 저장할 스트링 값
        if(radioPents.isChecked()){
            resultText = "하의";
        }
        return resultText;
    }

    public String Checked_Shoes(View v) {

        String resultText = ""; // 체크되었을 때 저장할 스트링 값
        if(radioShoes.isChecked()){
            resultText = "구두";
        }
        return resultText;
    }

    public String Checked_Etc(View v) {

        String resultText = ""; // 체크되었을 때 저장할 스트링 값
        if(radioEtc.isChecked()){
            resultText = "기타";
        }
        return resultText;
    }

    public String Checked_how(View v){
        String resultText = ""; // 체크되었을 때 저장할 스트링 값
        if(radioHome.isChecked()){
            resultText = "방문대여";
        }
        if(radioDelivery.isChecked()){
            resultText = "택배대여";
        }
        return resultText;
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

    //권한 동의 안했을 때
    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    //앨범으로 이동
    private void goToAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQ_CODE_SELECT_IMAGE);
    }

    public void theEnd(){
        lodingtask.cancel(true);
        finish();
    }

    //다이얼로그 데이터 표시
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        currentDateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(cal.getTime());

        if(!datecheck) {
            rental_Time.setText(currentDateString);
        }else {
            return_Time.setText(currentDateString);
        }
    }
    //앨범으로 이동 후 startActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode==Personal_Add.RESULT_OK)
            {

            SharedPreferences SuitUri = getSharedPreferences("SuitUri",Context.MODE_PRIVATE);
            String json = SuitUri.getString("imageList","nono");
            Gson gson = new Gson();

            if(!json.equals("nono")){ // 사진 정보가 있을 때
                Log.d("json",json);
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                Log.d("type",""+type);
                ArrayList<String> suitUris = gson.fromJson(json,type);
                suituris = suitUris;
            }

            if(data.getClipData()==null){ // 멀티 선택이 안되는 폰인 경우도 있음
                Log.d("1. single choice",String.valueOf(data.getData()));
                Cursor c = getContentResolver().query(Uri.parse(data.getData().toString()), null,null,null,null);
                c.moveToFirst();
                String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                c.close();
                suituris.add(absolutePath);
                if(suituris.size()>10){
                    Toast.makeText(Personal_Add.this,"사진은 10개까지 선택 가능합니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor editor = SuitUri.edit();
                json = gson.toJson(suituris);
                editor.putString("imageList",json);
                editor.commit();
                CustomAdapter adapter = new CustomAdapter(getLayoutInflater(),Personal_Add.this,false);
                pager.setAdapter(adapter);

                CircleIndicator indicator = findViewById(R.id.indicator);
                indicator.setViewPager(pager);
            }else{
                ClipData clipData = data.getClipData();
                Log.d("clidpdata",String.valueOf(data.getData()));
                if(clipData.getItemCount() > 10){
                    Toast.makeText(Personal_Add.this,"사진은 10개까지 선택 가능합니다.",Toast.LENGTH_SHORT).show();
                    gallery.setVisibility(View.VISIBLE);
                    return;
                }
                //멀티선택에서 하나만 선택했을 경우
//                    else if(clipData.getItemCount() ==1){
//                        Toast.makeText(Personal_Add.this,"사진이 부족합니다.",Toast.LENGTH_SHORT).show();
//                        gallery.setVisibility(View.VISIBLE);
//                        return;
//                    }
                else if(clipData.getItemCount()>=1&&clipData.getItemCount()<=10){
                    SharedPreferences.Editor editor = SuitUri.edit();

                    for(int i =0;i<clipData.getItemCount();i++){
                        Cursor c = getContentResolver().query(Uri.parse(clipData.getItemAt(i).getUri().toString()), null,null,null,null);
                        c.moveToNext();
                        String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                        c.close();
                        suituris.add(absolutePath);
                        json = gson.toJson(suituris);
                        editor.putString("imageList",json);
                        editor.commit();
                    }
                    CustomAdapter adapter = new CustomAdapter(getLayoutInflater(),Personal_Add.this,false);
                    pager.setAdapter(adapter);
                    CircleIndicator indicator = findViewById(R.id.indicator);
                    indicator.setViewPager(pager);
                }
            }
        }else {
//                if(suituris.size()==0) {
//                    gallery.setVisibility(View.VISIBLE);
//                }
            }
        }else if(requestCode==500){
            if(resultCode == 500) {
                String address = data.getStringExtra("address");
                editAddress.setText(address);
            }else if(resultCode == Activity.RESULT_CANCELED){ // 취소버튼을 눌렀을 때
            }
        }
    }

    //추가할 때 데이터베이스에 저장
    private void save(String size , String color, String price, String address,String number,String rental_time,String return_time, String rental_how, String uid, ArrayList<String> division, String username, String profileUri){
        //파이어베이스 설정

        key = databaseReference.child("posts").push().getKey(); // 작성 키를 만들어준다.
        rental_check = "false"; // 체크 값

        //쉐어드의 값을 저장시킨다.
        SharedPreferences SuitUri = getSharedPreferences("SuitUri",Context.MODE_PRIVATE);
        String json = SuitUri.getString("imageList","nono");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> suitUris = gson.fromJson(json,type);
        suituris = suitUris;

        Post post = new Post(size, color, price, address, number, rental_time, return_time, rental_how, uid, division,username,profileUri,suituris,rental_check); // 정장 정보의 객체

        Map<String,Object> postValues = post.toMap();

        Map<String,Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/"+key,postValues);
        childUpdates.put("/user-posts/"+uid+"/"+key,postValues);

        databaseReference.updateChildren(childUpdates);
    }

    // 로딩 어싱크테스크
    private class CheckTypesTask extends AsyncTask<Void, Integer, String> {

        private CustomProgressDialog asyncDialog;

        public CheckTypesTask(Personal_Add activity){
            asyncDialog = new CustomProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //파이어베이스
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            // show dialog
            asyncDialog.getWindow().setBackgroundDrawable(null);
            asyncDialog.show();
        }

        @Override
        protected String doInBackground(Void... result) {
             for(int i=0;i<suituris.size();i++) {
                final int j = i;
                imageUri = Uri.parse("file://"+suituris.get(i));
                Log.e("이미지 uri확인", "uploadFile: "+imageUri);
                //업로드할 파일이 있으면 수행
                if (imageUri != null) {
                    final FirebaseStorage storage = FirebaseStorage.getInstance();
                    //날짜와 시간으로 Unique한 파일명을 만들자.
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
                    Date now = new Date();
                    String filename = formatter.format(now) +i+ ".jpeg";
                    Log.e("파일이름 확인", "uploadFile: "+filename);
                    //storage 주소와 폴더 파일명을 지정해 준다.
                    final StorageReference storageRef = storage.getReferenceFromUrl("gs://suitlink-a202d.appspot.com").child("suit_images/" + filename);

                    //스토리지의 url을 실시간데이터베이스에도 url형식으로 받을 수 있게 해준다.
                    final int finalI = i;
                    Task<Uri> urlTask = storageRef.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return storageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                databaseReference.child("posts").child(key).child("suitimage").child(String.valueOf(finalI)).setValue(downloadUri.toString());
                                databaseReference.child("user-posts").child(uid).child(key).child("suitimage").child(String.valueOf(finalI)).setValue(downloadUri.toString());
                                if(j==suituris.size()-1){
                                    rental_check = "true";
                                    Log.e(TAG, "doInBackground: 반복문 마지막이면? "+rental_check); //여기에선 true로 변형되어있다..
                                    databaseReference.child("posts").child(key).child("rental_check").setValue(rental_check);
                                    databaseReference.child("user-posts").child(uid).child(key).child("rental_check").setValue(rental_check);
                                    Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                                    asyncDialog.dismiss();
                                    theEnd();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                            }
                            Log.e(TAG, "doInBackground: rental_check1 "+rental_check); //여기에선 true로 변형되어있다..
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "사진을 선택하세요.", Toast.LENGTH_SHORT).show();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress){
            Log.e(TAG, "onProgressUpdate: 실행되니?" );
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "onPostExecute: 체크 값 바꿔줘!"+result);
            super.onPostExecute(result);
        }
    }

    //upload the file
    private void uploadFile() {
        lodingtask = new CheckTypesTask(Personal_Add.this);
        lodingtask.execute();
    }

    @Override
    protected void onStop() {
        suituris.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy: OK");
        SharedPreferences SuitUri = getSharedPreferences("SuitUri",MODE_PRIVATE);
        SharedPreferences.Editor editor = SuitUri.edit();
        editor.clear();
        editor.commit();
        super.onDestroy();
    }

}
