package com.example.goo.test.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goo.test.Item.ListItem_Reply;
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
 * Created by Goo on 2018-05-19. 댓글 불러오는 어댑터
 */

public class RecyclerViewAdapter_Show_Reply extends RecyclerView.Adapter<RecyclerViewAdapter_Show_Reply.MyViewHolder> {
    private static final String DELETE_REPLY = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/delete_reply.php";
    private ArrayList<ListItem_Reply> listItems;
    Context context;


    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextView;
        private TextView textView,txt_message,txt_history,txt_reply;
        private TextView txtOptionDigit;
        private TextView txtOptionDigit_not_myproject;
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
            textView = itemView.findViewById(R.id.textView);
            txt_message = itemView.findViewById(R.id.txt_message);
            txt_history = itemView.findViewById(R.id.txt_history);
            txt_reply = itemView.findViewById(R.id.txt_reply);
            prof_pic  = (CircleImageView)itemView.findViewById(R.id.reply_img);
            txtOptionDigit = (TextView) itemView.findViewById(R.id.txtOptionDigit);
            txtOptionDigit_not_myproject = (TextView) itemView.findViewById(R.id.txtOptionDigit_not_myproject);

        }
    }

    //어댑터 생성자
    public RecyclerViewAdapter_Show_Reply(Context context, ArrayList<ListItem_Reply> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    //뷰홀더로 잡고 있는 것을 만들어줌
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_reply_home, parent, false);

        return new MyViewHolder(itemView);

    }

    //
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ListItem_Reply item = listItems.get(position);
        Picasso.with(context).load(item.url).error(R.mipmap.ic_launcher_round).into(holder.prof_pic);
        holder.txt_message.setText(item.message);
        holder.textView.setText(item.username);
        holder.txt_history.setText(item.history);


        if (item.email.equals(loadLoginEmail())) {
            //내가 작성한 글일 경우 모집확인 버튼으로 변경
            //holder.btn_join.setVisibility(View.GONE);
            // holder.btn_confirm_project.setVisibility(View.VISIBLE);
//            holder.txtOptionDigit_not_myproject.setVisibility(View.GONE);
//            holder.txtOptionDigit.setVisibility(View.VISIBLE );
            holder.txtOptionDigit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.txtOptionDigit);
                    popupMenu.inflate(R.menu.option_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu_item_motify:

                                    break;

                                case R.id.menu_item_delete:
                                    //게시물 삭제
                                    final int id = listItems.get(position).id;
                                    final String ID = Integer.toString(id);
                                    System.out.println("삭제된 포지션 : " + id);
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_REPLY,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                    // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                                                    if (response.contains("1")) {
                                                        Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show();


                                                    } else {
                                                        Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
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


                                            params.put("id", ID);
                                            params.put("project_id", String.valueOf(item.project_id));

                                            return params;

                                        }
                                    };

//                                    RequestQueue requestQueue = Volley.newRequestQueue(context);
//                                    requestQueue.add(stringRequest);
                                    MySingleton.getInstance(context).addToRequestque(stringRequest);

                                    listItems.remove(position);

                                    notifyDataSetChanged();
                                    break;

                                case R.id.menu_item_share:

                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });

                    popupMenu.show();
                }
            });
            //내가 작성한 글이 아닌 경우에는 옵션 메뉴에 공유 기능만 가능하다.
        } else {


            holder.txtOptionDigit_not_myproject.setVisibility(View.VISIBLE);
            holder.txtOptionDigit.setVisibility(View.GONE);
            holder.txtOptionDigit_not_myproject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.txtOptionDigit_not_myproject);
                    popupMenu.inflate(R.menu.optiom_menu_not_myproject);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {

                                case R.id.menu_item_share:

                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });

                    popupMenu.show();
                }
            });
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
