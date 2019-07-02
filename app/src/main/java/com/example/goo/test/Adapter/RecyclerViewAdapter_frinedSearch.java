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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goo.test.Fragment.MyInfo.MyInfoFragment_By_Friend;
import com.example.goo.test.Item.ListItem_friendSearch;
import com.example.goo.test.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class RecyclerViewAdapter_frinedSearch extends RecyclerView.Adapter<RecyclerViewAdapter_frinedSearch.MyViewHolder> {
    private List<ListItem_friendSearch> listItems;
    Context context;

    String username;
    String email;

    //안드로이드 현재시간 구하기
    long mNow;
    Date mDate;

    //시간에  대문자 HH를 넣어줘야 24시간 형식으로 출력됨.
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Button btn_follow;
    private Button btn_unfollow;
    private Button btn_me;

    private static final String FOLLOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/following.php";
    private static final String UNFOLLOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/unfollow.php";
    private static final String CHECK_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/checkMeYou.php";

    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextView;
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
            btn_me = itemView.findViewById(R.id.btn_me);
            prof_pic= itemView.findViewById(R.id.prof_pic);


            changeBtnFollowToUnfollow();
            checkMyId();
            //팔로우 버튼 이벤트
            btn_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    //해당 아이템의 유저이름을 정의
                    username = listItems.get(pos).username().toString();

                    if (pos != RecyclerView.NO_POSITION) {
                        System.out.println("포지션은 : " + pos);
                        System.out.println("포지션은 : " + listItems.get(pos).username().toString());
                        follow();
                        //팔로우가 되면 팔로우 버튼이 사라지고 팔로우 취소 버튼으로 바꾼다.
                        // loadLoginEmail();

                    }
                }
            });

            //언팔로우 버튼 이벤트
            btn_unfollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    //해당 아이템의 유저이름을 정의
                    username = listItems.get(pos).username().toString();
                    if (pos != RecyclerView.NO_POSITION) {
                        System.out.println("포지션은 : " + pos);
                        System.out.println("포지션은 : " + listItems.get(pos).username().toString());
                        unfollow();
                        //팔로우가 되면 팔로우 버튼이 사라지고 팔로우 취소 버튼으로 바꾼다.
                        //loadLoginEmail();

                    }
                }
            });


        }
    }

    //어댑터 생성자
    public RecyclerViewAdapter_frinedSearch(Context context, List<ListItem_friendSearch> listItems) {
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ListItem_friendSearch item = listItems.get(position);

        holder.mTextView.setText(item.username());
        if (item.url == null) {
            item.url = "http://13.125.216.157/uploads/555.jpg";
            Picasso.with(context)
                    .load(item.url)
                    .error(R.mipmap.ic_launcher_round)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(holder.prof_pic);
        } else {
            Picasso.with(context)
                    .load(item.url)
                    .error(R.mipmap.ic_launcher_round)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(holder.prof_pic);
        }

        //친구 프로필 사진을 눌렀을 때 해당 친구의 프로필 화면으로 옮김.
    /*    holder.prof_pic.setOnClickListener(new View.OnClickListener() {
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


    //팔로우 할 경우 친구 추가 시키기.
    private void follow() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, FOLLOW_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            Toast.makeText(context, username + "을 팔로우 합니다.", Toast.LENGTH_SHORT).show();
                            btn_follow.setVisibility(View.GONE);
                            btn_unfollow.setVisibility(View.VISIBLE);

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
                params.put("history", getTime());

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    //팔로우 할 경우 친구 추가 시키기.
    private void unfollow() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, UNFOLLOW_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")) {
                            Toast.makeText(context, username + "님 과의 팔로우를 취소합니다.", Toast.LENGTH_SHORT).show();
                            btn_follow.setVisibility(View.VISIBLE);
                            btn_unfollow.setVisibility(View.GONE);

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

    //팔로우한 친구의 경우 검색결과 팔로우 취소되게 만드는 메서드
    private void changeBtnFollowToUnfollow(){



        StringRequest stringRequest = new StringRequest(Request.Method.POST, CHECK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("follow");

                            for (int i = 0; i < jsonarray.length()-1; i++) {
                                JSONObject object = jsonarray.getJSONObject(i);

                                if(object.getString("you").contains(username)){

                                    btn_follow.setVisibility(View.GONE);
                                    btn_unfollow.setVisibility(View.VISIBLE);
                                }


                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }

                }) {
            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("username", username);
                params.put("email", loadLoginEmail());
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    //로그인 아이디와 친구 검색 결과의 아이디가 같으면 '본인'이라고 하고 싶다!!! -> 그렇게 했다~
    public void checkMyId(){


        //리스트에 있는 0번째 값의 유저이름과 이메일 변수... ->나중에 검색어가 서버에 있는 유저 이름 값을 포함하고 있을 경우 전부 출력되도록 수정해야함..
        username = listItems.get(0).username().toString();
        email = listItems.get(0).email().toString();

        //검색결과 지금 로그인해 있는 이메일과 친구의 이메일이 동일할 경우 '본인'으로 버튼을 변경
        if(loadLoginEmail().equals(email)){
            btn_follow.setVisibility(View.GONE);
            btn_unfollow.setVisibility(View.GONE);
            btn_me.setVisibility(View.VISIBLE);
        }
    }

    //쉐어드로 로그인 아이디 가져오기.
    private String loadLoginEmail() {
        SharedPreferences sp = context.getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println(email_from_login);


        return email_from_login;
    }
    //현재시간 구하기
    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

}
