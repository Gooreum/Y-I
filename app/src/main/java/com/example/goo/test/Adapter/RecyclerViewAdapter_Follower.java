package com.example.goo.test.Adapter;

        import android.content.Context;
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
        import com.example.goo.test.Item.ListItem_friendSearch;
        import com.example.goo.test.R;
        import com.example.goo.test.Util.MySingleton;
        import com.squareup.picasso.Picasso;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.Map;

        import de.hdodenhof.circleimageview.CircleImageView;

        import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-06.
 */

public class RecyclerViewAdapter_Follower extends RecyclerView.Adapter<RecyclerViewAdapter_Follower.MyViewHolder> {
    private ArrayList<ListItem_friendSearch> listItems;
    Context context;

    String username;
    String email;

    //로그인 후 메인액티비티로부터 이메일 값을 가져오기 위해 선언.
    String email2;

    //안드로이드 현재시간 구하기
    long mNow;
    Date mDate;

    //시간에  대문자 HH를 넣어줘야 24시간 형식으로 출력됨.
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    private static final String FOLLOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/following.php";
    private static final String UNFOLLOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/unfollow.php";
    private static final String CHECK_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/checkFollowerIfFollowing.php";

    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextView;

       private Button btn_follow;
        private Button btn_unfollow;
       // private Button btn_following;
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
                        System.out.println("포지션은 : " + listItems.get(pos).username().toString());

                    }
                }
            });


            //어떤 xml 요소를 뷰홀더로 잡을지 지정해줌.
            mCardView = itemView.findViewById(R.id.card_container);
            mTextView = itemView.findViewById(R.id.text_holder);
            btn_follow = itemView.findViewById(R.id.btn_follow);
            btn_unfollow = itemView.findViewById(R.id.btn_unfollow);

            prof_pic  = (CircleImageView)itemView.findViewById(R.id.prof_pic);
          //  btn_follow.setVisibility(View.GONE);
          //  btn_unfollow.setVisibility(View.VISIBLE);





        }
    }

    //어댑터 생성자
    public RecyclerViewAdapter_Follower(Context context, ArrayList<ListItem_friendSearch> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    //뷰홀더로 잡고 있는 것을 만들어줌
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_search_vertical, parent, false);

        return new MyViewHolder(itemView);

    }

    //
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ListItem_friendSearch item = listItems.get(position);
        Picasso.with(context).load(item.url).error(R.mipmap.ic_launcher_round).into(holder.prof_pic);
        holder.mTextView.setText(item.username());

        //팔로우버튼 상태 잡아주기.팔로우 상태이면
        if(item.btn_follow == true){
            holder.btn_follow.setVisibility(View.GONE);
            holder.btn_unfollow.setVisibility(View.VISIBLE);
        }else{
            holder.btn_follow.setVisibility(View.VISIBLE);
            holder.btn_unfollow.setVisibility(View.GONE);
        }

             //친구 관계에 따라 팔로우 버튼인지 팔로우 취소 버튼인지 결정
              StringRequest stringRequest2 = new StringRequest(Request.Method.POST, CHECK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response2) {

                        //팔로우 하고 있는 친구인 경우는 팔로우 취소 버튼이 보인다.
                        if (response2.contains("1")) {
                            item.btn_follow = true;
                            holder.btn_follow.setVisibility(View.GONE);
                            holder.btn_unfollow.setVisibility(View.VISIBLE);

                            //그 반대.
                        } else {
                            item.btn_follow = false;
                            holder.btn_follow.setVisibility(View.VISIBLE);
                            holder.btn_unfollow.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email", loadLoginEmail());
                //서버에 팔로워 이름 보내기.
                params.put("username", listItems.get(position).username);

                return params;

            }

        };

        MySingleton.getInstance(context).addToRequestque(stringRequest2);


        //팔로우 버튼을 눌렀을 때
        holder.btn_follow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                item.btn_follow = true;
                holder.btn_follow.setVisibility(View.GONE);
                holder.btn_unfollow.setVisibility(View.VISIBLE);

                //notifyDataSetChanged();
                username = listItems.get(position).username().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, FOLLOW_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("1")) {
                                    Toast.makeText(context, username + "을 팔로우 합니다.", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, "서버 오작동", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                            }


                        }) {
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();
                        //검색결과 찾은 유저의 이름
                        params.put("username", username);
                        //팔로우 버튼을 누른 시간 전송, 이 시간을 기준으로 팔로우 맺은 친구의 알림 활동 기록을 받아 볼 수 있도록 한다.
                        //그렇지 않으면 팔로우 맺기 이전의 모든 해당 친구의 활동 기록을 받아보게 됨...그러고 싶진 않아요..
                        System.out.println("팔로우 맺은 시간은 : " + getTime());

                        params.put("history", getTime());
                        //나의 이메일
                        params.put("email", loadLoginEmail());

                        return params;

                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
//                //해당 아이템의 유저이름을 정의
//                username = listItems.get(position).username().toString();
//
//                if (position != RecyclerView.NO_POSITION) {
//                    System.out.println("포지션은 : " + position);
//                    System.out.println("포지션은 : " + listItems.get(position).username().toString());
//                    follow(position);
//                    notifyDataSetChanged();
//                    //팔로우가 되면 팔로우 버튼이 사라지고 팔로우 취소 버튼으로 바꾼다.
//                    // loadLoginEmail();
//
//                }
            }
        });

        //언팔로우 버튼 이벤트
        holder.btn_unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int pos = getAdapterPosition();
                //해당 아이템의 유저이름을 정의
                username = listItems.get(position).username().toString();
                item.btn_follow = false;
                holder.btn_follow.setVisibility(View.VISIBLE);
                holder.btn_unfollow.setVisibility(View.GONE);
               // notifyDataSetChanged();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, UNFOLLOW_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("1")) {
                                    Toast.makeText(context, username + "님 과의 팔로우를 취소합니다.", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, "서버 오작동", Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                            }


                        }) {
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();
                        //검색결과 찾은 유저의 이름
                        params.put("username", username);


                        //나의 이메일
                        params.put("email", loadLoginEmail());

                        return params;

                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }
        });



    }

    //몇개를 출력해서 보여줄지 정해줌.
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    //현재시간 구하기
    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }


    //쉐어드로 로그인 아이디 가져오기.
    private String loadLoginEmail() {
        SharedPreferences sp = context.getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println(email_from_login);


        return email_from_login;
    }

}
