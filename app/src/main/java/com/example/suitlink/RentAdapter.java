package com.example.suitlink;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

public class RentAdapter extends PagerAdapter {

    private static final String TAG = "ViewPagerAdapter";
    LayoutInflater inflater;
    Context mcontext;
    Boolean check;

    public RentAdapter(LayoutInflater inflater, Context context, Boolean check) {
        // TODO Auto-generated constructor stub
        //전달 받은 LayoutInflater를 멤버변수로 전달
        this.inflater=inflater;
        this.mcontext = context;
        this.check = check;
    }

    @Override

    public int getCount() {

        return 1; //이미지 개수 리턴
    }

    @Override

    public Object instantiateItem(final ViewGroup container, final int position) {

        // TODO Auto-generated method stub

        Log.d("위치 값 확인",""+position);

        View view= inflater.inflate(R.layout.viewpager_childview, null);

        ImageView img= view.findViewById(R.id.img_viewpager_childimage);

        //현재 position에 해당하는 이미지를 setting
        SharedPreferences office_suit = mcontext.getSharedPreferences("office_suit",Context.MODE_PRIVATE);
        img.setImageResource(office_suit.getInt("office_suit",100));

        //ViewPager에 만들어 낸 View 추가
        container.addView(view);

        //Image가 세팅된 View를 리턴
        return view;

    }

    @Override

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
    @Override

    public boolean isViewFromObject(View v, Object obj) {

        // TODO Auto-generated method stub

        return v==obj;

    }

}
