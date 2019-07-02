package com.example.goo.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goo.test.Activity.Chat.ChatProfileActivity;
import com.example.goo.test.Activity.Chat.InvitingChatFriendListActivity;
import com.example.goo.test.Item.Item_Chat_MyProfile;
import com.example.goo.test.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.example.goo.test.Adapter.RecyclerViewAdapter_Chat_Inviting_Friend.index;
import static com.example.goo.test.Adapter.RecyclerViewAdapter_Chat_Inviting_Friend.listItems;


/**
 * Created by Goo on 2018-05-06.
 */

public class RecyclerViewAdapter_Chat_Added_Friend extends RecyclerView.Adapter<RecyclerViewAdapter_Chat_Added_Friend.MyViewHolder> {
    private ArrayList<Item_Chat_MyProfile> listItems_chat;
    Context context;
    Item_Chat_MyProfile item_friend;
    InvitingChatFriendListActivity invite;
    RecyclerViewAdapter_Chat_Inviting_Friend recyclerViewAdapter_chat_inviting_friend;
    private static final String FOLLOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/following.php";
    private static final String UNFOLLOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/unfollow.php";
    private static final String CHECK_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/checkFollowerIfFollowing.php";

    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextView;
        private CircleImageView prof_pic;
        private TextView username;
        private ImageView cancel;


        public MyViewHolder(View itemView) {
            super(itemView);


            itemView.setOnClickListener(new View.OnClickListener() {

                //해당 리스트뷰의 아이템을 누를 경우 이벤트를 발생시킬 수 있다.
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();


                }
            });


            //어떤 xml 요소를 뷰홀더로 잡을지 지정해줌.
            mCardView = itemView.findViewById(R.id.cardview_rec_friend);
            username = itemView.findViewById(R.id.username);
            prof_pic = (CircleImageView) itemView.findViewById(R.id.prof_pic);
            cancel =  itemView.findViewById(R.id.cancel);
          //  item_friend = new Item_Chat_MyProfile();
            invite = new InvitingChatFriendListActivity();
            recyclerViewAdapter_chat_inviting_friend = new RecyclerViewAdapter_Chat_Inviting_Friend(context,listItems);

        }
    }

    //어댑터 생성자
    public RecyclerViewAdapter_Chat_Added_Friend(Context context, ArrayList<Item_Chat_MyProfile> listItems) {
        this.context = context;
        this.listItems_chat = listItems;
    }

    //뷰홀더로 잡고 있는 것을 만들어줌
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_added_friend_horizontal, parent, false);

        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Item_Chat_MyProfile item = listItems_chat.get(position);


        Picasso.with(context).load(item.url).error(R.mipmap.ic_launcher_round).into(holder.prof_pic);
        holder.username.setText(item.username);

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invite.listItem_added_friend.remove(item);
                invite.getAddedFriend();
                System.out.println("내 인덱스 값은 : " + index);
                listItems.get(index).checkbox=false;
                System.out.println(listItems.get(index).checkbox);


                recyclerViewAdapter_chat_inviting_friend.notifyItemChanged(index,listItems);
            }
        });
    }

    //몇개를 출력해서 보여줄지 정해줌.
    @Override
    public int getItemCount() {
        return listItems_chat.size();
    }

    //쉐어드로 로그인 아이디 가져오기.
    private String loadLoginEmail() {
        SharedPreferences sp = context.getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println(email_from_login);

        return email_from_login;
    }

}
