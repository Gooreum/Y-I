package com.example.goo.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.goo.test.Activity.Home.Check_Join_Member;
import com.example.goo.test.Activity.Home.Check_Like_Member;
import com.example.goo.test.Item.ListItem_Show_Project;
import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-07.
 */

public class RecyclerViewAdapter_Show_Project_in_MyInfo extends RecyclerView.Adapter<RecyclerViewAdapter_Show_Project_in_MyInfo.MyViewHolder> {

    private List<ListItem_Show_Project> listItems;
    Context context;
    RequestQueue requestQueue2;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    StringRequest stringRequest2;

    private static final String LIKE_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/project_like.php";
    private static final String DELETE_PROJECT = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/delete_project_content.php";
    private static final String LIKE_STATE = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/checkLikeState.php";



    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView mCardView;
        private TextView text_username, dev_duration, apply_duration, member_num,gps;
        private TextView text_email;
        private TextView text_title;
        //private TextView text_content;
        private ReadMoreTextView text_content;
        private TextView text_history;
        private TextView txtOptionDigit;
        private TextView txtOptionDigit_not_myproject;
        private LinearLayout line_join;
        // private Button btn_join, ;
        private ImageView btn_like, btn_unlike, btn_reply, btn_join, btn_unjoin, btn_confirm_project, btn_project_completed;

