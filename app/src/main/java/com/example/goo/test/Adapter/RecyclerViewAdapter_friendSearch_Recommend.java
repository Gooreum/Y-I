package com.example.goo.test.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.goo.test.Item.ListItem_friendSearch;
import com.example.goo.test.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Goo on 2018-05-23.
 */

public class RecyclerViewAdapter_friendSearch_Recommend extends RecyclerView.Adapter<RecyclerViewAdapter_friendSearch_Recommend.MyViewHolder> {
    private List<ListItem_friendSearch> listItems;
    Context context;

    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextView, username;
        private CircleImageView prof_pic;

        public MyViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            prof_pic = (CircleImageView) itemView.findViewById(R.id.prof_pic);
            mCardView = itemView.findViewById(R.id.cardview_rec_friend);

        }


    }

    //어댑터 생성자
    public RecyclerViewAdapter_friendSearch_Recommend(Context context, List<ListItem_friendSearch> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_search_horizontal, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ListItem_friendSearch item = listItems.get(position);
        holder.username.setText(item.username);
        Picasso.with(context).load(item.url).error(R.mipmap.ic_launcher_round).into(holder.prof_pic);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
