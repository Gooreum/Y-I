package com.example.goo.test.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goo.test.Item.Item_Interesting;
import com.example.goo.test.Item.ListItem_Show_Project;
import com.example.goo.test.Item.ListItem_friendSearch;
import com.example.goo.test.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Goo on 2018-05-27.
 */

public class RecyclerViewAdapter_Interesting extends RecyclerView.Adapter<RecyclerViewAdapter_Interesting.MyViewHolder> {

    private List<Item_Interesting> listItems;
    private Context context;


    //어댑터 생성자
    public RecyclerViewAdapter_Interesting(Context context, List<Item_Interesting> listItems) {
        this.context = context;
        this.listItems = listItems;

    }
    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView txt_section,txt_reason;
        private CircleImageView prof_pic;
        private ImageView delete,motify;
        public MyViewHolder(View itemView) {
            super(itemView);




            //어떤 xml 요소를 뷰홀더로 잡을지 지정해줌.
            mCardView = itemView.findViewById(R.id.mCardView);
            txt_section = itemView.findViewById(R.id.txt_section);
            txt_reason = itemView.findViewById(R.id.txt_reason);




        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_interested, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item_Interesting item = listItems.get(position);
        holder.txt_section.setText(item.section);
        holder.txt_reason.setText(item.reason);

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
