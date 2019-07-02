package com.example.goo.test.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.goo.test.Activity.Chat.ChatRoomActivity;
import com.example.goo.test.Item.Item_Chat_Room_List;
import com.example.goo.test.R;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static com.example.goo.test.Activity.Chat.ChatRoomActivity.username;

/**
 * Created by Goo on 2018-05-06.
 */

public class RecyclerViewAdapter_Chat_Messages extends RecyclerView.Adapter<RecyclerViewAdapter_Chat_Messages.MyViewHolder> {
    private List<Item_Chat_Room_List> listItems;
    Context context;


    private static final String FOLLOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/following.php";
    private static final String UNFOLLOW_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/unfollow.php";
    private static final String CHECK_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/checkFollowerIfFollowing.php";

    //뷰홀더에 대한 클래스를 만든다.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextView, titletxt_me, desctxt_me, history_link_me, titletxt, desctxt;
        private CircleImageView prof_pic;
        private TextView username, message, history, message_me, history_me, message_none, history_none,history_img_me, history_img;
        private RelativeLayout relative_message, relative_message_me, relative_message_none, relative_link_me, relative_link;
        private ImageView chat_img_me, chat_img, logo, logo_me;

        public MyViewHolder(View itemView) {
            super(itemView);


            itemView.setOnClickListener(new View.OnClickListener() {

                //해당 리스트뷰의 아이템을 누를 경우 이벤트를 발생시킬 수 있다.
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                }
            });

