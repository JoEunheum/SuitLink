package com.example.suitlink;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import me.relex.circleindicator.CircleIndicator;

public class OpenMaleSuit extends BaseActivity {
    private ImageView previous;
    private ViewPager pager;
    private TextView retal_time_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_male_suit);

        previous = findViewById(R.id.imagePrevious);
        pager=findViewById(R.id.officepager);

        OpenMaleSuitAdapter adapter = new OpenMaleSuitAdapter(getLayoutInflater(),OpenMaleSuit.this);
        pager.setAdapter(adapter);
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        retal_time_price = findViewById(R.id.retal_time_price);

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences office_name = getSharedPreferences("office_name",MODE_PRIVATE);
        String name = office_name.getString("office_name","");
        if(name.equals("employment_wing")){
            retal_time_price.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //이전버튼 클릭했을 때
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
