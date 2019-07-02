package com.example.goo.test.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goo.test.Item.Item_Career;
import com.example.goo.test.Item.Item_Interesting;
import com.example.goo.test.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Goo on 2018-05-27.
 */

public class RecyclerViewAdapter_Career extends RecyclerView.Adapter<RecyclerViewAdapter_Career.MyViewHolder> {

    private List<Item_Career> listItems;
    private Context context;


    //어댑터 생성자
    public RecyclerViewAdapter_Career(Context context, List<Item_Career> listItems) {
        this.context = context;
        this.listItems = listItems;

    }
    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView position,company,region,start_day,end_day,explain;
        private CircleImageView prof_pic;
        private ImageView delete,motify;
        public MyViewHolder(View itemView) {
            super(itemView);




            //어떤 xml 요소를 뷰홀더로 잡을지 지정해줌.
            mCardView = itemView.findViewById(R.id.mCardView);
            position = itemView.findViewById(R.id.position);
            company = itemView.findViewById(R.id.company);
            region = itemView.findViewById(R.id.region);
            start_day = itemView.findViewById(R.id.start_day);
            end_day = itemView.findViewById(R.id.end_day);

           // explain = itemView.findViewById(R.id.explain);




        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_career, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item_Career item = listItems.get(position);
        holder.position.setText(item.position);
        holder.company.setText(item.company);
        holder.region.setText(item.region);
        holder.start_day.setText(item.start_day + " - ");
        holder.end_day.setText(item.end_day);


       // holder.explain.setText(item.explain);
    }

    @Override
    public int getItemCount() {
        if(listItems.size() > 3){
            return 3;
        }else{
        }
        return listItems.size();

    }
}
