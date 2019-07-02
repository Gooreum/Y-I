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
import android.widget.Button;
import android.widget.TextView;

import com.example.goo.test.Fragment.MyInfo.MyInfoFragment_By_Friend;
import com.example.goo.test.Item.ListItem_Check_Join_Member;
import com.example.goo.test.Item.ListItem_Check_Like_Member;
import com.example.goo.test.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-11.
 */

public class RecyclerViewAdapter_Show_Like_Member extends RecyclerView.Adapter<RecyclerViewAdapter_Show_Like_Member.MyViewHolder> {
    private List<ListItem_Check_Like_Member> listItems;
    Context context;

    private static final String FOLLOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/following.php";
    private static final String UNFOLLOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/unfollow.php";
    private static final String CHECK_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/checkMeYou.php";

    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextView;
        private Button btn_follow;
        private Button btn_unfollow;
        private Button btn_me;
        private TextView txt_username;

        private CircleImageView prof_pic;

        public MyViewHolder(View itemView) {
            super(itemView);


            itemView.setOnClickListener(new View.OnClickListener() {

                //해당 리스트뷰의 아이템을 누를 경우 이벤트를 발생시킬 수 있다.
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        System.out.println("포지션은 : " + pos);
                       // System.out.println("포지션은 : " + listItems.get(pos).username().toString());

                    }
                }
            });


            //어떤 xml 요소를 뷰홀더로 잡을지 지정해줌.
            mCardView = itemView.findViewById(R.id.card_container);
           // mTextView = itemView.findViewById(R.id.text_holder);
//            btn_follow = itemView.findViewById(R.id.btn_follow);
//            btn_unfollow = itemView.findViewById(R.id.btn_unfollow);
//            btn_me = itemView.findViewById(R.id.btn_me);
//            btn_follow.setVisibility(View.GONE);
//            btn_unfollow.setVisibility(View.VISIBLE);
            txt_username = itemView.findViewById(R.id.txt_username);

            prof_pic = itemView.findViewById(R.id.prof_pic);

        }
    }

    //어댑터 생성자
    public RecyclerViewAdapter_Show_Like_Member(Context context, List<ListItem_Check_Like_Member> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    //뷰홀더로 잡고 있는 것을 만들어줌
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_check_like_member, parent, false);

        return new MyViewHolder(itemView);

    }

    //
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ListItem_Check_Like_Member item = listItems.get(position);

        holder.txt_username.setText(item.getUsername());
        //holder.mTextView.setText(item.username());
        Picasso.with(context)
                .load(item.url)
                .error(R.mipmap.ic_launcher_round)
                .into(holder.prof_pic);

        //친구 프로필 사진을 눌렀을 때 해당 친구의 프로필 화면으로 옮김.
       /* holder.prof_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                System.out.println("sjldfkjsladkfjloskejdflkjweikfjwoeijf  " + item.email);
                //친구의 이메일 값을 쉐어드로 저장해둠.
                SharedPreferences ID = context.getApplicationContext().getSharedPreferences("friend_email",  MODE_PRIVATE);
                SharedPreferences.Editor editor = ID.edit();
                editor.putString("friend_email", item.email);
                editor.commit();

                MyInfoFragment_By_Friend myinfo = new MyInfoFragment_By_Friend();
                myinfo.setArguments(bundle);

                ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, myinfo).addToBackStack(null)
                        .commit();

            }
        });*/

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
