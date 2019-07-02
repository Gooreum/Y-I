package com.example.goo.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.goo.test.Activity.Home.Check_Join_Member;
import com.example.goo.test.Activity.Home.Check_Like_Member;
import com.example.goo.test.Activity.Home.Content_Detail_Activity;
import com.example.goo.test.Activity.MainActivity;
import com.example.goo.test.Fragment.Home.Fragment_Reply;
import com.example.goo.test.Fragment.MyInfo.MyInfoFragment_By_Friend;
import com.example.goo.test.Item.ListItem_Check_Join_Member;
import com.example.goo.test.Item.ListItem_Show_Project;
import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

//import com.ms.square.android.expandabletextview.ExpandableTextView;

/**
 * Created by Goo on 2018-05-07.
 */

public class RecyclerViewAdapter_Show_Completed_Project extends RecyclerView.Adapter<RecyclerViewAdapter_Show_Completed_Project.MyViewHolder> {

    private List<ListItem_Show_Project> listItems;
    Context context;
    //ImageView imageView;
    StringRequest stringRequest;
    StringRequest stringRequest2;
    StringRequest stringRequest3;

    int url_length;
    private static final String LIKE_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/project_like.php";
    private static final String JOIN_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/project_join.php";
    private static final String DELETE_PROJECT = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/delete_project_content.php";
    private static final String LIKE_STATE = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/checkLikeState.php";
    private static final String JOIN_STATE = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/checkJoinState.php";
    private static final String SHOW_JOIN_MEMBER_IMG = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/SHOW_JOIN_MEMBER_IMG.php";

    //안드로이드 현재시간 구하기
    long mNow;
    Date mDate;

    //시간에  대문자 HH를 넣어줘야 24시간 형식으로 출력됨.
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView mCardView;
        private TextView text_username, dev_duration, apply_duration, member_num, gps,recruiting,more_recruit_num;
        private TextView text_email;
        private TextView text_title;
        private TextView amount;
        //private TextView text_content;
        private ReadMoreTextView text_content;
        private TextView text_history;
        private TextView txtOptionDigit;
        private TextView txtOptionDigit_not_myproject;
        private LinearLayout line_join, line_pic,line_more_recruit,line_project_url;
        // private Button btn_join, ;
        private ImageView btn_like, btn_unlike, btn_reply, btn_join, btn_unjoin, btn_confirm_project, btn_project_completed,url_img ;
        private HorizontalScrollView scrollView;
        private CircleImageView prof_pic, join_member_pic;
        private TextView txt_like, txt_join, txt_reply,url_title,url_content;
        private RelativeLayout line_texts;

        // private ExpandableTextView expandableTextView;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {

                //해당 리스트뷰의 아이템을 누를 경우 이벤트를 발생시킬 수 있다.
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    //상세보기 화면으로 전환.
                    //해당 프로젝트 글의 이메일 값과 프로젝트 아이디 값을 전달.
                    if (pos != RecyclerView.NO_POSITION) {
                        System.out.println("포지션은 : " + pos + " " + listItems.get(pos).username.toString());
                        System.out.println("포지션은 : " + pos + " " + listItems.get(pos).id);
                        Intent intent = new Intent(context, Content_Detail_Activity.class);
                        intent.putExtra("project_id",listItems.get(pos).getId());
                        intent.putExtra("project_email",listItems.get(pos).getEmail());
                        intent.putExtra("project_completed",listItems.get(pos).project_completed);
                        intent.putExtra("project_url",listItems.get(pos).project_url);

                        context.startActivity(intent);

                    }
                }
            });


            //어떤 xml 요소를 뷰홀더로 잡을지 지정해줌.
            mCardView = (CardView) itemView.findViewById(R.id.card_container);
            text_username = (TextView) itemView.findViewById(R.id.text_username);
            recruiting = (TextView) itemView.findViewById(R.id.recruiting);
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
            btn_join = (ImageView) itemView.findViewById(R.id.btn_join);
            btn_unjoin = (ImageView) itemView.findViewById(R.id.btn_unjoin);
            btn_confirm_project = itemView.findViewById(R.id.btn_confirm_project);
            btn_project_completed = itemView.findViewById(R.id.btn_project_completed);
            line_texts = (RelativeLayout) itemView.findViewById(R.id.line_texts);
            line_project_url = (LinearLayout) itemView.findViewById(R.id.line_project_url);
            txt_join = (TextView) itemView.findViewById(R.id.txt_join);
            txt_reply = (TextView) itemView.findViewById(R.id.txt_reply);
            txt_like = (TextView) itemView.findViewById(R.id.txt_like);
            line_join = (LinearLayout) itemView.findViewById(R.id.line_join);
            line_pic = (LinearLayout) itemView.findViewById(R.id.line_pic);

            url_img = itemView.findViewById(R.id.url_img);
            url_title = itemView.findViewById(R.id.url_title);
            url_content = itemView.findViewById(R.id.url_content);

            amount = itemView.findViewById(R.id.amount);


            prof_pic = (CircleImageView) itemView.findViewById(R.id.prof_pic);
            join_member_pic = (CircleImageView) itemView.findViewById(R.id.join_member_pic);
            dev_duration = itemView.findViewById(R.id.dev_duration);
            apply_duration = itemView.findViewById(R.id.apply_duration);
            member_num = itemView.findViewById(R.id.member_num);
            gps = itemView.findViewById(R.id.gps);

        }
    }

    //어댑터 생성자
    public RecyclerViewAdapter_Show_Completed_Project(Context context, List<ListItem_Show_Project> listItems) {
        this.context = context;
        this.listItems = listItems;

    }

    //뷰홀더로 잡고 있는 것을 만들어줌
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_completed_project, parent, false);


        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ListItem_Show_Project item = listItems.get(position);
        holder.amount.setText(item.amount+" GOO_TOKEN");
        class Title_Me extends AsyncTask<Void, Void, Void> {
            String title;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... voids) {


                try {
                    Document document = Jsoup.connect(item.project_url).get();

                    title = document.title();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                holder.url_title.setText(title);

            }

        }

        class Description_Me extends AsyncTask<Void, Void, Void> {
            String desc;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... voids) {


                try {
                    Document document = Jsoup.connect(item.project_url).get();


                    Elements description = document.select("meta[name=description]");
                    desc = description.attr("content");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                holder.url_content.setText(desc);
               /* holder.url_content.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(item.project_url));
                        context.startActivity(browserIntent);
                    }
                });*/
            }

        }

        class Logo_Me extends AsyncTask<Void, Void, Void> {
            Bitmap bitmap;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... voids) {


                try {
                    Document document = Jsoup.connect(item.project_url).get();

                    Element img = document.select("img").first();
                    String srcValue = img.absUrl("src");
                    InputStream input = new URL(srcValue).openStream();
                    bitmap = BitmapFactory.decodeStream(input);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                holder.url_img.setImageBitmap(bitmap);

            }
        }


        //프로젝트 url 값이 null값이 아니거나 url정규표현식이 제대로 되었다면 url 미리보기를 그려준다.
        if(item.project_url != null) {
            if (extractUrlParts(item.project_url) == true) {
                holder.line_project_url.setVisibility(View.VISIBLE);

                Title_Me title_Me = new Title_Me();
                Description_Me description_Me = new Description_Me();
                Logo_Me logo_Me = new Logo_Me();

                //해당 url의 제목,본문내용,이미지를 미리볼 수 있도록 해준다.
                title_Me.execute();
                description_Me.execute();
                logo_Me.execute();
                holder.url_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(item.project_url));
                        context.startActivity(browserIntent);
                    }
                });
            } else {
                holder.url_content.setText("잘못된 url 값입니다.");
            }
        }



        holder.gps.setText(item.gps);
        holder.text_username.setText(item.getUsername());
        // holder.text_email.setText(item.getEmail());
        holder.text_title.setText(item.getTitle());
        holder.text_content.setText(item.getContent());
        holder.text_history.setText(item.getHistory());
        holder.apply_duration.setText(item.apply_duration);
        holder.dev_duration.setText(item.dev_duration);
        holder.member_num.setText(item.member_num + "");
        //프로필 사진 출력
        Picasso.with(context)
                .load(item.url)
                .error(R.mipmap.ic_launcher_round)
                .fit()
                .into(holder.prof_pic);


        //친구 프로필 사진을 눌렀을 때 해당 친구의 프로필 화면으로 옮김.
        holder.prof_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                //친구의 이메일 값을 쉐어드로 저장해둠.
                SharedPreferences ID = context.getApplicationContext().getSharedPreferences("friend_email", MODE_PRIVATE);
                SharedPreferences.Editor editor = ID.edit();
                editor.putString("friend_email", listItems.get(position).getEmail());
                editor.commit();
                // main_content_home
                MyInfoFragment_By_Friend myinfo = new MyInfoFragment_By_Friend();
                myinfo.setArguments(bundle);
                if (context instanceof MainActivity) {

                    MainActivity myActivity = (MainActivity) context;

                    myActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, myinfo).addToBackStack(null)
                            .commit();
                } else {
                    ((AppCompatActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_content_home, myinfo).addToBackStack(null)
                            .commit();

                }
            }
        });


        //진행중인 프로젝트 이지만 추가모집중이라면 join,unjoin버튼이 보이도록 한다.
        //그리고 '추가모집중'이라는 태그가 보이도록 한다.
        //내가 작성한 모집글인 경우 신청확인 버튼이 나타나게 해줌.

        if (item.email.equals(loadLoginEmail())) {
            item.me = true;
        } else {
            item.me = false;
        }

        //like=false 이면 좋아요버튼이 보이게, like=true이면 좋아요 취소 버튼이 보이게.
        if (item.getLike() == true) {
            holder.btn_like.setVisibility(View.GONE);
            holder.btn_unlike.setVisibility(View.VISIBLE);
        } else {
            holder.btn_like.setVisibility(View.VISIBLE);
            holder.btn_unlike.setVisibility(View.GONE);

        }


        if (item.getLike_count() != 0 || item.getJoin_count() != 0 || item.getReply_count() != 0) {
            item.counting = true;
            holder.line_texts.setVisibility(View.VISIBLE);
            holder.txt_like.setText("추천 " + item.getLike_count());
            holder.txt_join.setText("신청인원 " + item.getJoin_count() + " 명");
            holder.txt_reply.setText("댓글 " + item.getReply_count());
            //신청인원수가 아무도 없을 때 띄우는 문구.
            if (item.getJoin_count() == 0) {
                holder.txt_join.setText(item.getUsername() + "님의 첫 프로젝트 신청자가 되어 보세요. ");
            } else if (item.getLike_count() == 0) {
                holder.txt_like.setText("추천 ");
            } else if (item.getLike_count() == 0 && item.getJoin_count() == 0 && item.getReply_count() == 0) {
                item.counting = false;
                holder.line_texts.setVisibility(View.GONE);
            }
        }

        //좋아요,신청하기가 하나도 없는 경우와 하나라도 있을 경우 line_texts의 visibility 설정값 정해주기.그래야 리사이클러뷰가 꼬이지 않는다.
        if (item.counting == true) {
            holder.line_texts.setVisibility(View.VISIBLE);
        } else {
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
                                if (response.contains("1")) {

                                    //좋아요를 누르면 좋아요 갯수가 +1 증가.
                                    item.like_count = item.like_count + 1;
                                    holder.txt_like.setText("추천 " + item.getLike_count());
                                    holder.txt_join.setText("신청인원 " + item.getJoin_count() + " 명");
                                    holder.txt_reply.setText("댓글 " + item.getReply_count());
                                    holder.line_texts.setVisibility(View.VISIBLE);
                                    notifyDataSetChanged();

                                } else {
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
                        params.put("history", getTime());
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
                                if (response.contains("1")) {

                                    //좋아요 취소를 누르면 좋아요 갯수가 하나씩 줄어듦.
                                    //Toast.makeText(context, "1", Toast.LENGTH_LONG).show();
                                    item.like_count = item.like_count - 1;
                                    holder.txt_like.setText("추천 " + item.getLike_count());
                                    holder.txt_join.setText("신청인원 " + item.getJoin_count() + " 명");
                                    holder.txt_reply.setText("댓글 " + item.getReply_count());
                                    holder.line_texts.setVisibility(View.VISIBLE);
                                    notifyDataSetChanged();
                                    //좋아요 갯수가 0개인 경우 좋아요 갯수 공간이 사라짐
                                    if (item.like_count == 0 && item.join_count == 0 && item.reply_count == 0) {
                                        holder.line_texts.setVisibility(View.GONE);
                                        notifyDataSetChanged();
                                    }

                                } else {
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



        //댓글 달기
        holder.btn_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,Fragment_Reply.class);
                intent.putExtra("project_id",listItems.get(position).getId());
                context.startActivity(intent);

            }
        });
        //신청인원을 선택하면 누가 신청했는지에 대한 명단을 띄운다.
        if (listItems.get(position).join_count != 0) {
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


                }
            });
        }
        //댓글 띄우기
        if (listItems.get(position).reply_count != 0) {
            holder.txt_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,Fragment_Reply.class);
                    intent.putExtra("project_id",listItems.get(position).getId());
                    context.startActivity(intent);
                }
            });
        }
        //추천을 선택하면 누가 추천했는지에 대한 명단을 띄운다.
        if (listItems.get(position).like_count != 0) {
            holder.txt_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(context, Check_Like_Member.class);
                    intent.putExtra("project_id",listItems.get(position).getId());
                    intent.putExtra("email",listItems.get(position).getEmail());
                    intent.putExtra("username",listItems.get(position).getUsername());

                    context.startActivity(intent);

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


        // MySingleton.getInstance(context).addToRequestque(stringRequest);

        //신청하기 버튼 상태를 확인

        if (!listItems.get(position).email.equals(loadLoginEmail())) {

            stringRequest2 = new StringRequest(Request.Method.POST, JOIN_STATE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response2) {

                            // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                            if (response2.contains("1")) {
                                // Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show();
                                holder.btn_join.setVisibility(View.GONE);
                                holder.btn_unjoin.setVisibility(View.VISIBLE);


                                stringRequest3 = new StringRequest(Request.Method.POST, SHOW_JOIN_MEMBER_IMG,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response5) {

                                                try {
                                                    JSONObject jsonobject = new JSONObject(response5);
                                                    JSONArray jsonarray = jsonobject.getJSONArray("project_join");
                                                    // JSONObject data = jsonarray.getJSONObject(0);

                                                    for (int i = 0; i < jsonarray.length(); i++) {
                                                        JSONObject object = jsonarray.getJSONObject(i);
                                                        ListItem_Check_Join_Member item2 = new ListItem_Check_Join_Member();

                                                        item2.url = object.getString("join_profile");

                                                        //신청인원 텍스트 옆에 내 프로필 사진 붙이기

                                                        Picasso.with(context)
                                                                .load(item2.url)
                                                                .error(R.mipmap.ic_launcher_round)
                                                                .fit()
                                                                .into(holder.join_member_pic);


                                                    }


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
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

                                        params.put("join_id", String.valueOf(listItems.get(position).getId()));
                                        params.put("email", loadLoginEmail());

                                        return params;

                                    }
                                };

                                MySingleton.getInstance(context).addToRequestque(stringRequest3);


                            } else {
                                // Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
                                holder.btn_join.setVisibility(View.VISIBLE);
                                holder.btn_unjoin.setVisibility(View.GONE);
                                holder.join_member_pic.setVisibility(View.GONE);
                                // notifyDataSetChanged();
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

                    params.put("email_join", loadLoginEmail());
                    params.put("id_join", String.valueOf(listItems.get(position).getId()));

                    return params;

                }

            };


            MySingleton.getInstance(context).addToRequestque(stringRequest2);
        }

        MySingleton.getInstance(context).addToRequestque(stringRequest);


        //내가작성한 글일 경우만 옵션메뉴의 선택지가 수정,삭제, 공유가 들어가게 된다.

        if (listItems.get(position).getEmail().equals(loadLoginEmail())) {
            //내가 작성한 글일 경우 모집확인 버튼으로 변경
            holder.txtOptionDigit_not_myproject.setVisibility(View.GONE);
            holder.txtOptionDigit.setVisibility(View.VISIBLE);
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
    //보낸 메세지가 url형식인지 판단해주는 정규표현식.
    public boolean extractUrlParts(String url) {
        //String url = editMessage.getText().toString();

        // Pattern urlPattern = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");
        Pattern urlPattern = Pattern.compile("[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");


        Matcher mc = urlPattern.matcher(url);

        if (mc.matches()) {
            System.out.println(true);
            return true;
        } else {
            System.out.println(false);
            return false;
        }

    }

    //현재시간 구하기
    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}