            //어떤 xml 요소를 뷰홀더로 잡을지 지정해줌.
            mCardView = itemView.findViewById(R.id.mCardView);
            username = itemView.findViewById(R.id.username);
            message = itemView.findViewById(R.id.message);
            history = itemView.findViewById(R.id.history);
            history_me = itemView.findViewById(R.id.history_me);
            chat_img_me = itemView.findViewById(R.id.chat_img_me);
            chat_img = itemView.findViewById(R.id.chat_img);
            prof_pic = (CircleImageView) itemView.findViewById(R.id.prof_pic);
            relative_message = itemView.findViewById(R.id.relative_message);
            relative_message_me = itemView.findViewById(R.id.relative_message_me);
            relative_message_none = itemView.findViewById(R.id.relative_message_none);
            relative_link_me = itemView.findViewById(R.id.relative_link_me);
            relative_link = itemView.findViewById(R.id.relative_link);
            message_me = itemView.findViewById(R.id.message_me);
            message_none = itemView.findViewById(R.id.message_none);
            history_img_me = itemView.findViewById(R.id.history_img_me);
            history_img = itemView.findViewById(R.id.history_img);
            logo = itemView.findViewById(R.id.logo);
            logo_me = itemView.findViewById(R.id.logo_me);
            titletxt = itemView.findViewById(R.id.titletxt);
            titletxt_me = itemView.findViewById(R.id.titletxt_me);
            desctxt = itemView.findViewById(R.id.desctxt);
            desctxt_me = itemView.findViewById(R.id.desctxt_me);
            history_link_me = itemView.findViewById(R.id.history_link_me);
            history_none = itemView.findViewById(R.id.history_none);

        }


    }

    //어댑터 생성자
    public RecyclerViewAdapter_Chat_Messages(Context context, List<Item_Chat_Room_List> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    //뷰홀더로 잡고 있는 것을 만들어줌
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_chat_message, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Item_Chat_Room_List item = listItems.get(position);
        //  Picasso.with(context).load(item.url).error(R.mipmap.ic_launcher_round).into(holder.prof_pic);
        holder.username.setText(item.username);
        holder.message.setText(item.message);

        //지금 접속해 있는 유저 이름과 arraylist안에 있는 유저 이름 값이 같다면 오른쪽에,
        //다르다면 왼쪽에, 접속했거나 나갔다는 알림은 가운데 레이아웃이 위치하도록 해준다.

        //메세지와 이미지가 같이 있는 경우.
        //메세지만 있는 경우, 메세지 중에서 웹 url 값이 있는경우, 없는 경우
        //이미지만 있는 경우.
        class Title_Me extends AsyncTask<Void, Void, Void> {
            String title;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... voids) {


                try {
                    Document document = Jsoup.connect(item.message).get();

                    title = document.title();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                holder.titletxt_me.setText(title);

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
                    Document document = Jsoup.connect(item.message).get();


                    Elements description = document.select("meta[name=description]");
                    desc = description.attr("content");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                holder.desctxt_me.setText(desc);

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
                    Document document = Jsoup.connect(item.message).get();

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

                holder.logo_me.setImageBitmap(bitmap);

            }

        }


        class Title extends AsyncTask<Void, Void, Void> {
            String title;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... voids) {


                try {
                    Document document = Jsoup.connect(item.message).get();

                    title = document.title();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                holder.titletxt.setText(title);

            }

        }

        class Description extends AsyncTask<Void, Void, Void> {
            String desc;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... voids) {


                try {
                    Document document = Jsoup.connect(item.message).get();

                    Elements description = document.select("meta[name=description]");
                    desc = description.attr("content");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                holder.desctxt.setText(desc);

            }
        }

        class Logo extends AsyncTask<Void, Void, Void> {
            Bitmap bitmap;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... voids) {


                try {
                    Document document = Jsoup.connect(item.message).get();

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

                holder.logo.setImageBitmap(bitmap);

            }

        }


        Title_Me title_Me = new Title_Me();
        Description_Me description_Me = new Description_Me();
        Logo_Me logo_Me = new Logo_Me();

        Title title = new Title();
        Description description = new Description();
        Logo logo = new Logo();


        if (item.username.equals(username)) {

            item.me = 1;
            holder.relative_message.setVisibility(View.GONE);
            holder.relative_message_me.setVisibility(View.VISIBLE);
            holder.relative_message_none.setVisibility(View.GONE);
            //메세지와 이미지 둘 다 있는 경우
            if (item.img_in == 1 && item.message_in == 1) {
                holder.chat_img_me.setVisibility(View.VISIBLE);
                holder.message_me.setVisibility(View.VISIBLE);

                holder.history.setText(item.history);
                holder.history_img.setText(item.history);
                holder.history_me.setText(item.history);
                holder.history_img_me.setText(item.history);

                //메세지가 웹 url인 경우라면
                if (item.link_url_in == 1) {
                    //텍스트뷰에 해당 url 링크를 걸어준다.
                    holder.message_me.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.message_me.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                            browserIntent.setData(Uri.parse(item.message));
                            context.startActivity(browserIntent);
                        }
                    });
                    holder.relative_link_me.setVisibility(View.VISIBLE);
                    holder.relative_link.setVisibility(View.GONE);
                    title_Me.execute();
                    description_Me.execute();
                    logo_Me.execute();
                } else {
                    holder.relative_link_me.setVisibility(View.GONE);
                }
                //이미지만 있는 경우
            } else if (item.img_in == 1 && item.message_in == 0) {

                holder.chat_img_me.setVisibility(View.VISIBLE);
                holder.message_me.setVisibility(View.GONE);
                holder.history_me.setVisibility(View.GONE);
                holder.history_img_me.setVisibility(View.VISIBLE);

                holder.history.setText(item.history);
                holder.history_img.setText(item.history);
                holder.history_me.setText(item.history);
                holder.history_img_me.setText(item.history);
                //메세지가 웹 url인 경우라면
                if (item.link_url_in == 1) {
                    //텍스트뷰에 해당 url 링크를 걸어준다.
                    holder.message_me.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.message_me.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                            browserIntent.setData(Uri.parse(item.message));
                            context.startActivity(browserIntent);
                        }
                    });
                    holder.relative_link_me.setVisibility(View.VISIBLE);
                    holder.relative_link.setVisibility(View.GONE);
                    title_Me.execute();
                    description_Me.execute();
                    logo_Me.execute();
                } else {
                    holder.relative_link_me.setVisibility(View.GONE);
                }
            }
            //메세지만 있는 경우
            else if (item.img_in == 0 && item.message_in == 1) {
                holder.chat_img_me.setVisibility(View.GONE);
                holder.message_me.setVisibility(View.VISIBLE);
                holder.history_me.setVisibility(View.VISIBLE);
                holder.history_img_me.setVisibility(View.GONE);

                holder.history.setText(item.history);
                holder.history_img.setText(item.history);
                holder.history_me.setText(item.history);
                holder.history_img_me.setText(item.history);
                //메세지가 웹 url인 경우라면
                if (item.link_url_in == 1) {
                    //텍스트뷰에 해당 url 링크를 걸어준다.
                    holder.message_me.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.message_me.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                            browserIntent.setData(Uri.parse(item.message));
                            context.startActivity(browserIntent);
                        }
                    });
                    holder.relative_link_me.setVisibility(View.VISIBLE);
                    holder.relative_link.setVisibility(View.GONE);


                    title_Me.execute();
                    description_Me.execute();
                    logo_Me.execute();
                } else {
                    holder.relative_link_me.setVisibility(View.GONE);
                }
            }
        } else {
            item.me = 0;
            holder.relative_message.setVisibility(View.VISIBLE);
            holder.relative_message_me.setVisibility(View.GONE);
            holder.relative_message_none.setVisibility(View.GONE);
            //메세지와 이미지 둘 다 있는 경우
            if (item.img_in == 1 && item.message_in == 1) {

                holder.chat_img.setVisibility(View.VISIBLE);
                holder.message.setVisibility(View.VISIBLE);
                holder.history.setVisibility(View.VISIBLE);
                holder.history_img.setVisibility(View.VISIBLE);

                holder.history.setText(item.history);
                holder.history_img.setText(item.history);
                holder.history_me.setText(item.history);
                holder.history_img_me.setText(item.history);
                //메세지가 웹 url인 경우라면
                if (item.link_url_in == 1) {
                    //텍스트뷰에 해당 url 링크를 걸어준다.
                    holder.message.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.message.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                            browserIntent.setData(Uri.parse(item.message));
                            context.startActivity(browserIntent);
                        }
                    });
                    holder.relative_link_me.setVisibility(View.GONE);
                    holder.relative_link.setVisibility(View.VISIBLE);
                    title.execute();
                    description.execute();
                    logo.execute();

                } else {
                    holder.relative_link.setVisibility(View.GONE);
                }

                //이미지만 있는 경우
            } else if (item.img_in == 1 && item.message_in == 0) {

                holder.chat_img.setVisibility(View.VISIBLE);
                holder.message.setVisibility(View.GONE);
                holder.history.setVisibility(View.GONE);
                holder.history_img.setVisibility(View.VISIBLE);

                holder.history.setText(item.history);
                holder.history_img.setText(item.history);
                holder.history_me.setText(item.history);
                holder.history_img_me.setText(item.history);
                //메세지가 웹 url인 경우라면
                if (item.link_url_in == 1) {
                    //텍스트뷰에 해당 url 링크를 걸어준다.
                    holder.message.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.message.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                            browserIntent.setData(Uri.parse(item.message));
                            context.startActivity(browserIntent);
                        }
                    });
                    holder.relative_link_me.setVisibility(View.GONE);
                    holder.relative_link.setVisibility(View.VISIBLE);
                    title.execute();
                    description.execute();
                    logo.execute();

                } else {
                    holder.relative_link.setVisibility(View.GONE);
                }


                //메세지만 있는 경우
            } else if (item.img_in == 0 && item.message_in == 1) {

                holder.chat_img.setVisibility(View.GONE);
                holder.message.setVisibility(View.VISIBLE);
                holder.history.setVisibility(View.VISIBLE);
                holder.history_img.setVisibility(View.GONE);

                holder.history.setText(item.history);
                holder.history_img.setText(item.history);
                holder.history_me.setText(item.history);
                holder.history_img_me.setText(item.history);

                //메세지가 웹 url인 경우라면
                if (item.link_url_in == 1) {
                    //텍스트뷰에 해당 url 링크를 걸어준다.
                    holder.message.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.message.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                            browserIntent.setData(Uri.parse(item.message));
                            context.startActivity(browserIntent);
                        }
                    });
                    holder.relative_link_me.setVisibility(View.GONE);
                    holder.relative_link.setVisibility(View.VISIBLE);
                    title.execute();
                    description.execute();
                    logo.execute();

                } else {
                    holder.relative_link.setVisibility(View.GONE);
                }

            }
            if (item.username.isEmpty()) {
                item.none = 1;
                holder.relative_message.setVisibility(View.GONE);
                holder.relative_message_me.setVisibility(View.GONE);
                holder.chat_img_me.setVisibility(View.GONE);
                holder.chat_img.setVisibility(View.GONE);
                holder.relative_message_none.setVisibility(View.VISIBLE);

            }
        }

        //내가 보낸 메세지는 메세지 화면이 오른쪽에 위치한다.
        if (item.me == 1) {
            holder.relative_message.setVisibility(View.GONE);
            holder.relative_message_me.setVisibility(View.VISIBLE);
            holder.message_me.setText(item.message);
            holder.history_me.setText(item.history);
            //내가 만약 이미지를 보냈다면 이미지를 출력해주고, 메세지 부분은 안보이도록 처리한다.
            if (item.img != null && item.img_in == 1) {
                holder.history_img_me.setText(item.history);
                holder.chat_img_me.setVisibility(View.VISIBLE);
                holder.chat_img.setVisibility(View.GONE);
                Picasso.with(context).load(item.img.toString()).error(R.mipmap.ic_launcher_round).into(holder.chat_img_me);
                if (item.message.equals("null") || item.message == null || item.message.isEmpty()) {
                    holder.message_me.setVisibility(View.GONE);
                } else {
                    holder.message_me.setVisibility(View.VISIBLE);
                    holder.history_me.setText(item.history);
                    //holder.chat_img_me.setVisibility(View.GONE);
                }
            } else {
                //item.img_in = false;
                holder.chat_img_me.setVisibility(View.GONE);
                holder.chat_img.setVisibility(View.GONE);

            }
            //상대방이 보낸 메세지는 왼쪽에 위치한다.
        } else {

            holder.relative_message.setVisibility(View.VISIBLE);
            holder.relative_message_me.setVisibility(View.GONE);
            holder.relative_message_none.setVisibility(View.GONE);
            //상대방의 프로필 이미지가 없다면 임의의 프로필 이미지를 넣어준다.
            if (item.url == null || item.url.equals("null") || item.url.isEmpty()) {
                Picasso.with(context).load("http://13.125.216.157/uploads/Mingu Seo.jpg").error(R.mipmap.ic_launcher_round).into(holder.prof_pic);
                //프로필 이미지가 있을 때.
            } else {
                Picasso.with(context).load(item.url.toString()).error(R.mipmap.ic_launcher_round).into(holder.prof_pic);
                //상대가 보낸 메세지에 이미지가 있는 경우.
                if (!item.img.isEmpty() && item.img_in == 1) {
                    holder.history_img.setText(item.history);
                    holder.chat_img_me.setVisibility(View.GONE);
                    holder.chat_img.setVisibility(View.VISIBLE);
                    System.out.println("내가 유알엘 값을 가져 왔나? " + item.img.toString());
                    Picasso.with(context).load(item.img.toString()).error(R.mipmap.ic_launcher_round).into(holder.chat_img);
                    //이미지는 있는데 메세지가 없는 경우
                    if (item.message == null || item.message.equals("null") || item.message.isEmpty()) {
                        holder.message.setVisibility(View.GONE);
                        //이미지와 메세지 둘 다 있는 경우.
                    } else {
                        holder.message.setVisibility(View.VISIBLE);
                        holder.history.setText(item.history);
                    }
                    //상대가 보낸 메세지 중 이미지가 없는 경우라면 이미지 UI는 안보이도록 해준다.
                } else {

                    holder.chat_img_me.setVisibility(View.GONE);
                    holder.chat_img.setVisibility(View.GONE);

                    System.out.println("내가 유알엘 값을 가져 왔나? " + item.img.toString());

                }
            }
            if (item.username.isEmpty()) {
                item.none = 1;
                holder.relative_message.setVisibility(View.GONE);
                holder.relative_message_me.setVisibility(View.GONE);
                holder.relative_message_none.setVisibility(View.VISIBLE);
                holder.message_none.setText(item.message);
                holder.chat_img.setVisibility(View.GONE);
                holder.chat_img_me.setVisibility(View.GONE);

            }
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
