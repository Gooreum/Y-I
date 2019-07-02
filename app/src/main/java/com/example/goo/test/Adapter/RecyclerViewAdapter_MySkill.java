package com.example.goo.test.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.goo.test.Item.Item_Career;
import com.example.goo.test.Item.Item_MySkill;
import com.example.goo.test.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Goo on 2018-05-27.
 */

public class RecyclerViewAdapter_MySkill extends RecyclerView.Adapter<RecyclerViewAdapter_MySkill.MyViewHolder> {

    private List<Item_MySkill> listItems;
    private Context context;


    //어댑터 생성자
    public RecyclerViewAdapter_MySkill(Context context, List<Item_MySkill> listItems) {
        this.context = context;
        this.listItems = listItems;

    }
    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView myskill,explain;
        private CircleImageView prof_pic;

        public MyViewHolder(View itemView) {
            super(itemView);




            //어떤 xml 요소를 뷰홀더로 잡을지 지정해줌.
            mCardView = itemView.findViewById(R.id.mCardView);
            myskill = itemView.findViewById(R.id.myskill);
            explain = itemView.findViewById(R.id.explain);




        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_my_skill, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item_MySkill item = listItems.get(position);
        holder.myskill.setText(item.myskill);
        holder.explain.setText(item.explain);

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