        private CircleImageView prof_pic, join_member_pic;
        private TextView txt_like, txt_join, txt_reply;
        private LinearLayout line_texts;
        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {

                //해당 리스트뷰의 아이템을 누를 경우 이벤트를 발생시킬 수 있다.
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        System.out.println("포지션은 : " + pos + " " + listItems.get(pos).username.toString());
                        // System.out.println("포지션은 : " + listItems.get(pos).username().toString());
                        System.out.println("포지션은 : " + pos + " " + listItems.get(pos).id);
                    }
                }
            });


            //어떤 xml 요소를 뷰홀더로 잡을지 지정해줌.
            mCardView = (CardView) itemView.findViewById(R.id.card_container);
            text_username = (TextView) itemView.findViewById(R.id.text_username);
          //  text_email = (TextView) itemView.findViewById(R.id.text_email);
            text_title = (TextView) itemView.findViewById(R.id.text_title);
            //text_content = (TextView) itemView.findViewById(R.id.text_content);
            text_content = (ReadMoreTextView) itemView.findViewById(R.id.text_content);
            text_history = (TextView) itemView.findViewById(R.id.text_history);
            txtOptionDigit = (TextView) itemView.findViewById(R.id.txtOptionDigit);
            txtOptionDigit_not_myproject = (TextView) itemView.findViewById(R.id.txtOptionDigit_not_myproject);
            btn_like = (ImageView) itemView.findViewById(R.id.btn_like);
            btn_unlike = (ImageView) itemView.findViewById(R.id.btn_unlike);
            btn_reply = (ImageView) itemView.findViewById(R.id.btn_reply);
           // btn_join = (ImageView) itemView.findViewById(R.id.btn_join);
           // btn_unjoin = (ImageView) itemView.findViewById(R.id.btn_unjoin);
            btn_confirm_project = itemView.findViewById(R.id.btn_confirm_project);
          //  btn_project_completed = itemView.findViewById(R.id.btn_project_completed);
            line_texts = (LinearLayout) itemView.findViewById(R.id.line_texts);
            txt_join = (TextView) itemView.findViewById(R.id.txt_join);
            txt_reply = (TextView) itemView.findViewById(R.id.txt_reply);
            txt_like = (TextView) itemView.findViewById(R.id.txt_like);
            line_join = (LinearLayout) itemView.findViewById(R.id.line_join);
            prof_pic = (CircleImageView) itemView.findViewById(R.id.prof_pic);
            join_member_pic = (CircleImageView) itemView.findViewById(R.id.join_member_pic);
            dev_duration = itemView.findViewById(R.id.dev_duration);
            apply_duration = itemView.findViewById(R.id.apply_duration);
            member_num = itemView.findViewById(R.id.member_num);
            gps = itemView.findViewById(R.id.gps);

        }
    }

    //어댑터 생성자
    public RecyclerViewAdapter_Show_Project_in_MyInfo(Context context, List<ListItem_Show_Project> listItems) {
        this.context = context;
        this.listItems = listItems;

    }

    //뷰홀더로 잡고 있는 것을 만들어줌
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_myinfo, parent, false);

        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ListItem_Show_Project item = listItems.get(position);

        if(item.gps.equals("null")){
            holder.gps.setText("대한민국, 서울");
        }else{
            holder.gps.setText(item.gps);
        }


        holder.text_username.setText(item.getUsername());

        holder.text_title.setText(item.getTitle());
        holder.text_content.setText(item.getContent());
        holder.text_history.setText(item.getHistory());
        holder.apply_duration.setText(item.apply_duration);
        holder.dev_duration.setText(item.dev_duration);
        holder.member_num.setText(item.member_num + "");
        Picasso.with(context)
                .load(item.url)
                .error(R.mipmap.ic_launcher_round)
                .into(holder.prof_pic);


        //like=false 이면 좋아요버튼이 보이게, like=true이면 좋아요 취소 버튼이 보이게.
        if (item.getLike() == true) {
            holder.btn_like.setVisibility(View.GONE);
            holder.btn_unlike.setVisibility(View.VISIBLE);
        } else {
            holder.btn_like.setVisibility(View.VISIBLE);
            holder.btn_unlike.setVisibility(View.GONE);

        }


        if (item.getLike_count() != 0 || item.getJoin_count() != 0) {
            item.counting = true;
            holder.line_texts.setVisibility(View.VISIBLE);
            holder.txt_like.setText("추천 " + item.getLike_count() );
            holder.txt_join.setText("신청인원 " + item.getJoin_count() + " 명");
            //신청인원수가 아무도 없을 때 띄우는 문구.
            if(item.getJoin_count() == 0){
                holder.txt_join.setText(item.getUsername() + "님의 첫 프로젝트 신청자가 되어 보세요. ");
            }else if(item.getLike_count() == 0){
                holder.txt_like.setText("추천 " );
            }else if(item.getLike_count() == 0 && item.getJoin_count() == 0){
                item.counting = false;
                holder.line_texts.setVisibility(View.GONE);
            }
        }

        //좋아요,신청하기가 하나도 없는 경우와 하나라도 있을 경우 line_texts의 visibility 설정값 정해주기.그래야 리사이클러뷰가 꼬이지 않는다.
        if(item.counting == true){
            holder.line_texts.setVisibility(View.VISIBLE);
        }else{
            holder.line_texts.setVisibility(View.GONE);
        }
        //좋아요 버튼
        holder.btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btn_like.setVisibility(View.GONE);
                holder.btn_unlike.setVisibility(View.VISIBLE);
                item.like = true;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, LIKE_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                            if(response.contains("1")){

                                //좋아요를 누르면 좋아요 갯수가 +1 증가.
                                    item.like_count = item.like_count+1;
                                    holder.txt_like.setText("추천 " + item.getLike_count() );
                                    holder.txt_join.setText("신청인원 " + item.getJoin_count() + " 명");
                                    holder.line_texts.setVisibility(View.VISIBLE);
                                    notifyDataSetChanged();

                            }else{
                               // Toast.makeText(context, "2", Toast.LENGTH_LONG).show();
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

                        params.put("id", String.valueOf(listItems.get(position).getId()));
                        params.put("email", loadLoginEmail());

                        return params;

                    }
                };

                MySingleton.getInstance(context).addToRequestque(stringRequest);


                //notifyDataSetChanged();


            }
        });

        //좋아요 취소버튼
        holder.btn_unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btn_like.setVisibility(View.GONE);
                holder.btn_unlike.setVisibility(View.VISIBLE);
                item.like = false;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, LIKE_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.contains("1")){

                                    //좋아요 취소를 누르면 좋아요 갯수가 하나씩 줄어듦.
                                    //Toast.makeText(context, "1", Toast.LENGTH_LONG).show();
                                    item.like_count = item.like_count-1;
                                        holder.txt_like.setText("추천 " + item.getLike_count());
                                        holder.txt_join.setText("신청인원 " + item.getJoin_count() + " 명");
                                        holder.line_texts.setVisibility(View.VISIBLE);
                                    notifyDataSetChanged();
                                        //좋아요 갯수가 0개인 경우 좋아요 갯수 공간이 사라짐
                                    if(item.like_count == 0 && item.join_count == 0){
                                        holder.line_texts.setVisibility(View.GONE);
                                        notifyDataSetChanged();
                                    }

                                }else{
                                    //Toast.makeText(context, "2", Toast.LENGTH_LONG).show();
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

                        params.put("id_unlike", String.valueOf(listItems.get(position).getId()));
                        params.put("email", loadLoginEmail());

                        return params;

                    }
                };

                MySingleton.getInstance(context).addToRequestque(stringRequest);


                //notifyDataSetChanged();


            }
        });

        //신청인원을 선택하면 누가 신청했는지에 대한 명단을 띄운다.
        if(listItems.get(position).join_count != 0) {
            holder.txt_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    Intent intent = new Intent(context, Check_Join_Member.class);
                    intent.putExtra("project_id",listItems.get(position).getId());
                    intent.putExtra("project_email",listItems.get(position).getEmail());
                    intent.putExtra("username",listItems.get(position).getUsername());
                    intent.putExtra("selected_num",listItems.get(position).selected_num);
                    intent.putExtra("join_count",listItems.get(position).getJoin_count());
                    context.startActivity(intent);

                 /*   Bundle bundle = new Bundle();
                    bundle.putInt("project_id", listItems.get(position).getId()); // Put anything what you want
                    bundle.putString("email", listItems.get(position).getEmail()); // Put anything what you want
                    bundle.putString("username", listItems.get(position).getUsername()); // Put anything what you want
                    bundle.putInt("join_count", listItems.get(position).getJoin_count()); // Put anything what you want
                    bundle.putInt("selected_num", listItems.get(position).selected_num); // Put anything what you want

                    Check_Join_Member check_join_member = new Check_Join_Member();*/
                   // check_join_member.setArguments(bundle);

                 /*   ((AppCompatActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            //신청인원 확인 창이 뜨고 뒤로가기 버튼을 눌렀을 때 직전 프래그먼트가 나올 수 있게 해줌. addToBackStack()메서드가 하는 역할임.
                            .replace(R.id.myinfo, check_join_member).addToBackStack(null)
                            .commit();
*/

                }
            });
        }
        //신청인원을 선택하면 누가 신청했는지에 대한 명단을 띄운다.
        if (listItems.get(position).join_count != 0) {
            holder.btn_confirm_project.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(context, Check_Join_Member.class);
                    intent.putExtra("project_id",listItems.get(position).getId());
                    intent.putExtra("project_email",listItems.get(position).getEmail());
                    intent.putExtra("username",listItems.get(position).getUsername());
                    intent.putExtra("selected_num",listItems.get(position).selected_num);
                    intent.putExtra("join_count",listItems.get(position).getJoin_count());
                    context.startActivity(intent);

                  /*  Bundle bundle = new Bundle();
                    bundle.putInt("project_id", listItems.get(position).getId()); // Put anything what you want
                    bundle.putString("email", listItems.get(position).getEmail()); // Put anything what you want
                    bundle.putString("username", listItems.get(position).getUsername()); // Put anything what you want
                    bundle.putInt("join_count", listItems.get(position).getJoin_count()); // Put anything what you want
                    bundle.putInt("selected_num", listItems.get(position).selected_num); // Put anything what you want
                    System.out.println("선택된자들의 수는 : " + listItems.get(position).selected_num);
                    Check_Join_Member check_join_member = new Check_Join_Member();*/
                  /*  check_join_member.setArguments(bundle);

                    ((AppCompatActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.myinfo, check_join_member).addToBackStack(null)
                            .commit();*/


                }
            });
        }
        //추천을 선택하면 누가 추천했는지에 대한 명단을 띄운다.
        if(listItems.get(position).like_count != 0) {
            holder.txt_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                /*    Bundle bundle = new Bundle();
                    bundle.putInt("project_id", listItems.get(position).getId()); // Put anything what you want
                    bundle.putString("email", listItems.get(position).getEmail()); // Put anything what you want
                    bundle.putString("username", listItems.get(position).getUsername()); // Put anything what you want

                    Check_Like_Member check_like_member = new Check_Like_Member();
                    check_like_member.setArguments(bundle);

                    ((AppCompatActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            //뒤로가기 눌렀을 때 다시 여기 화면으로 돌아오기 위해선 addtoBackStack메서드를 사용.
                            .replace(R.id.myinfo, check_like_member).addToBackStack(null)
                            .commit();
*/

                    Intent intent = new Intent(context, Check_Like_Member.class);
                    intent.putExtra("project_id",listItems.get(position).getId());
                    intent.putExtra("email",listItems.get(position).getEmail());
                    intent.putExtra("username",listItems.get(position).getUsername());

                    context.startActivity(intent);

                }
            });
        }

        //좋아요 버튼 상태를 확인
             stringRequest = new StringRequest(Request.Method.POST, LIKE_STATE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                            if (response.contains("1")) {
                               // Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show();
                                holder.btn_like.setVisibility(View.GONE);
                                holder.btn_unlike.setVisibility(View.VISIBLE);

                            } else {
                               // Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
                                holder.btn_like.setVisibility(View.VISIBLE);
                                holder.btn_unlike.setVisibility(View.GONE);
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

                    System.out.println("이메일은 " + loadLoginEmail());
                    System.out.println("프로젝트 아이디는 " + listItems.get(position).getId());
                    params.put("email_like", loadLoginEmail());
                    params.put("id_like", String.valueOf(listItems.get(position).getId()));

                    return params;

                }
            };

        MySingleton.getInstance(context).addToRequestque(stringRequest);


       /* requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);*/
        //MySingleton.getInstance(context).addToRequestque(stringRequest2);




        //내가작성한 글일 경우만 옵션메뉴의 선택지가 수정,삭제, 공유가 들어가게 된다.

        if (listItems.get(position).email.equals(loadLoginEmail())) {
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
                                //게시글 수정
                                case R.id.menu_item_motify:

                                    break;
                                //게시글 삭제
                                case R.id.menu_item_delete:
                                    //게시물 삭제
                                    final int id = listItems.get(position).getId();
                                    final String ID = Integer.toString(id);
                                    System.out.println("삭제된 포지션 : " + id);
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_PROJECT,
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
        // System.out.println(email_from_login);


        return email_from_login;
    }

}
