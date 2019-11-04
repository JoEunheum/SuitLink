package com.example.suitlink;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SelfcyclerViewAdapter extends RecyclerView.Adapter<SelfcyclerViewAdapter.SelfViewHolder>{
    private static final String TAG = "SelfAdapter";
    private ArrayList<Rent_Check_Item> mList;
    private Context mContext;

    //private MyRecyclerViewClickListener mListener;

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class SelfViewHolder extends RecyclerView.ViewHolder /*implements View.OnCreateContextMenuListener*/ {
        protected ImageView suits_image;
        protected TextView name;
        protected TextView rent_time;
        protected TextView rent_status;
        protected TextView division;
        protected TextView colorSize;
        protected TextView rentHow;
        protected TextView price;




        public SelfViewHolder(View view){
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
    public SelfcyclerViewAdapter(Context context, ArrayList<Rent_Check_Item> list){
        this.mContext = context;
        this.mList = list;
    }


    // RecyclerView에 새로운 데이터를 보여주기 위해 필요한 ViewHolder를 생성해야 할 때 호출됩니다.
    @Override
    public SelfViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rent_item,viewGroup,false);
        SelfViewHolder viewHolder = new SelfViewHolder(view);

        return viewHolder; // OnBindViewHolder로 넘겨줌
    }

    @Override
    public void onBindViewHolder(@NonNull SelfViewHolder viewHolder, final int position){

        viewHolder.rent_time.setText(mList.get(position).getRent_time());
        viewHolder.name.setText(mList.get(position).getName());
        Glide.with(mContext).load(Uri.parse(mList.get(position).getSuit_image())).into(viewHolder.suits_image);
        viewHolder.division.setText(mList.get(position).getDivision()); // 여기는 디비전이 어레이리스트라, toString으로 다 가져올 지 의문
        viewHolder.colorSize.setText(mList.get(position).getColor()+" / "+mList.get(position).getSize());
        //상태확인
        Log.e(TAG, "onBindViewHolder: 상태 확인 : "+mList.get(position).getStatus());

        if(mList.get(position).getStatus().equals("대여중")){
            String strColor = "#4CAF50";
            viewHolder.rent_status.setTextColor(Color.parseColor(strColor));
            viewHolder.rent_status.setText(mList.get(position).getStatus());
        }else if(mList.get(position).getStatus().equals("반납완료")){
            String strColor = "#FFFF0000";
            viewHolder.rent_status.setTextColor(Color.parseColor(strColor));
            viewHolder.rent_status.setText(mList.get(position).getStatus());
        }else if(mList.get(position).getStatus().equals("편집")){
            viewHolder.rent_status.setText(mList.get(position).getStatus());
        }else{ // 대기
            viewHolder.rent_status.setText(mList.get(position).getStatus());
        }
        viewHolder.price.setText(mList.get(position).getPrice()+" 원");

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!(mList.get(position).getStatus().equals("대여중") || mList.get(position).getStatus().equals("반납완료"))) {
                    show(position, mContext);
                }else
                    Toast.makeText(mContext,"편집 할 수 없습니다.",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //아이템뷰를 클릭했을 때 위치값 로그로 찍어주기
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //위치 값 잘 가져온다.
                Log.d("Recyclerview","position = "+position);
                Intent intent = new Intent(mContext,Self_Suit_Status.class);
                intent.putExtra("post_uid",mList.get(position).getPost_uid());
                intent.putExtra("post_key",mList.get(position).getPost_key());
                intent.putExtra("position",position);
                mContext.startActivity(intent);
            }
        });
    }

    //전체 데이터 개수 리턴
    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0); // 널값이 아니면 사이즈가 될때 동안 0 부터 실행
    }

    void show(final int position, final Context context) {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("편집");
        ListItems.add("삭제");

        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                Log.e("편집 삭제 클릭시","position = "+position);

                SharedPreferences userinfo = mContext.getSharedPreferences("userinfo",context.MODE_PRIVATE);
                String uid = userinfo.getString("uid","");
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference postsReference = firebaseDatabase.getReference().child("posts");
                final DatabaseReference userpostReference = firebaseDatabase.getReference().child("user-posts");
                switch (which){
                    case 0 : //편집 버튼 눌렀을 때 반응
                        Intent intent = new Intent(mContext,Personal_Edit.class); // 편집화면으로 다시 뿌려줘야한다.
                        intent.putExtra("post_key",mList.get(position).getPost_key());
                        intent.putExtra("position",position);
                        Log.e(TAG, "onDataChange: 키 값 확인"+mList.get(position).getPost_key());
                        ((Activity)mContext).startActivityForResult(intent,101);
                        break;

                        case 1 : //삭제 버튼 눌렀을 때 반응
                            postsReference.child(mList.get(position).getPost_key()).removeValue();
                            userpostReference.child(uid).child(mList.get(position).getPost_key()).removeValue();
                            break;
                    }
            }
        });
        builder.show();
        }
}
