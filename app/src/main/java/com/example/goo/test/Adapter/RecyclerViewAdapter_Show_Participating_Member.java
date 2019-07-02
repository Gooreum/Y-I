package com.example.goo.test.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.goo.test.Item.ListItem_Check_Join_Member;
import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.example.goo.test.Activity.Home.Check_Join_Member.btn_start;
import static com.example.goo.test.Activity.Home.Check_Join_Member.join_count;
import static com.example.goo.test.Activity.Home.Check_Join_Member.project_email;
import static com.example.goo.test.Activity.Home.Check_Join_Member.project_id;
import static com.example.goo.test.Activity.Home.Check_Join_Member.selected_count;
import static com.example.goo.test.Activity.Home.Check_Join_Member.txt_selected_num;

/**
 * Created by Goo on 2018-05-11.
 */

public class RecyclerViewAdapter_Show_Participating_Member extends RecyclerView.Adapter<RecyclerViewAdapter_Show_Participating_Member.MyViewHolder> {
    private List<ListItem_Check_Join_Member> listItems;
    Context context;

    int selected_num = selected_count;

    private static final String JOIN_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/selected_join_member.php";
    private static final String UNFOLLOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/unfollow.php";
    private static final String CHECK_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/checkMeYou.php";

    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextView;
        private RelativeLayout btn_relative;
        private TextView txt_username;
        private CheckBox checkbox;
        private CircleImageView prof_pic;
        private Button btn_cancel, btn_together;

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

            txt_username = itemView.findViewById(R.id.txt_username);
            prof_pic = itemView.findViewById(R.id.prof_pic);


        }
    }

    //어댑터 생성자
    public RecyclerViewAdapter_Show_Participating_Member(Context context, List<ListItem_Check_Join_Member> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    //뷰홀더로 잡고 있는 것을 만들어줌
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_check_participating_member, parent, false);

        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ListItem_Check_Join_Member item = listItems.get(position);

        holder.txt_username.setText(item.username);
        Picasso.with(context).load(item.url).error(R.mipmap.ic_launcher_round).into(holder.prof_pic);
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
        //System.out.println(email_from_login);


        return email_from_login;
    }

}
