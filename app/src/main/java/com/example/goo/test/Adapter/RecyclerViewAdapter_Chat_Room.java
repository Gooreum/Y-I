package com.example.goo.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goo.test.Activity.Chat.ChatProfileActivity;
import com.example.goo.test.Activity.Chat.ChatRoomActivity;
import com.example.goo.test.Item.Item_Chat_MyProfile;
import com.example.goo.test.Item.Item_Chat_Room_List;
import com.example.goo.test.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-06.
 */

public class RecyclerViewAdapter_Chat_Room extends RecyclerView.Adapter<RecyclerViewAdapter_Chat_Room.MyViewHolder> {
    private ArrayList<Item_Chat_Room_List> listItems;
    Context context;

    private static final String UPDATE_CHAT_STATE = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/update_chat_in_state.php";
    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextView;
        private CircleImageView prof_pic;
        private TextView username, message,history,member_count,project_state;

        public MyViewHolder(View itemView) {
            super(itemView);


            itemView.setOnClickListener(new View.OnClickListener() {

                //해당 리스트뷰의 아이템을 누를 경우 이벤트를 발생시킬 수 있다.
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, ChatRoomActivity.class);
                        //유저이름
                        if(loadLoginEmail().equals(listItems.get(pos).email)){
                            intent.putExtra("username" , listItems.get(pos).username.toString());
                            intent.putExtra("room_num" , listItems.get(pos).room_num);
                            intent.putExtra("url" , listItems.get(pos).url);
                            intent.putExtra("project_id" , listItems.get(pos).project_id);
                            intent.putExtra("boss" , listItems.get(pos).boss);

                            //방에 접속시 채팅 상태를 '접속'상태로 변경해준다.
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_CHAT_STATE,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {


                                            //사진의 갯수가 n개 이고 서버로부터 받아오는 response 값이 n과 같은 경우일 때 메인화면으로 화면 전환이 이루어진다.
                                            //이렇게 처리하지 않으면 사진을 서버로 옮길 때 마다 화면 전환이 이루어짐.
                                            if (response.contains("1")) {
                                                //내가 보낸 메세지는 서버를 거치지 않고 바로 받아 볼 수 있도록 한다.

                                            }else{
                                                Toast.makeText(context,"failed",Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(context, "Error while uploading image", Toast.LENGTH_LONG).show();
                                        }


                                    }) {
                                @Override
                                protected Map<String, String> getParams() {

                                    Map<String, String> params = new HashMap<String, String>();
                                   // params.put("connect_state", String.valueOf(connect_state));
                                    params.put("room_num", String.valueOf(listItems.get(pos).room_num));
                                    params.put("email_in", loadLoginEmail());


                                    return params;

                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);
                        }else{
                            intent.putExtra("username" , listItems.get(pos).username.toString());
                            intent.putExtra("room_num" , listItems.get(pos).room_num);
                            intent.putExtra("url" , listItems.get(pos).url);
                            intent.putExtra("project_id" , listItems.get(pos).project_id);
                            intent.putExtra("boss" , listItems.get(pos).boss);
                            //방에 접속시 채팅 상태를 '접속'상태로 변경해준다.
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_CHAT_STATE,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {


                                            //사진의 갯수가 n개 이고 서버로부터 받아오는 response 값이 n과 같은 경우일 때 메인화면으로 화면 전환이 이루어진다.
                                            //이렇게 처리하지 않으면 사진을 서버로 옮길 때 마다 화면 전환이 이루어짐.
                                            if (response.contains("1")) {
                                                //내가 보낸 메세지는 서버를 거치지 않고 바로 받아 볼 수 있도록 한다.


                                            }else{
                                                Toast.makeText(context,"failed",Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(context, "Error while uploading image", Toast.LENGTH_LONG).show();
                                        }


                                    }) {
                                @Override
                                protected Map<String, String> getParams() {

                                    Map<String, String> params = new HashMap<String, String>();
                                    // params.put("connect_state", String.valueOf(connect_state));
                                    params.put("room_num", String.valueOf(listItems.get(pos).room_num));
                                    params.put("email_in", loadLoginEmail());


                                    return params;

                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);
                        }

                        //프로필 이미지 url

                       // intent.putExtra("email" , listItems.get(pos).email.toString());
                       // intent.putExtra("email2" , listItems.get(pos).email2.toString());
                        context.startActivity(intent);
                        System.out.println("포지션은 : " + pos);
                        System.out.println("유저네임은 : " + listItems.get(pos).username.toString());
                        System.out.println("유저네임은 : " + listItems.get(pos).username.toString());
                        System.out.println("이메일은 : " + listItems.get(pos).url.toString());
                        System.out.println("이메일은 : " + listItems.get(pos).room_num);

                    }
                }
            });


            //어떤 xml 요소를 뷰홀더로 잡을지 지정해줌.
            mCardView = itemView.findViewById(R.id.mCardView);
            username = itemView.findViewById(R.id.username);
            message = itemView.findViewById(R.id.message);
            history = itemView.findViewById(R.id.history);
            member_count = itemView.findViewById(R.id.member_count);
            project_state = itemView.findViewById(R.id.project_state);
            //prof_pic = (CircleImageView) itemView.findViewById(R.id.prof_pic);


        }
    }

    //어댑터 생성자
    public RecyclerViewAdapter_Chat_Room(Context context, ArrayList<Item_Chat_Room_List> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    //뷰홀더로 잡고 있는 것을 만들어줌
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_chat_room_item, parent, false);

        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Item_Chat_Room_List item = listItems.get(position);
      //  Picasso.with(context).load(item.url).error(R.mipmap.ic_launcher_round).into(holder.prof_pic);

        //프로젝트 방이면 방목록에 '프로젝트' 태그를 붙여준다.
        if(item.project_state == 1){
            holder.project_state.setVisibility(View.VISIBLE);
        }else{
            holder.project_state.setVisibility(View.GONE);
        }


        holder.history.setText(item.history);
        holder.history.setVisibility(View.GONE);
        holder.member_count.setText(item.member_count+"");

        if(item.room_name.equals("null") ){

            holder.username.setText(item.users);
        }else{
            holder.username.setText(item.room_name);
        }
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
