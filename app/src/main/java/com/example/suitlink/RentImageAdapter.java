package com.example.suitlink;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RentImageAdapter extends PagerAdapter {

    private static final String TAG = "ViewPagerAdapter";
    LayoutInflater inflater;
    Context mcontext;
    Boolean check;

    public RentImageAdapter(LayoutInflater inflater, Context context, Boolean check) {
        // TODO Auto-generated constructor stub
        //전달 받은 LayoutInflater를 멤버변수로 전달
        this.inflater=inflater;
        this.mcontext = context;
        this.check = check;
    }

    //PagerAdapter가 가지고 잇는 View의 개수를 리턴

    //보통 보여줘야하는 이미지 배열 데이터의 길이를 리턴

    @Override

    public int getCount() {
        SharedPreferences suituri = mcontext.getSharedPreferences("SuitUri",Context.MODE_PRIVATE);
        String json = suituri.getString("imageList","");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> suitUris = gson.fromJson(json,type);

        // TODO Auto-generated method stub
        return suitUris.size(); //이미지 개수 리턴
    }

    @Override

    public Object instantiateItem(final ViewGroup container, final int position) {

        // TODO Auto-generated method stub

        Log.d("위치 값 확인",""+position);

        View view= inflater.inflate(R.layout.viewpager_childview, null);

        ImageView img= view.findViewById(R.id.img_viewpager_childimage);

        SharedPreferences suituri = mcontext.getSharedPreferences("SuitUri",Context.MODE_PRIVATE);
        String json = suituri.getString("imageList","");
        Log.d("json",json);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        Log.d("type",""+type);
        final ArrayList<String> suitUris = gson.fromJson(json,type);
        Log.d("suitUri",""+suitUris);
        String suri = suitUris.get(position); // 클릭했던 이미지의 절대값을 넘겨준다.
        Log.d("suri",suri);
        Glide.with(mcontext).load(suri).into(img);

        //ViewPager에 만들어 낸 View 추가
        container.addView(view);

        //Image가 세팅된 View를 리턴
        return view;

    }

    @Override

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    //instantiateItem() 메소드에서 리턴된 Ojbect가 View가  맞는지 확인하는 메소드
    @Override

    public boolean isViewFromObject(View v, Object obj) {

        // TODO Auto-generated method stub

        return v==obj;

    }
}
