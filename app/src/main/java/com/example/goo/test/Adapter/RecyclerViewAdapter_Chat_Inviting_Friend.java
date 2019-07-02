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
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.goo.test.Activity.Chat.ChatProfileActivity;
import com.example.goo.test.Activity.Chat.InvitingChatFriendListActivity;
import com.example.goo.test.Item.Item_Chat_MyProfile;
import com.example.goo.test.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.example.goo.test.Activity.Chat.InvitingChatFriendListActivity.btn_invite;
import static com.example.goo.test.Activity.Chat.InvitingChatFriendListActivity.listItem_added_friend;


/**
 * Created by Goo on 2018-05-06.
 */

public class RecyclerViewAdapter_Chat_Inviting_Friend extends RecyclerView.Adapter<RecyclerViewAdapter_Chat_Inviting_Friend.MyViewHolder> {
    static  ArrayList<Item_Chat_MyProfile> listItems;
    Context context;
    //친구초대 액티비티에 있는 추가된 친구를 담을 arraylist 변수를 가져오기 위해 InvitingChatFriendListActivity 클래스를 가지고옴.
    InvitingChatFriendListActivity invite ;
    static int index;
   // public Button checkbox,checkbox2;
   public  RecyclerViewAdapter_Chat_Inviting_Friend(){

    }
    private static final String FOLLOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/following.php";
    private static final String UNFOLLOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/unfollow.php";
    private static final String CHECK_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/checkFollowerIfFollowing.php";

    //뷰홀더에 대한 클래스를 만든다.
    public  class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextView;
        private CircleImageView prof_pic;
        private TextView username;
        public Button checkbox,checkbox2;


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
            prof_pic = (CircleImageView) itemView.findViewById(R.id.prof_pic);
            checkbox =  itemView.findViewById(R.id.checkbox);
            checkbox2 =  itemView.findViewById(R.id.checkbox2);
            invite = new InvitingChatFriendListActivity();

        }



    }

    //어댑터 생성자
    public RecyclerViewAdapter_Chat_Inviting_Friend(Context context, ArrayList<Item_Chat_MyProfile> listItems) {
        this.context = context;
        this.listItems = listItems;

    }

    //뷰홀더로 잡고 있는 것을 만들어줌
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_chat_inviting_frined_list, parent, false);

        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Item_Chat_MyProfile item = listItems.get(position);
        Item_Chat_MyProfile item_friend = new Item_Chat_MyProfile();

        Picasso.with(context).load(item.url).error(R.mipmap.ic_launcher_round).into(holder.prof_pic);
        holder.username.setText(item.username);

        //체크박스 처리
        if(item.checkbox == true){
            holder.checkbox.setVisibility(View.GONE );
            holder.checkbox2.setVisibility(View.VISIBLE);
        }else{
            holder.checkbox.setVisibility(View.VISIBLE );
            holder.checkbox2.setVisibility(View.GONE);
        }

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                item.checkbox = true;
                holder.checkbox2.setVisibility(View.VISIBLE);
                holder.checkbox.setVisibility(View.GONE);

                item_friend.username = item.username;
                item_friend.email = item.email;
                item_friend.url = item.url;
                index = position;
                listItem_added_friend.add(item_friend);

                invite.getAddedFriend();

                    btn_invite.setVisibility(View.VISIBLE);

               // invite.btn_invite.setOnClickListener(this);
            }
        });
        holder.checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.checkbox = false;
                holder.checkbox2.setVisibility(View.GONE);
                holder.checkbox.setVisibility(View.VISIBLE);

                item.checkbox = false;
                listItem_added_friend.remove(item_friend);

                invite.getAddedFriend();

                if(listItem_added_friend.size() == 0) {
                    btn_invite.setVisibility(View.GONE);
                }
                //invite.btn_invite.setOnClickListener(this);
            }
        });


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
