package com.example.goo.test.Activity.Chat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goo.test.Activity.Home.Content_Detail_Activity;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Chat_Messages;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Chatting_Member;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Join_Member_Detail;
import com.example.goo.test.ConnectActivity;
import com.example.goo.test.Database.DatabaseHelper;
import com.example.goo.test.Dialog.Dialog;
import com.example.goo.test.Dialog.Dialog_Change_Room_Name;
import com.example.goo.test.Dialog.Dialog_Project_Url;
import com.example.goo.test.Item.Item_Chat_MyProfile;
import com.example.goo.test.Item.Item_Chat_Room_List;
import com.example.goo.test.Item.ListItem_Check_Join_Member;
import com.example.goo.test.Item.ListItem_Check_Like_Member;
import com.example.goo.test.R;
import com.example.goo.test.Util.MyCommand;
import com.example.goo.test.Util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.goo.test.Activity.Home.Check_Join_Member.UPDATE_PROJECT_STATE;

/**
 * Created by Goo on 2018-06-06.
 */

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String UPDATE_CREATED_STATE = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/update_created_state.php";
    private static final String CHAT_MEMBER = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_chat_member.php";
    private static final String CHAT_IMAGE_UPLOAD = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/chat_image_upload.php";
    private static final String SEND_NOTIFICATION = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/send_notification.php";
    private static final String UPDATE_CHAT_STATE = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/update_chat_in_state.php";
    private final String CHECK_PROJECT_STATE = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/check_project_state.php";
    private final int GALLERY_REQUEST = 1200;


    //안드로이드 현재시간 구하기
    long mNow;
    Date mDate;

    //시간에  대문자 HH를 넣어줘야 24시간 형식으로 출력됨.
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    EditText editIp, editPort, editMessage;
    Button btnConnect, btnSend;
    TextView txtMessage, d_day, notification, room_title_change, member_out, streaming, members;
    public static TextView recruit_cancel, recruit, project_quit, project_done;
    ImageView add, cancel;
    LinearLayout invite_line;
    RelativeLayout add_menu;
    CircleImageView album, profile;
    List<ListItem_Check_Like_Member> listItems;

    RecyclerView recyclerview_chat_member;
    //메세지를 담을 recyclerview 선언
    RecyclerView recyclerview_chat_messages;


    Item_Chat_Room_List item_chat_messages;
    //이미지 경로를 담을 arraylist 선언
    ArrayList<Uri> imageList;
    //이미지를 담을 레이아웃 선언
    private LinearLayout linearMain;
    //채팅메세지 넣을 리스트 선언
    List<Item_Chat_Room_List> list_messages;
    // Item_Chat_Room_List item_chat_messages;


    RecyclerViewAdapter_Chat_Messages adapter;
    RecyclerView.LayoutManager layoutManager;

    Handler msgHandler;
    SocketClient client;
    ReceiveThread receive;
    SendThread send;
    Socket socket;
    //아이피 주소
    // String ip = "192.168.145.1";
    String ip = "13.125.216.157";

    //포트번호
    String port = "5001";


    //방번호
   public static int room_num;

    //프로젝트 번호
    public static int project_id;


    //전송 시간을 보내주기 위한 시간 객체 선언.
    //방을 만든 사람
    String boss;
    Date today;
    SimpleDateFormat format;
    //유저네임을 static으로 지정하여 adapter에서도 사용할 수 있도록 한다.
    //서버로부터 입력 받은 값과 현재 로그인해 있는 유저이름이 같은지 다른지 구분해주기 위해..
    public static String username;
    String url;

    Intent intent;

    Context context;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    private DrawerLayout drawerLayout;

    //데이터베이스 선언
    DatabaseHelper myDB;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room2);
        System.out.println("온크리에이트 되었습니다.");

        setUpToolbar();
        context = this; //컨텍스트 정보 설정?

        //채팅방 리스트에서 해당 방번호를 가지고 온다.
        intent = getIntent();
        username = intent.getStringExtra("username");
        room_num = intent.getIntExtra("room_num", 0);
        url = intent.getStringExtra("url");
        boss = intent.getStringExtra("boss");
        project_id = intent.getIntExtra("project_id", 0);
        System.out.println("채팅방 프로젝트 아이디 값은 : " + project_id);

        myDB = new DatabaseHelper(this);

        listItems = new ArrayList<ListItem_Check_Like_Member>();

        //채팅 메세지를 담을 리스트 생성
        list_messages = new ArrayList<Item_Chat_Room_List>();
        //이미지를 담을 arraylist 생성
        imageList = new ArrayList<>();

        //이미지를 담을 레이아웃 생성
        linearMain = findViewById(R.id.linearMain);
        //채팅멤버 리사이클러뷰 관련
        recyclerview_chat_member = findViewById(R.id.recyclerview_chat_member);
        recyclerview_chat_member.setLayoutManager(new LinearLayoutManager(ChatRoomActivity.this));
        recyclerview_chat_member.setHasFixedSize(true);

        //채팅메세지 리사이클러뷰 생성
        recyclerview_chat_messages = findViewById(R.id.recyclerview_chat_messages);
        recyclerview_chat_messages.setLayoutManager(new LinearLayoutManager(ChatRoomActivity.this));
        recyclerview_chat_messages.setHasFixedSize(true);

        //대화방에 들어 왔을 때 기존의 대화 목록을 불러온다.
        refreshData();

        //추가 버튼 누르면 메뉴 레이아웃 가져오기
        add_menu = findViewById(R.id.add_menu);
        //앨범 가져오기
        album = findViewById(R.id.album);
        //다른 사람 프로필 가져오기
        profile = findViewById(R.id.profile);
        //사진 및 기타 파일 전송 버튼
        add = findViewById(R.id.add);
        //사진 및 기타 파일 전송 레이아웃 취소하기 버튼
        cancel = findViewById(R.id.cancel);
        //메세지 입력 텍스트
        editMessage = findViewById(R.id.editMessage);
        //메세지 전송 버튼
        btnSend = findViewById(R.id.btnSend);
        //메세지 출력 텍스트
        //txtMessage = findViewById(R.id.txtMessage);
        //드로우 네비게이션에서 디데이 텍스트
        // d_day = findViewById(R.id.d_day);
        //드로우 네비게이션에서 공지사항 버튼
        notification = findViewById(R.id.notification);
        //드로우 네비게이션에서 방제목 변경 버튼
        room_title_change = findViewById(R.id.room_title_change);
        //드로우 네비게이션에서 강퇴버튼(방장만 보여지게 한다.)
        //member_out = findViewById(R.id.member_out);
        //드로우 네비게이션에서 화상채팅 버튼
        streaming = findViewById(R.id.streaming);
        //드로우 네비게이션에서 추가모집 버튼
        recruit = findViewById(R.id.recruit);
        //드로우 네비게이션에서 추가모집 취소 버튼
        recruit_cancel = findViewById(R.id.recruit_cancel);
        //드로우 네비게이션에서 프로젝트 그만두기 버튼
        project_quit = findViewById(R.id.project_quit);
        //드로우 네비게이션에서 프로젝트 완료 버튼
        project_done = findViewById(R.id.project_done);
        //드로우 네비게이션에서 대화상대 초대 버튼(실수로 멤버가 방을 나간 경우를 위해)
        invite_line = findViewById(R.id.invite_line);

        //친구초대 하기 버튼 이벤트
        invite_line.setOnClickListener(this);
        //추가버튼 누르면 추가 메뉴 올려주기
        add.setOnClickListener(this);
        //앨범 누르면 사진 띄우기
        album.setOnClickListener(this);
        //추가하기 취소버튼
        cancel.setOnClickListener(this);
        //프로젝트 완료 버튼을 누르면 해당 프로젝트 게시글이 '프로젝트 완료'상태로 변경된다.
        project_done.setOnClickListener(this);
        //영상통화 하기
        streaming.setOnClickListener(this);

        //프로젝트 추가모집
        recruit.setOnClickListener(this);
        //프로젝트 추가모집 취소
        recruit_cancel.setOnClickListener(this);
        //채팅멤버들 불러오기
        uploadChatMember();
        //방제목 변경하기
        room_title_change.setOnClickListener(this);
      /*  //현재 프로젝트 방이 추가모집중인지 아닌지 확인해주기. 추가모집중이라면 추가모집 취소버튼이, 아니라면 추가모집버튼이 보이도록 한다.
        checkProjectStateRecruiting();
        //현재 프로젝트 방이 완료된 상태라면 추가모집,추가모집 취소,프로젝트 그만두기,프로젝트 완료버튼 안보이게 한다.
        checkProjectStateCompleted();*/
        //프로젝트 채팅방이 아니라면 '디데이, 추가모집,프로젝트 그만두기,프로젝트 완료 버튼이 안보이게 한다.
        if (project_id == 0) {
            //  d_day.setVisibility(View.GONE);
            recruit.setVisibility(View.GONE);
            project_quit.setVisibility(View.GONE);
            project_done.setVisibility(View.GONE);
            //방을 만든 사람만 위의 버튼들이 보이게 하기
        } else if (project_id != 0 && boss.equals(loadLoginEmail())) {
            // d_day.setVisibility(View.VISIBLE);
            recruit.setVisibility(View.VISIBLE);
            project_quit.setVisibility(View.VISIBLE);
            project_done.setVisibility(View.VISIBLE);
            //현재 프로젝트 방이 추가모집중인지 아닌지 확인해주기. 추가모집중이라면 추가모집 취소버튼이, 아니라면 추가모집버튼이 보이도록 한다.
            checkProjectStateRecruiting();
            //현재 프로젝트 방이 완료된 상태라면 추가모집,추가모집 취소,프로젝트 그만두기,프로젝트 완료버튼 안보이게 한다.
            checkProjectStateCompleted();
        }
        //프로젝트 방이지만 방장이 아니라면 추가모집,프로젝트 그만두기,프로젝트 완료 버튼은 보이지 않도록 한다.
        else if (project_id != 0 && !boss.equals(loadLoginEmail())) {
            recruit.setVisibility(View.GONE);
            project_quit.setVisibility(View.GONE);
            project_done.setVisibility(View.GONE);
        }
        //채팅방으로 들어오면 메세지 전송 에디트 텍스트 키보드가 올라 오지 않도록 한다.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //백그라운드 스레드에서 받은 메세지를 처리해주는 핸들러 생성
        msgHandler = new Handler() {
            @Override
            //
            public void handleMessage(Message msg) {


                //유저네임
                if (msg.what == 1111) {


                    // 프로필 이미지
                } else if (msg.what == 2222) {


                    //메세지
                } else if (msg.what == 3333) {


                } else if (msg.what == 4444) {


                    //전송시간
                } else if (msg.what == 5555) {

                }

                //receive 쓰레드를 통해 들어온 메세지들은 핸들러로 오게 된다. 이 때 리사이클러뷰를 통해
                //메세지들을 보여주게 한다.
                //myDB.addData(item_chat_messages);
                //  list_messages = myDB.getAllMessages(room_num);

                //list_messages = myDB.getAllMessages(room_num);
                RecyclerViewAdapter_Chat_Messages adapter = new RecyclerViewAdapter_Chat_Messages(ChatRoomActivity.this, list_messages);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChatRoomActivity.this);
                recyclerview_chat_messages.setLayoutManager(layoutManager);
                recyclerview_chat_messages.setAdapter(adapter);
                recyclerview_chat_messages.setNestedScrollingEnabled(false);
                recyclerview_chat_messages.scrollToPosition(list_messages.size() - 1);
            }

        };


        //서버에 접속
        client = new SocketClient(ip, port);
        client.start();

        //메세지를 서버에 전송하는  버튼
        btnSend.setOnClickListener(new View.OnClickListener() {
            final String message = editMessage.getText().toString();

            @Override
            public void onClick(View view) {
                //예를 들어 서버에게 '오전 02:34' 이라는 형식으로 전송 시간을 보내준다.
                today = new Date();
                format = new SimpleDateFormat("a hh:mm");

                if (message != null || message.equals("")) {


                    //true이면 1 false 이면 0
                    System.out.println("이건 유알엘 값이 맞나요 아닌가요 : " + Patterns.WEB_URL.matcher(message).matches());
                    if (imageList.size() == 0) {
                        //내가 보낸 메세지는 서버를 거치지 않고 바로 받아 볼 수 있도록 한다.
                        String img_url = "";
                        item_chat_messages = new Item_Chat_Room_List();
                        item_chat_messages.room_num = room_num;
                        item_chat_messages.me = 1;
                        item_chat_messages.img_in = 0;
                        item_chat_messages.img = null;
                        item_chat_messages.history = format.format(today);
                        item_chat_messages.message = editMessage.getText().toString();
                        item_chat_messages.username = username;
                        item_chat_messages.url = url;

                        //메세지가 웹의 url 값이라면
                        if (extractUrlParts(editMessage.getText().toString()) == true) {
                            item_chat_messages.link_url_in = 1;
                        } else {
                            item_chat_messages.link_url_in = 0;
                        }
                        if (editMessage.getText().toString().isEmpty() || editMessage.getText().toString().equals("null") || editMessage.getText().toString() == null) {
                            item_chat_messages.message_in = 0;
                        } else {
                            item_chat_messages.message_in = 1;
                        }


                        send = new SendThread(socket, img_url, format.format(today));

                        send.start();

                        //sqlite에 넣어준다.

                        //내가 메세지 전송 후 접속되어 있지 않은 친구들에게 알림을 준다.
                        //  send_notification(item_chat_messages.room_num,  item_chat_messages.username ,   item_chat_messages.url,editMessage.getText().toString(), "null",  item_chat_messages.history, 0,0,item_chat_messages.img_in ,item_chat_messages.message_in,item_chat_messages.link_url_in);
                        send_notification(item_chat_messages.room_num, item_chat_messages.username, item_chat_messages.url, editMessage.getText().toString(), img_url, item_chat_messages.history, 0, 0, item_chat_messages.message_in, item_chat_messages.link_url_in);

                        myDB.addData(item_chat_messages);
                        list_messages = myDB.getAllMessages(room_num);


                        editMessage.setText("");


                        //여기에 어댑터를 넣으니깐 내 메세지가 서버와 연결이 끊기고도 올라간다.
                        RecyclerViewAdapter_Chat_Messages adapter = new RecyclerViewAdapter_Chat_Messages(ChatRoomActivity.this, list_messages);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChatRoomActivity.this);
                        recyclerview_chat_messages.setLayoutManager(layoutManager);
                        recyclerview_chat_messages.setAdapter(adapter);
                        recyclerview_chat_messages.setNestedScrollingEnabled(false);
                        //메세지가 전송될 때 마다 리사이클러뷰의 스크롤은 제일 아랫 줄로 내려간다.
                        recyclerview_chat_messages.scrollToPosition(list_messages.size() - 1);
                    } else {
                        //이미지가 있는 경우라면 현재시간을 구해서 url 값으로 만들어준다.
                        //url값을 만들면 서버에 이미지 파일과 url 값을 보낸다.
                        //서버에서는 이미지를 업로드 하고,
                        //채팅할 땐 url값만 넘겨 picasso를 이용하여 클라이언트 쪽에서 이미지를 그리도록 한다.


                        //서버에 이미지 전송
                        final MyCommand myCommand = new MyCommand(getApplicationContext());
                        //이미지 따로 보내기
                        for (int i = 0; i < imageList.size(); i++) {
                            long currentTime = System.currentTimeMillis();
                            //클라이언트가 먼저 채팅방에 보낼 이미지의 url 값을 만들어 서버에 전달한다.
                            //채팅을 할 때 다른 클라이언트에겐 이 url 값만 전달한다.
                            String img_url = "http://13.125.216.157/uploads/" + currentTime + ".jpeg";

                            try {

                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageList.get(i));


                                String encodedString = imageToString(bitmap);

                                StringRequest stringRequest_image = new StringRequest(Request.Method.POST, CHAT_IMAGE_UPLOAD,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {


                                                //사진의 갯수가 n개 이고 서버로부터 받아오는 response 값이 n과 같은 경우일 때 메인화면으로 화면 전환이 이루어진다.
                                                //이렇게 처리하지 않으면 사진을 서버로 옮길 때 마다 화면 전환이 이루어짐.
                                                if (response.contains("1")) {
                                                    //내가 보낸 메세지는 서버를 거치지 않고 바로 받아 볼 수 있도록 한다.
                                                    Toast.makeText(ChatRoomActivity.this, "done", Toast.LENGTH_SHORT).show();
                                                    item_chat_messages = new Item_Chat_Room_List();
                                                    item_chat_messages.room_num = room_num;
                                                    item_chat_messages.history = format.format(today);
                                                    item_chat_messages.me = 1;
                                                    item_chat_messages.img_in = 1;
                                                    // item_chat_messages.message = editMessage.getText().toString();
                                                    item_chat_messages.username = username;
                                                    item_chat_messages.url = url;
                                                    //클라이언트에서 이미지 url 값을 먼저 만들어 준다.
                                                    item_chat_messages.img = img_url;

                                                    if (editMessage.getText().toString().isEmpty() || editMessage.getText().toString().equals("null") || editMessage.getText().toString() == null) {
                                                        item_chat_messages.message_in = 0;
                                                        item_chat_messages.message = "";
                                                    } else {
                                                        item_chat_messages.message_in = 1;
                                                        item_chat_messages.message = editMessage.getText().toString();
                                                    }

                                                    send = new SendThread(socket, img_url, format.format(today));

                                                    send.start();

                                                    // list_messages.add(item_chat_messages);

                                                    //메세지들을 sqlite에 넣어준다.
                                                    myDB.addData(item_chat_messages);
                                                    list_messages = myDB.getAllMessages(room_num);


                                                    System.out.println("보낸 메세지는 : " + editMessage.getText().toString());

                                                    editMessage.setText("");

                                                    //내가 메세지 전송 후 접속되어 있지 않은 친구들에게 알림을 준다.
                                                    //send_notification(room_num, username, url, editMessage.getText().toString(), img_url,  item_chat_messages.history, 0,0,1,item_chat_messages.message_in,0);
                                                    send_notification(room_num, username, url, "", img_url, format.format(today), 0, 1, item_chat_messages.message_in, 0);

                                                    //여기에 어댑터를 넣으니깐 내 메세지가 서버와 연결이 끊기고도 올라간다.
                                                    RecyclerViewAdapter_Chat_Messages adapter = new RecyclerViewAdapter_Chat_Messages(ChatRoomActivity.this, list_messages);
                                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChatRoomActivity.this);
                                                    recyclerview_chat_messages.setLayoutManager(layoutManager);
                                                    recyclerview_chat_messages.setAdapter(adapter);
                                                    recyclerview_chat_messages.setNestedScrollingEnabled(false);
                                                    //메세지가 전송될 때 마다 리사이클러뷰의 스크롤은 제일 아랫 줄로 내려간다.
                                                    recyclerview_chat_messages.scrollToPosition(list_messages.size() - 1);

                                                } else {
                                                    Toast.makeText(ChatRoomActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                                }


                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(ChatRoomActivity.this, "Error while uploading image", Toast.LENGTH_LONG).show();
                                            }


                                        }) {
                                    @Override
                                    protected Map<String, String> getParams() {

                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("image", encodedString);
                                        params.put("img_url", "uploads/" + currentTime + ".jpeg");


                                        return params;

                                    }
                                };

                                myCommand.add(stringRequest_image);


                            } catch (FileNotFoundException e) {
                                Toast.makeText(ChatRoomActivity.this, "Error while loading image", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //모두 전송 되었으면 이미지 경로를 담은 arraylist를 비우고 이미지 미리 보기 레이아웃은 없어진다.
                            if (i == imageList.size() - 1) {
                                imageList.removeAll(imageList);
                                //모두 전송 후엔 linearMain이 담고 있는 이미지뷰들을 모두 삭제해주어야 한다.
                                linearMain.removeAllViews();
                                linearMain.setVisibility(View.GONE);


                            }
                        }

                        myCommand.execute();
                    }
                }
            }
        });
    }

    //내부 클래스
    class SocketClient extends Thread {
        boolean threadAlive; //스레드 동작여부.
        String ip;
        String port;
        // int room_num;


        OutputStream outputStream = null;
        BufferedReader br = null;
        DataOutputStream output = null;

        public SocketClient(String ip, String port) {
            threadAlive = true;
            this.ip = ip;
            this.port = port;

        }

        public void run() {
            try {
                //채팅서버에 접속
                socket = new Socket(ip, Integer.parseInt(port));
                //서버에 메세지를 전달하기 위한 스트림 생성
                output = new DataOutputStream(socket.getOutputStream());
                //메세지 수신용 스레드 생성
                receive = new ReceiveThread(socket);
                receive.start();
                //와이피아 정보 관리자 객체로부터 폰의 mac address를 가져와서 채팅서버에 전달
                WifiManager mng = (WifiManager) context.getSystemService(WIFI_SERVICE);
                WifiInfo info = mng.getConnectionInfo();
                //식별자로 mac 주소를 넣어줌.
                // mac = info.getMacAddress();
                //식별자로 사용자 닉네임 값을 넣어줌.
                intent = getIntent();
                username = intent.getStringExtra("username");
                room_num = intent.getIntExtra("room_num", 0);
                url = intent.getStringExtra("url");
                // String message = editMessage.getText().toString();
                //서버에 유저일므을 보낸다.
                output.writeUTF(username);
                output.writeUTF(String.valueOf(room_num));
                output.writeUTF(url);
                //  output.writeUTF(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //내부클래스, 서버에서 받은 메세지를 핸들러에 보내줌.
    class ReceiveThread extends Thread {
        Socket socket = null;
        DataInputStream input = null;

        public ReceiveThread(Socket socket) {
            this.socket = socket;
            try {
                //
                input = new DataInputStream(socket.getInputStream());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {

                //input스트림이 존재한다면
                while (input != null) {
                    //처음 대화방이 열릴 때 메세지가 하나라도 생겨야 상대방은 대화방이 만들어짐.
                    updateCreatedState();
                    //채팅 서버로부터 받은 메세지
                    String username2 = input.readUTF();
                    String url = input.readUTF();
                    String msg = input.readUTF();
                    String img_url = input.readUTF();
                    String history = input.readUTF();
                    System.out.println("받은 시간은 : " + history);

                    //서버를 거쳐 들어오는 값들은 내가 보낸 메세지가 아닌 것들만 들어온다.
                    item_chat_messages = new Item_Chat_Room_List();

                    if (!username2.equals(username)) {
                        item_chat_messages.message = msg;
                        item_chat_messages.room_num = room_num;
                        item_chat_messages.username = username2;
                        item_chat_messages.url = url;
                        item_chat_messages.img = img_url;
                        item_chat_messages.history = history;
                        if (extractUrlParts(msg) == true) {
                            item_chat_messages.link_url_in = 1;
                        } else {
                            item_chat_messages.link_url_in = 0;
                        }

                        if (img_url.isEmpty()) {
                            item_chat_messages.img_in = 0;
                        } else {
                            item_chat_messages.img_in = 1;
                        }

                        if (msg.isEmpty() || msg.equals("null") || msg == null) {
                            item_chat_messages.message_in = 0;
                        } else {
                            item_chat_messages.message_in = 1;
                        }
                        System.out.println("이미지 유알엘 값은 : " + item_chat_messages.img);
                        System.out.println("이미지 유알엘 값은 : " + item_chat_messages.img.isEmpty());

                        if (username2.isEmpty() && url.isEmpty()) {
                            item_chat_messages.none = 1;
                        }
                        // list_messages.add(item_chat_messages);


                        //sqlite에 넣어준다.
                        myDB.addData(item_chat_messages);
                        list_messages = myDB.getAllMessages(room_num);

                    }
                    //메세지가 널 값이 아니라면 핸들러에 보낸다.

                    if (msg != null && username2 != null && url != null) {
                        //핸들러에게 전달할 메세지 객체
                        Message hdmsg = msgHandler.obtainMessage();
                        Message hdmsg2 = msgHandler.obtainMessage();
                        Message hdmsg3 = msgHandler.obtainMessage();
                        Message hdmsg_img_url = msgHandler.obtainMessage();
                        Message hdmsg_history = msgHandler.obtainMessage();
                        //what은 식별자를 의미
                        hdmsg.what = 1111; //유저이름 식별자
                        hdmsg2.what = 2222; //프로필 이미지 식별자
                        hdmsg3.what = 3333; //메세지 식별자
                        hdmsg_img_url.what = 4444; //이미지 식별자
                        hdmsg_history.what = 5555; //전송시간 식별자
                        //obj는 본문을 의미
                        hdmsg.obj = msg; //메시지의 본문
                        msgHandler.sendMessage(hdmsg);
                        hdmsg2.obj = username2;
                        msgHandler.sendMessage(hdmsg2);
                        hdmsg3.obj = url;
                        msgHandler.sendMessage(hdmsg3);
                        hdmsg_img_url.obj = img_url;
                        msgHandler.sendMessage(hdmsg_img_url);
                        hdmsg_history.obj = img_url;
                        msgHandler.sendMessage(hdmsg_history);

                        //핸들러에게 메세지 전달(화면 변경 요청)

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //내부 클래스, 폰에서 작성한 메세지를 서버에 보내주는 역할
    class SendThread extends Thread {

        Socket socket;
        //사용자가 작성한 메세지
        String sendmsg = editMessage.getText().toString();
        String img_url;
        String history;

        DataOutputStream output;


        public SendThread(Socket socket, String img_url, String history) {
            this.socket = socket;
            this.img_url = img_url;
            this.history = history;
            try {
                //채팅서버로 메세지를 보내기 위한 스트림 생성.
                output = new DataOutputStream(socket.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //여기서 서버로 보냄
        public void run() {
            try {
                if (output != null) {
                    if (sendmsg != null) {
                        //채팅서버에 메세지 전달
                        output.writeUTF(sendmsg);
                        output.writeUTF(img_url);
                        output.writeUTF(history);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //방이 새로 생성되었을 경우, 메세지를 입력하고 성공적으로 전송되었다면 상대방도 채팅방이 만들어지도록 한다.
    public void updateCreatedState() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_CREATED_STATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //1:1채팅 하기 버튼을 누르고 서버에 정상적으로 채팅방 번호 값이 생성되었다면 1 이라는 코드를 받게 된다.
                        if (response.contains("1")) {

                        } else {
                            //Toast.makeText(ChatRoomActivity.this, "다시 시도해주세요.", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatRoomActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("room_num", String.valueOf(room_num));

                return params;

            }
        };

        MySingleton.getInstance(ChatRoomActivity.this).addToRequestque(stringRequest);

    }


    //방이 새로 생성되었을 경우, 메세지를 입력하고 성공적으로 전송되었다면 상대방도 채팅방이 만들어지도록 한다.
    // public void send_notification(int room_num,String username,String url,String message,String img,String history,int me,int none,int img_in,int message_in,int link_url_in) {
    public void send_notification(int room_num, String username, String url, String message, String img, String history, int me, int img_in, int message_in, int link_url_in) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEND_NOTIFICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //1:1채팅 하기 버튼을 누르고 서버에 정상적으로 채팅방 번호 값이 생성되었다면 1 이라는 코드를 받게 된다.
                        if (response.contains("1")) {

                        } else {
                            //Toast.makeText(ChatRoomActivity.this, "다시 시도해주세요.", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatRoomActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                System.out.println("방번호 값은 " + room_num);
                System.out.println("유저네임 값은 " + username);
                System.out.println("프로필 값은 " + url);
                System.out.println("메세지 값은 " + message);
                System.out.println("이미지 값은 " + img);
                System.out.println("작성시간 값은 " + history);
                System.out.println("내 건가 값은 " + me);
                System.out.println("이미지가 있나 값은 " + img_in);
                System.out.println("메세지가 있나 값은 " + message_in);
                System.out.println("링크가 있나 값은 " + link_url_in);

                params.put("room_num", String.valueOf(room_num));
                params.put("username", username);
                params.put("url", url);
                params.put("message", message);
                params.put("img", img);
                params.put("history", history);
                params.put("me", String.valueOf(me));
                // params.put("none", String.valueOf(none));
                params.put("img_in", String.valueOf(img_in));
                params.put("message_in", String.valueOf(message_in));
                params.put("link_url_in", String.valueOf(link_url_in));

                return params;

            }
        };

        MySingleton.getInstance(ChatRoomActivity.this).addToRequestque(stringRequest);

    }

    //현재 채팅방이 프로젝트 방이고, 추가모집중인지 아닌지 확인하기
    public void checkProjectStateRecruiting() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CHECK_PROJECT_STATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //현재 추가모집중인 프로젝트라면 '추가모집 취소'버튼이 보이도록 한다.
                        if (response.contains("1")) {
                            recruit.setVisibility(View.GONE);
                            recruit_cancel.setVisibility(View.VISIBLE);
                        } else if (response.contains("2")) {
                            recruit.setVisibility(View.VISIBLE);
                            recruit_cancel.setVisibility(View.GONE);

                        } else {
                            Toast.makeText(ChatRoomActivity.this, "서버오류.", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatRoomActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("project_id", String.valueOf(project_id));

                return params;

            }
        };

        MySingleton.getInstance(ChatRoomActivity.this).addToRequestque(stringRequest);
    }

    //현재 채팅방이 완료된 프로젝트 방인지 확인하기. 완료된 프로젝트 방이라면 추가모집,추가모집취소,프로젝트완료,프로젝트 그만두기, 디데이 버튼이 안보이도록 처리
    //프로젝트 오픈소스 url값만 보이도록 하고 링크처리.
    public void checkProjectStateCompleted() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CHECK_PROJECT_STATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //현재 추가모집중인 프로젝트라면 '추가모집 취소'버튼이 보이도록 한다.
                        if (response.contains("1")) {
                            Toast.makeText(ChatRoomActivity.this, "완료된 프로젝트 ", Toast.LENGTH_LONG).show();
                            //추가모집 버튼, 추가모집 취소버튼,프로젝트 그만두기,프로젝트 완료버튼 안보이게 처리하기.
                            recruit_cancel.setVisibility(View.GONE);
                            recruit.setVisibility(View.GONE);
                            project_quit.setVisibility(View.GONE);
                            project_done.setVisibility(View.GONE);
                        } else if (response.contains("2")) {
                            // Toast.makeText(ChatRoomActivity.this, "프로젝트 완료 여부 불러오기 실패", Toast.LENGTH_LONG).show();


                        } else {
                            Toast.makeText(ChatRoomActivity.this, "서버오류.", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatRoomActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("project_id_completed", String.valueOf(project_id));

                return params;

            }
        };

        MySingleton.getInstance(ChatRoomActivity.this).addToRequestque(stringRequest);
    }

    //채팅멤버 불러오기
    public void uploadChatMember() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CHAT_MEMBER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("chat_member");
                            // JSONObject data = jsonarray.getJSONObject(0);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                ListItem_Check_Like_Member item = new ListItem_Check_Like_Member();

                                item.username = object.getString("username");
                                item.url = object.getString("profile_img");


                                listItems.add(item);


                            }

                            RecyclerViewAdapter_Show_Chatting_Member adapter = new RecyclerViewAdapter_Show_Chatting_Member(ChatRoomActivity.this, listItems);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChatRoomActivity.this);
                            recyclerview_chat_member.setLayoutManager(layoutManager);
                            recyclerview_chat_member.setAdapter(adapter);
                            //nestedscrollview가 매끄럽게 움직이게 해줌.
                            recyclerview_chat_member.setNestedScrollingEnabled(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatRoomActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                System.out.println("방 번호는 : " + room_num);

                params.put("room_num", String.valueOf(room_num));

                return params;

            }
        };

        MySingleton.getInstance(ChatRoomActivity.this).addToRequestque(stringRequest);

    }

    //프로젝트 완료 버튼을 누르면 해당 프로젝트 게시글이 '프로젝트 완료' 상태로 변경되고
    //홈화면의 프로젝트완료 탭으로 게시글을 옮긴다.
    public void openDialog_Project_Completed() {
        Dialog_Project_Url dialog = new Dialog_Project_Url();
        dialog.show(getSupportFragmentManager(), "Dialog");
    }


    //프로젝트 추가 모집시, 모집할 인원수를 입력할 다이얼로그 열기
    public void openDialog() {
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(), "Dialog");

    }

    //메뉴버튼 메서드
    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerlayout);
        toolbar = findViewById(R.id.toolBar_info);
        toolbar.setTitle("채팅방");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        //toolbar.setBackgroundColor(0xFFFFFFFF);


        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboardFrom(context, drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        }; // Drawer Toggle Object Made
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });

    }


    //친구초대 버튼을 누르면 친구목록을 띄워주는 화면으로 넘어간다.
    public void toFriendList() {
        Intent intent = new Intent(this, InvitingChatFriendListActivity.class);
        //방번호를 넘겨준다.
        intent.putExtra("room_num", room_num);
        startActivity(intent);
    }


    //추가모집 취소할 것인지 물어보는 다이얼로그
    public void cancelRecruiting() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ChatRoomActivity.this);
        builder.setTitle("확인메세지");
        builder.setMessage("추가모집을 취소하시겠습니까?");
        builder.setPositiveButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setNegativeButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_PROJECT_STATE,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        //현재 추가모집중인 프로젝트라면 '추가모집 취소'버튼이 보이도록 한다.
                                        if (response.contains("1")) {
                                            Toast.makeText(ChatRoomActivity.this, "추가모집 취소", Toast.LENGTH_SHORT).show();
                                            recruit.setVisibility(View.VISIBLE);
                                            recruit_cancel.setVisibility(View.GONE);
                                        } else if (response.contains("2")) {
                                            Toast.makeText(ChatRoomActivity.this, "실패", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(ChatRoomActivity.this, "서버오류.", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(ChatRoomActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                    }


                                }) {
                            @Override
                            protected Map<String, String> getParams() {

                                Map<String, String> params = new HashMap<String, String>();

                                params.put("project_id_cancel", String.valueOf(project_id));
                                params.put("history", getTime());

                                return params;

                            }
                        };

                        MySingleton.getInstance(ChatRoomActivity.this).addToRequestque(stringRequest);

                    }
                });
        builder.show();
    }

    public void dialog_change_room_title() {
        Dialog_Change_Room_Name dialog_change_room_name = new Dialog_Change_Room_Name();

        dialog_change_room_name.show(getSupportFragmentManager(), "Dialog");
    }

    //현재시간 구하기
    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //친구 초대하기
            case R.id.invite_line:
                toFriendList();
                break;

            //이미지 및 기타 파일 추가하기 버튼
            case R.id.add:
                add_menu.setVisibility(View.VISIBLE);
                add.setVisibility(View.GONE);
                cancel.setVisibility(View.VISIBLE);
                hideKeyboardFrom(context, view);
                break;

            //위에서 누른거 취소버튼
            case R.id.cancel:
                add_menu.setVisibility(View.GONE);
                add.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.GONE);
                break;

            //앨범선택
            case R.id.album:
                selectImage();
                break;

            //프로젝트 추가 모집 버튼
            case R.id.recruit:
                openDialog();
                break;

            //프로젝트 완료 버튼
            case R.id.project_done:
                openDialog_Project_Completed();
                break;

            //프로젝트 추가모집 취소
            case R.id.recruit_cancel:
                cancelRecruiting();
                break;

            //영상통화 하기
            case R.id.streaming:
                Intent intent = new Intent(ChatRoomActivity.this, ConnectActivity.class);
                intent.putExtra("room_num", "chat_room_num_is" + room_num);
                startActivity(intent);
                break;

            //방제목 변경하기
            case R.id.room_title_change:
                dialog_change_room_title();
                break;

        }
    }

    //갤러리 불러오기
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(Intent.createChooser(intent, "다중 선택은 '포토'를 선택하세요."), GALLERY_REQUEST);
    }

    //비트맵을 jpg파일로 바꿈
    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            //사진을 받아오면 사진추가 메뉴를 안 보이게 한다. 그리고 메세지 옆에 있는 추가 버튼이 보이도록 한다.
            add_menu.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.GONE);

            //이미지를 전송한 후에 linearMain을 gone처리 해주었기 때문에 새로운 사진을 가지고 올 땐 linearMain을 visible로 바꾸어 주어야 한다.
            linearMain.setVisibility(View.VISIBLE);


            int count = data.getClipData().getItemCount();

            for (int i = 0; i < count; i++) {
                Uri path = data.getClipData().getItemAt(i).getUri();
                //갤러리에서 불러온 여러 이미지의 경로를 가지고옴. getClipData().getItemAt(i).getUri() 메서드를 반드시 써줘야함.
                //그냥 getPath()는 이미지 한개만 불러옴.


                imageList.add(path);


                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);

                    ImageView imageView = new ImageView(getApplicationContext());


                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setPadding(10, 10, 10, 10);
                    imageView.setMaxWidth(150);
                    imageView.setMaxHeight(200);
                    imageView.setImageBitmap(bitmap);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    //앨범에서 고른 사진의 갯수만큼 라이니어레이아웃에서 이미지뷰를 추가한다.

                    linearMain.addView(imageView);


                    //이미지를 클릭하면 해당 이미지는 삭제됨.
                    /*심각한 문제가 발생..
                    * 갤러리에서 불러온 이미지를 가져오고 나서 삭제를 하게 되면 index문제가 발생.
                    * 5개의 이미지가 있다면, [0]번째 이미지를 삭제 후 인덱스의 변화가 생기지 않음.지워진 이미지의 인덱스는 그대로 남아있게 됨.
                    * */

                    int finalI = i;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //null값을 줘야 화면에서도 이미지가 사라짐.
                            imageView.setImageBitmap(null);
                            //remove를 해주어야 이미지를 담고 있는 imageList에서도 이미지가 삭제되어 서버에 전송이 되지 않는다.
                            System.out.println("사진의 i 값은 : " + finalI);

                            imageList.remove(finalI);
                            imageView.setVisibility(View.GONE);

                        }
                    });

                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Error while loading image", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

    //기존의 메세지 내용을 불러온다.
    private void refreshData() {

        list_messages = myDB.getAllMessages(room_num);
        adapter = new RecyclerViewAdapter_Chat_Messages(ChatRoomActivity.this, list_messages);
        layoutManager = new LinearLayoutManager(ChatRoomActivity.this);
        recyclerview_chat_messages.setLayoutManager(layoutManager);
        recyclerview_chat_messages.setAdapter(adapter);
        recyclerview_chat_messages.setNestedScrollingEnabled(false);
        //메세지가 전송될 때 마다 리사이클러뷰의 스크롤은 제일 아랫 줄로 내려간다.
        recyclerview_chat_messages.scrollToPosition(list_messages.size() - 1);
    }


    // 올려져 있는 키보드를 내린다.
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    //채팅방에 접속해 있지 않을 때 접속 상태를 '0'으로 만들어 준다.
    public void makeStateZero() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_CHAT_STATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        //사진의 갯수가 n개 이고 서버로부터 받아오는 response 값이 n과 같은 경우일 때 메인화면으로 화면 전환이 이루어진다.
                        //이렇게 처리하지 않으면 사진을 서버로 옮길 때 마다 화면 전환이 이루어짐.
                        if (response.contains("1")) {
                            //내가 보낸 메세지는 서버를 거치지 않고 바로 받아 볼 수 있도록 한다.


                        } else {
                            Toast.makeText(ChatRoomActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatRoomActivity.this, "Error while uploading image", Toast.LENGTH_LONG).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("room_num", String.valueOf(room_num));
                params.put("email_out", loadLoginEmail());


                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //채팅방에 접속하면 접속 상태를 '1'으로 만들어 준다.
    public void makeStateOne() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_CHAT_STATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        //사진의 갯수가 n개 이고 서버로부터 받아오는 response 값이 n과 같은 경우일 때 메인화면으로 화면 전환이 이루어진다.
                        //이렇게 처리하지 않으면 사진을 서버로 옮길 때 마다 화면 전환이 이루어짐.
                        if (response.contains("1")) {
                            //내가 보낸 메세지는 서버를 거치지 않고 바로 받아 볼 수 있도록 한다.


                        } else {
                            Toast.makeText(ChatRoomActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatRoomActivity.this, "Error while uploading image", Toast.LENGTH_LONG).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("room_num", String.valueOf(room_num));
                params.put("email_in", loadLoginEmail());


                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //onPuase상태일 때 채팅방에 접속해 있지 않은 상태로 만든다. DB에 접속상태 0으로 변경
   /* @Override
    protected void onPause() {
        super.onPause();
        System.out.println("디스토리이 되었습니다.");
        makeStateZero();
        System.out.println("접속상태가 0이 되었습니다. ");
    }*/


    //홈버튼을 눌렀을 때 채팅방 접속 상태를 0으로 만들어 주어서 fcm 알림을 받을 수 있도록 한다.
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
//여기서 감지
        makeStateZero();


    }


    //뒤로가기 버튼을 누르면 채팅방 액티비티 종료 시키기.
    //종료가 되면 채팅방 접속상태를 0 으로 만들어 fcm을 받을 수 있도록 한다.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Execute your code here
        makeStateOne();
        // finish();
        // makeStateZero();
        // makeStateOne();
        System.out.println("뒤로 가기 버튼을 눌렀습니다.");
        System.out.println("접속상태가 1이 되었습니다. ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("다시 시작 되었습니다.");
        makeStateOne();
        System.out.println("접속상태가 1이 되었습니다. ");
    }

    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println(email_from_login);


        return email_from_login;
    }
}