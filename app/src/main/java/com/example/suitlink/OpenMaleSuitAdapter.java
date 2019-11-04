package com.example.suitlink;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class OpenMaleSuitAdapter extends PagerAdapter {
    private static final String TAG = "OpenFemaleSuitAdapter";
    private LayoutInflater inflater;
    private Context context;


    public OpenMaleSuitAdapter(LayoutInflater inflater, Context context) {
        // TODO Auto-generated constructor stub
        //전달 받은 LayoutInflater를 멤버변수로 전달
        this.inflater=inflater;
        this.context=context;
    }

    @Override
    public int getCount() {
        //사진 4장만 보여주자
        return 4;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        // TODO Auto-generated method stub
        Log.d("위치 값 확인",""+position);

        View view=null;

        view= inflater.inflate(R.layout.viewpager_childview, null);

        //만들어진 View안에 있는 ImageView 객체 참조

        //위에서 inflated 되어 만들어진 view로부터 findViewById()를 해야 하는 것에 주의.

        ImageView img= view.findViewById(R.id.img_viewpager_childimage);

        img.setImageResource(R.drawable.malesuit1+position);

        final int malesuit = R.drawable.malesuit1+position;

        //ViewPager에 만들어 낸 View 추가
        container.addView(view);


        //뷰페이저 사진 선택하면 그 선택화면으로 이동
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: "+position);
                show(malesuit, context);
            }
        });

        //Image가 세팅된 View를 리턴
        return view;
    }

    //instantiateItem() 메소드에서 리턴된 Object가 View가 맞는지 확인하는 메소드드
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        //뷰페이지에서 안보이는 뷰는 제거
        container.removeView((View)object);
    }

    void show(int femalesuit, Context context){
        SharedPreferences office_name = context.getSharedPreferences("office_name",Context.MODE_PRIVATE);
        String name = office_name.getString("office_name","");
        if(name.equals("employment_wing")){
            Intent intent = new Intent(context, Wing_FemaleData.class);
            intent.putExtra("image", femalesuit);
            context.startActivity(intent);
        }else {
            Intent intent = new Intent(context, Open_FemaleData.class);
            intent.putExtra("image", femalesuit);
            context.startActivity(intent);
        }
    }
}
