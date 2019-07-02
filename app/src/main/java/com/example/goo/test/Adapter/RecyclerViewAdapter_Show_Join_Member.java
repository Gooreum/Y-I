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

public class RecyclerViewAdapter_Show_Join_Member extends RecyclerView.Adapter<RecyclerViewAdapter_Show_Join_Member.MyViewHolder> {
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
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
            btn_together = itemView.findViewById(R.id.btn_together);
            txt_username = itemView.findViewById(R.id.txt_username);
            prof_pic = itemView.findViewById(R.id.prof_pic);
            btn_relative = itemView.findViewById(R.id.btn_relative);
            //   checkbox = itemView.findViewById(R.id.checkbox);


        }
    }

    //어댑터 생성자
    public RecyclerViewAdapter_Show_Join_Member(Context context, List<ListItem_Check_Join_Member> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    //뷰홀더로 잡고 있는 것을 만들어줌
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_check_join_member, parent, false);

        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ListItem_Check_Join_Member item = listItems.get(position);

        txt_selected_num.setText(selected_num+ " ");
        //프로젝트 작성자만 '함께하기', '취소하기'버튼 보이도록 하기
        //together가 true이면 '함께하기취소'버튼이, false이면 '함께하기'버튼이 나오도록 한다.
        if(project_email.equals(loadLoginEmail())) {
            if (item.together == true) {
                holder.btn_cancel.setVisibility(View.VISIBLE);
                holder.btn_together.setVisibility(View.GONE);
            } else {
                holder.btn_cancel.setVisibility(View.GONE);
                holder.btn_together.setVisibility(View.VISIBLE);
            }
        }else{
            holder.btn_together.setVisibility(View.GONE);
            holder.btn_cancel.setVisibility(View.GONE);
        }


        //모집인원과 선택된 인원이 같을 경우 '프로젝트 시작'버튼을 보이게 한다.
        if(join_count <= selected_num && project_email.equals(loadLoginEmail())){
            btn_start.setVisibility(View.VISIBLE);
        }else if(join_count > selected_num){
            btn_start.setVisibility(View.GONE);
        }
        //내 모집글에서 '함께하기' 버튼을 눌렀을 때!
        holder.btn_together.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btn_cancel.setVisibility(View.VISIBLE);
                holder.btn_together.setVisibility(View.GONE);
                item.together = true;
                System.out.println(item.username);
                selected_num = selected_num + 1;
                txt_selected_num.setText(selected_num + " ");


                StringRequest stringRequest = new StringRequest(Request.Method.POST, JOIN_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("1")) {

                                    //좋아요 취소를 누르면 좋아요 갯수가 하나씩 줄어듦.
                                    Toast.makeText(context, "1", Toast.LENGTH_LONG).show();

                                    notifyDataSetChanged();


                                } else {
                                     Toast.makeText(context, "2", Toast.LENGTH_LONG).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    //해쉬맵을 통해 php에 이메일 값을 보내줌.
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();
                       //프로젝트 아이디, 신청한 사람의 유저네임이나 이메일 값.
                        System.out.println("alksdjflaksjdlasjdlasjdlkasjd" + project_id);
                        System.out.println("alksdjflaksjdlasjdlasjdlkasjd" +  item.getEmail());
                        params.put("project_id", String.valueOf(project_id));
                        params.put("join_email", item.getEmail());
                        return params;

                    }
                };

                MySingleton.getInstance(context).addToRequestque(stringRequest);


            }
        });
        //내 모집글에서 '함께하기 취소' 버튼을 눌렀을 때!
        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btn_cancel.setVisibility(View.GONE);
                holder.btn_together.setVisibility(View.VISIBLE);
                item.together = false;
                System.out.println("취소버튼을 눌렀더니 나오는 유저이름: "+ item.username);
                selected_num = selected_num -1;
                txt_selected_num.setText(selected_num  + " ");

                StringRequest stringRequest = new StringRequest(Request.Method.POST, JOIN_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("1")) {

                                    //좋아요 취소를 누르면 좋아요 갯수가 하나씩 줄어듦.
                                    Toast.makeText(context, "1", Toast.LENGTH_LONG).show();

                                    notifyDataSetChanged();


                                } else {
                                    Toast.makeText(context, "2", Toast.LENGTH_LONG).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    //해쉬맵을 통해 php에 이메일 값을 보내줌.
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();
                        //프로젝트 아이디, 신청한 사람의 유저네임이나 이메일 값.
                        System.out.println("alksdjflaksjdlasjdlasjdlkasjd" + project_id);
                        System.out.println("alksdjflaksjdlasjdlasjdlkasjd" +  item.getEmail());
                        params.put("project_id_cancel", String.valueOf(project_id));
                        params.put("join_email_cancel", item.getEmail());
                        return params;

                    }
                };

                MySingleton.getInstance(context).addToRequestque(stringRequest);

            }
        });


        holder.txt_username.setText(item.getUsername());
        //해당 모집글의 유저와 신청확인을 하는 사람의 이메일 값이 같은 경우에만 체크박스를 띄운다.
        if (project_email.equals(loadLoginEmail())) {
            holder.btn_relative.setVisibility(View.VISIBLE);

        }

        //피카소로 신청인원의 프로필 사진 가져오기
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
        //System.out.println(email_from_login);


        return email_from_login;
    }

}
