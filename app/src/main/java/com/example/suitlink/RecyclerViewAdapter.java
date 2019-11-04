package com.example.suitlink;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>{
    private static final String TAG = "ReAdapter";
    private ArrayList<Personal_Item> mList;
    private Context mContext;
    private String uid;

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class CustomViewHolder extends RecyclerView.ViewHolder /*implements View.OnCreateContextMenuListener*/ {
        protected ImageView profile_image;
        protected TextView profile_id;
        protected ImageButton cardview_edit;
        protected ImageView suit_image; // 카드뷰에 들어갈 이미지 뷰

        // 편집을 눌렀을 때 액티비티에 뿌려줄 아이템
        protected ImageView suit_photo; // 편집했을 때 들어가야할 이미지 뷰
        protected CheckBox jacket;
        protected CheckBox chest;
        protected CheckBox pents;
        protected CheckBox shoes;
        protected CheckBox etc;
        protected EditText color;
        protected EditText size;
        protected EditText price;
        protected EditText number;
        protected EditText address;
        protected Button rental_Time;
        protected Button return_Time;
        protected RadioGroup rental_How;
        protected Button update;
        protected Boolean checkuid=false;


        public CustomViewHolder(View view){
            super(view);
            this.profile_image = view.findViewById(R.id.profile_image);
            this.profile_id = view.findViewById(R.id.textName);
            this.cardview_edit = view.findViewById(R.id.cardview_edit);
            this.suit_image = view.findViewById(R.id.suits_image);

            this.suit_photo = view.findViewById(R.id.suits_image);
            this.jacket = view.findViewById(R.id.radiojacket);
            this.chest = view.findViewById(R.id.radiochest);
            this.pents= view.findViewById(R.id.radiopents);
            this.shoes = view.findViewById(R.id.radioshoes);
            this.etc = view.findViewById(R.id.radiotie);
            this.color = view.findViewById(R.id.textcolor);
            this.size = view.findViewById(R.id.textSize);
            this.price = view.findViewById(R.id.textprice);
            this.number = view.findViewById(R.id.textNumber);
            this.address = view.findViewById(R.id.textAddress);
            this.rental_Time = view.findViewById(R.id.textRental_time);
            this.return_Time = view.findViewById(R.id.textReturn_time);
            this.rental_How = view.findViewById(R.id.rental_how);
            this.update = view.findViewById(R.id.update);

            //수정기능이나 삭제기능
            cardview_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "postuid확인: "+mList.get(getAdapterPosition()).getPost_uid() );
                    show();
                }
            });
        }

        public void show(){
            final List<String> ListItems = new ArrayList<>();
            ListItems.add("편집");
            ListItems.add("삭제");

            final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, final int which) {
                    Log.e("편집 삭제 클릭시","position = "+getAdapterPosition());

                    SharedPreferences userinfo = mContext.getSharedPreferences("userinfo",mContext.MODE_PRIVATE);
                    uid = userinfo.getString("uid","");
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    final DatabaseReference postsReference = firebaseDatabase.getReference().child("posts");
                    final DatabaseReference userpostReference = firebaseDatabase.getReference().child("user-posts");
                    Log.e(TAG, "onClick: uid 확인 : "+mList.get(getAdapterPosition()).getPost_uid());

                    if(uid.equals(mList.get(getAdapterPosition()).getPost_uid())){
                        checkuid = true;
                    }
                    if(checkuid){
                        switch (which){
                            case 0 : //편집 버튼 눌렀을 때 반응
                                Intent intent = new Intent(mContext,Personal_Edit.class); // 편집화면으로 다시 뿌려줘야한다.
                                intent.putExtra("post_key",mList.get(getAdapterPosition()).getPost_key());
                                intent.putExtra("position",getAdapterPosition());
                                Log.e(TAG, "onDataChange: 키 값 확인"+mList.get(getAdapterPosition()).getPost_key());
                                ((Activity)mContext).startActivityForResult(intent,101);
                                break;

                            case 1 : //삭제 버튼 눌렀을 때 반응
                                postsReference.child(mList.get(getAdapterPosition()).getPost_key()).removeValue();
                                userpostReference.child(uid).child(mList.get(getAdapterPosition()).getPost_key()).removeValue();
                                break;
                        }
                    }else{
                        Toast.makeText(itemView.getContext(),"본인 글만 편집할 수 있습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.show();
        }
    }

    //생성자에서 데이터 리스트 객체를 전달받음.
    public RecyclerViewAdapter(Context context, ArrayList<Personal_Item> list){
        this.mContext = context;
        this.mList = list;
    }


    // RecyclerView에 새로운 데이터를 보여주기 위해 필요한 ViewHolder를 생성해야 할 때 호출됩니다.
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.personal_item,viewGroup,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder; // OnBindViewHolder로 넘겨줌
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewHolder, final int position){
        Log.e(TAG, "onBindViewHolder: "+mList.get(position).get_profile_id() );
        Glide.with(mContext).load(Uri.parse(mList.get(position).get_profile_image())).apply(RequestOptions.circleCropTransform()).into(viewHolder.profile_image);
        viewHolder.profile_id.setText(mList.get(position).get_profile_id());
        viewHolder.cardview_edit.setImageResource(mList.get(position).get_cardview_edit());
        Glide.with(mContext).load(mList.get(position).get_suit_image()).into(viewHolder.suit_image);

        //아이템뷰를 클릭했을 때 위치값 로그로 찍어주기
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //위치 값 잘 가져온다.
                    Log.d("Recyclerview","position = "+position);
                    Log.e(TAG, "아이템 클릭했을 때 postkey : "+mList.get(position).getPost_key());
                    Log.e(TAG, "아이템 클릭했을 때 postuid : "+mList.get(position).getPost_uid());
                    Intent intent = new Intent(mContext,Personal_Suit_Status.class);
                    intent.putExtra("post_uid",mList.get(position).getPost_uid());
                    intent.putExtra("post_key",mList.get(position).getPost_key());
                    intent.putExtra("post_name",mList.get(position).getPost_name());
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
