package com.example.suitlink;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RentcyclerViewAdapter extends RecyclerView.Adapter<RentcyclerViewAdapter.RentViewHolder>{
    private static final String TAG = "RentAdapter";
    private ArrayList<Rent_Check_Item> mList;
    private Context mContext;

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class RentViewHolder extends RecyclerView.ViewHolder {
        protected ImageView suits_image;
        protected TextView name;
        protected TextView rent_time;
        protected TextView rent_status;
        protected TextView division;
        protected TextView colorSize;
        protected TextView rentHow;
        protected TextView price;



        public RentViewHolder(View view){
            super(view);
            this.suits_image = view.findViewById(R.id.suits_image);
            this.name = view.findViewById(R.id.textName);
            this.rent_time = view.findViewById(R.id.textRent_time);
            this.rent_status = view.findViewById(R.id.textRentStat);
            this.division = view.findViewById(R.id.textDivision);
            this.colorSize = view.findViewById(R.id.textColorSize);
            this.rentHow = view.findViewById(R.id.textRentHow);
            this.price = view.findViewById(R.id.textPrice);
        }
    }

    //생성자에서 데이터 리스트 객체를 전달받음.
    public RentcyclerViewAdapter(Context context, ArrayList<Rent_Check_Item> list){
        this.mContext = context;
        this.mList = list;
    }


    // RecyclerView에 새로운 데이터를 보여주기 위해 필요한 ViewHolder를 생성해야 할 때 호출됩니다.
    @Override
    public RentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rent_item,viewGroup,false);
        RentViewHolder viewHolder = new RentViewHolder(view);

        return viewHolder; // OnBindViewHolder로 넘겨줌
    }

    @Override
    public void onBindViewHolder(@NonNull RentViewHolder viewHolder, final int position){
        viewHolder.rent_time.setText(mList.get(position).getRent_time());
        viewHolder.name.setText(mList.get(position).getName());
        try{
            Glide.with(mContext).load(Uri.parse(mList.get(position).getSuit_image())).into(viewHolder.suits_image);
        }catch (NullPointerException e){
            Log.e(TAG, "onBindViewHolder: "+e);
            viewHolder.suits_image.setImageResource(mList.get(position).getOffice_image());
        }
        viewHolder.division.setText(mList.get(position).getDivision()); // 여기는 디비전이 어레이리스트라, toString으로 다 가져올 지 의문
        viewHolder.colorSize.setText(mList.get(position).getColor()+" / "+mList.get(position).getSize());
        if(mList.get(position).getStatus().equals("대여중")){
            viewHolder.rent_status.setText(mList.get(position).getStatus());
        }else {
            String strColor = "#FFFF0000";
            viewHolder.rent_status.setTextColor(Color.parseColor(strColor));
            viewHolder.rent_status.setText(mList.get(position).getStatus());
        }
        viewHolder.price.setText(mList.get(position).getPrice());

        //아이템뷰를 클릭했을 때 위치값 로그로 찍어주기
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //위치 값 잘 가져온다.
                    Log.d("Recyclerview","position = "+position);
                    Intent intent = new Intent(mContext,Rent_Suit_Status.class);
                    intent.putExtra("post_uid",mList.get(position).getPost_uid());
                    intent.putExtra("post_key",mList.get(position).getPost_key());
                    mContext.startActivity(intent);
                }
            });
    }

    //전체 데이터 개수 리턴
    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
