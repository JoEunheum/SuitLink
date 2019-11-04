package com.example.suitlink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class OpenRental extends BaseActivity {
    private ImageView previous;
    private ImageView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_suit);

        previous = findViewById(R.id.imagePrevious);
        next = findViewById(R.id.imageNext);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //이전 버튼 클릭했을 때
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //다음 버튼 클릭했을 때
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpenRental.this,Male_or_Female.class);
                intent.putExtra("office_name","open_closet");
                intent.putExtra("office_address", "서울특별시 광진구 화양동 아차산로 213");
                intent.putExtra("office_number","02-6929-1020");
                startActivity(intent);
            }
        });
    }
}
