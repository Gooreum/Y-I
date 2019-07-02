package com.example.goo.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goo.test.Activity.Chat.ChatProfileActivity;
import com.example.goo.test.Item.Item_Chat_MyProfile;
import com.example.goo.test.Item.ListItem_friendSearch;
import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-06.
 */

public class RecyclerViewAdapter_Chat_Friend extends RecyclerView.Adapter<RecyclerViewAdapter_Chat_Friend.MyViewHolder> {
    private ArrayList<Item_Chat_MyProfile> listItems;
    Context context;


    private static final String FOLLOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/following.php";
    private static final String UNFOLLOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/unfollow.php";
    private static final String CHECK_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/checkFollowerIfFollowing.php";

    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextView;
        private CircleImageView prof_pic;
        private TextView username, state_message;

        public MyViewHolder(View itemView) {
            super(itemView);


            itemView.setOnClickListener(new View.OnClickListener() {

                //해당 리스트뷰의 아이템을 누를 경우 이벤트를 발생시킬 수 있다.
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, ChatProfileActivity.class);
                        //유저이름
                        intent.putExtra("username" , listItems.get(pos).username.toString());
                        //프로필 이미지 url
                        intent.putExtra("url" , listItems.get(pos).url.toString());
                        intent.putExtra("email" , listItems.get(pos).email.toString());

                        context.startActivity(intent);
                        System.out.println("포지션은 : " + pos);
                        System.out.println("포지션은 : " + listItems.get(pos).username.toString());
                        System.out.println("포지션은 : " + listItems.get(pos).email.toString());

                    }
                }
            });


            //어떤 xml 요소를 뷰홀더로 잡을지 지정해줌.
            mCardView = itemView.findViewById(R.id.mCardView);
            username = itemView.findViewById(R.id.username);
            state_message = itemView.findViewById(R.id.state_message);
            prof_pic = (CircleImageView) itemView.findViewById(R.id.prof_pic);


        }
    }

    //어댑터 생성자
    public RecyclerViewAdapter_Chat_Friend(Context context, ArrayList<Item_Chat_MyProfile> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    //뷰홀더로 잡고 있는 것을 만들어줌
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_chat_frined_list, parent, false);

        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Item_Chat_MyProfile item = listItems.get(position);
        Picasso.with(context).load(item.url).error(R.mipmap.ic_launcher_round).into(holder.prof_pic);
        holder.username.setText(item.username);
        holder.state_message.setText(item.state_message);
    }

    //몇개를 출력해서 보여줄지 정해줌.
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    //쉐어드로 로그인 아이디 가져오기.
    private String loadLoginEmail() {
        SharedPreferences sp = context.getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println(email_from_login);

        return email_from_login;
    }

}
