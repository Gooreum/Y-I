package com.example.goo.test.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goo.test.Fragment.MyInfo.MyInfoFragment_By_Friend;
import com.example.goo.test.Item.Item_Interesting;
import com.example.goo.test.Item.Item_Recommend;
import com.example.goo.test.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-27.
 */

public class RecyclerViewAdapter_Recommend extends RecyclerView.Adapter<RecyclerViewAdapter_Recommend.MyViewHolder> {

    private List<Item_Recommend> listItems;
    private Context context;


    //어댑터 생성자
    public RecyclerViewAdapter_Recommend(Context context, List<Item_Recommend> listItems) {
        this.context = context;
        this.listItems = listItems;

    }

    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView username, recommend_content;
        private CircleImageView prof_pic;
        private ImageView delete, motify;

        public MyViewHolder(View itemView) {
            super(itemView);


            //어떤 xml 요소를 뷰홀더로 잡을지 지정해줌.
            mCardView = itemView.findViewById(R.id.mCardView);
            username = itemView.findViewById(R.id.username);
            recommend_content = itemView.findViewById(R.id.recommend_content);
            prof_pic = itemView.findViewById(R.id.prof_pic);


        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_recommend, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item_Recommend item = listItems.get(position);
        holder.username.setText(item.username);
        holder.recommend_content.setText(item.recommend_content);
        Picasso.with(context).load(item.url).error(R.mipmap.ic_launcher_round).into(holder.prof_pic);
        //친구 프로필 사진을 눌렀을 때 해당 친구의 프로필 화면으로 옮김.
        holder.prof_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                System.out.println("sjldfkjsladkfjloskejdflkjweikfjwoeijf  " + listItems.get(position).email);
                //친구의 이메일 값을 쉐어드로 저장해둠.
                SharedPreferences ID = context.getApplicationContext().getSharedPreferences("friend_email", MODE_PRIVATE);
                SharedPreferences.Editor editor = ID.edit();
                editor.putString("friend_email", listItems.get(position).email);
                editor.commit();

                MyInfoFragment_By_Friend myinfo = new MyInfoFragment_By_Friend();
                myinfo.setArguments(bundle);

                ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, myinfo).addToBackStack(null)
                        .commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        if (listItems.size() > 10) {
            return 10;

        } else {
            return listItems.size();

        }
    }
}
