package com.example.goo.test.Util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.goo.test.Activity.Chat.ChatRoomActivity;
import com.example.goo.test.Activity.MainActivity;
import com.example.goo.test.Database.DatabaseHelper;
import com.example.goo.test.Item.Item_Chat_Room_List;
import com.example.goo.test.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    //데이터베이스 선언
    DatabaseHelper myDB;
    Item_Chat_Room_List item_chat_messages;

    public MyFirebaseMessagingService() {
    }
    int NOTIFICATION_ID = 234;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        myDB = new DatabaseHelper(this);
        item_chat_messages = new Item_Chat_Room_List();

        System.out.println("방번호 값은 : " + remoteMessage.getData().get("room_num"));
        System.out.println("유저네임 값은 : " + remoteMessage.getData().get("username"));
        System.out.println("프로필이미지 값은 : " + remoteMessage.getData().get("url"));
        System.out.println("메세지 값은 : " + remoteMessage.getData().get("message"));
        System.out.println("이미지 값은 : " + remoteMessage.getData().get("img"));
        System.out.println("작성시간 값은 : " + remoteMessage.getData().get("history"));
        System.out.println("누가 보냈나 값은 : " + remoteMessage.getData().get("me"));
       // System.out.println("아무 메세지가 없나 값은 : " + remoteMessage.getData().get("none"));
        System.out.println("이미지가 있나 값은 : " + remoteMessage.getData().get("img_in"));
        System.out.println("메세지가 있나 값은 : " + remoteMessage.getData().get("message_in"));
        System.out.println("링크가 걸린 메세지 값은 : " + remoteMessage.getData().get("link_url_in"));

        int room_num = Integer.parseInt(remoteMessage.getData().get("room_num"));
        String username = remoteMessage.getData().get("username");
        String url = remoteMessage.getData().get("url");
        String message_chat = remoteMessage.getData().get("message");
        String img = remoteMessage.getData().get("img");
        String history = remoteMessage.getData().get("history");
        int me = Integer.parseInt(remoteMessage.getData().get("me"));
     //   int none = Integer.parseInt(remoteMessage.getData().get("none"));
        int img_in = Integer.parseInt(remoteMessage.getData().get("img_in"));
        int message_in = Integer.parseInt(remoteMessage.getData().get("message_in"));
        int link_url_in = Integer.parseInt(remoteMessage.getData().get("link_url_in"));


        //fcm으로 받은 채팅 메세지의 데이터를 디비에 저장해준다.

        item_chat_messages.room_num = room_num;
        item_chat_messages.username = username;
        item_chat_messages.url = url;
        item_chat_messages.message = message_chat;
        item_chat_messages.img = img;
        item_chat_messages.history = history;
        item_chat_messages.me = me;
       // item_chat_messages.none = none;
        item_chat_messages.img_in = img_in;
        item_chat_messages.message_in = message_in;
        item_chat_messages.link_url_in = link_url_in;

        myDB.addData(item_chat_messages);

        //sendNotification(title,"님이 메세지를 보냈습니다.");
        showNotification(username,message_chat,img_in,link_url_in,room_num);
    }

    //알림 메세지를 보내주는 메서드
     public void showNotification(String username ,String message,int img_in,int link_url_in,int room_num){
        Intent intent = new Intent(this,ChatRoomActivity.class);
        intent.putExtra("room_num",room_num);
        intent.putExtra("url",getProfileUrl());
        intent.putExtra("username",getUsername());

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle("채팅 메세지");
        builder.setColor(Color.RED);
        //알림 메세지를 보낼 때 일반 메세지인지, 이미지 인지, 링크인지 구분해서 보내준다.
        if(img_in == 0 && link_url_in == 0) {
            builder.setContentText(username + " : " + message);
        }else if(img_in != 0){
            builder.setContentText(username + "님이 사진을 보냈습니다.");
        }else if(img_in == 0 && link_url_in == 1){
            builder.setContentText(username + "님이 링크를 보냈습니다.");
        }
        //builder.setSmallIcon(R.drawable.ic_account_circle_black_24dp);
        builder.setSmallIcon(R.drawable.ic_account_circle_black_24dp);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());

    }



    //로그인 후 저장 해두었던 유저네임 값을 가지고 오는 메서드
    private String getUsername(){
        SharedPreferences sp = getSharedPreferences("profile_info", MODE_PRIVATE);
        String username = sp.getString("username", null);


        return username;
    }

    //로그인 후 저장 해두었던 프로필 이미지 값을 가지고 오는 메서드
    private String getProfileUrl(){
        SharedPreferences sp = getSharedPreferences("profile_info", MODE_PRIVATE);
        String profile_url = sp.getString("profile_url", null);



        return profile_url;
    }
}
