package com.example.goo.test.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.goo.test.Item.Item_Chat_Room_List;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Goo on 2018-06-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VER = 2;
    public static final String DATABASE_NAME = "chat_2";
    public static final String TABLE_NAME = "chat2";
    public static final String ID = "ID";
    public static final String ROOM_NUM = "ROOM_NUM";
    public static final String USERNAME = "USERNAME";
    public static final String PROFILE = "PROFILE";
    public static final String MESSAGE = "MESSAGE";
    public static final String IMG_URL = "IMG_URL";
    public static final String HISTORY = "HISTORY";
    public static final String ME = "ME";
    public static final String NONE = "NONE";
    public static final String IMAGE_IN = "IMAGE_IN";
    public static final String MESSAGE_IN = "MESSAGE_IN";
    public static final String LINK_URL_IN = "LINK_URL_IN";
    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VER);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME
                +" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +ROOM_NUM+
                " INTEGER,"+USERNAME+" TEXT," + PROFILE + " TEXT," + MESSAGE + " TEXT," + IMG_URL + " TEXT," + HISTORY+ " TEXT," + ME + " INTEGER,"+
                NONE+" INTEGER," + IMAGE_IN + " INTEGER," + MESSAGE_IN + " INTEGER," + LINK_URL_IN + " INTEGER"+ ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "  + TABLE_NAME);
        onCreate(db);
    }

    public void addData(Item_Chat_Room_List chat_message){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ROOM_NUM,chat_message.room_num);
        contentValues.put(USERNAME,chat_message.username);
        contentValues.put(PROFILE,chat_message.url);
        contentValues.put(MESSAGE,chat_message.message);
        contentValues.put(IMG_URL,chat_message.img);
        contentValues.put(HISTORY,chat_message.history);
        contentValues.put(ME,chat_message.me);
        contentValues.put(NONE,chat_message.none);
        contentValues.put(IMAGE_IN,chat_message.img_in);
        contentValues.put(MESSAGE_IN,chat_message.message_in);
        contentValues.put(LINK_URL_IN,chat_message.link_url_in);

        db.insert(TABLE_NAME,null,contentValues);
        db.close();
    }

    public int updateData(Item_Chat_Room_List chat_message){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ROOM_NUM,chat_message.room_num);
        contentValues.put(USERNAME,chat_message.username);
        contentValues.put(PROFILE,chat_message.url);
        contentValues.put(MESSAGE,chat_message.message);
        contentValues.put(IMG_URL,chat_message.img);
        contentValues.put(HISTORY,chat_message.history);
        contentValues.put(ME,chat_message.me);
        contentValues.put(NONE,chat_message.none);
        contentValues.put(IMAGE_IN,chat_message.img_in);
        contentValues.put(MESSAGE_IN,chat_message.message_in);
        contentValues.put(LINK_URL_IN,chat_message.link_url_in);

        return db.update(TABLE_NAME,contentValues,ROOM_NUM+" = ?",new String[]{String.valueOf(chat_message.room_num)});
    }

    public void deleteData(Item_Chat_Room_List chat_message,int room_num){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,ROOM_NUM+" ="+room_num, new String[]{String.valueOf(chat_message.room_num)});
        db.close();
    }

    public Item_Chat_Room_List getMessage(int room_num){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[] {ROOM_NUM,USERNAME,PROFILE,MESSAGE,IMG_URL,HISTORY,ME,NONE,IMAGE_IN,MESSAGE_IN,LINK_URL_IN}
        ,ROOM_NUM+"=?",new String[]{String.valueOf(room_num)},null,null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        return new Item_Chat_Room_List(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)
        ,cursor.getInt(6),cursor.getInt(7),cursor.getInt(8),cursor.getInt(9),cursor.getInt(10));

    }

    public List<Item_Chat_Room_List> getAllMessages(int room_num){
        List<Item_Chat_Room_List> lstPersons = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+ TABLE_NAME +" WHERE room_num = " + room_num;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                Item_Chat_Room_List chat_message = new Item_Chat_Room_List();
                chat_message.room_num=cursor.getInt(1);
                chat_message.username = cursor.getString(2);
                chat_message.url = cursor.getString(3);
                chat_message.message = cursor.getString(4);
                chat_message.img = cursor.getString(5);
                chat_message.history = cursor.getString(6);
                chat_message.me = cursor.getInt(7);
                chat_message.none = cursor.getInt(8);
                chat_message.img_in = cursor.getInt(9);
                chat_message.message_in = cursor.getInt(10);
                chat_message.link_url_in = cursor.getInt(11);
                lstPersons.add(chat_message);
            }
            while(cursor.moveToNext());


        }
        return lstPersons;
    }

    //마지막 메세지 불러오기
    public void selectData(int room_num){
        String sql = "select * from " +TABLE_NAME+ " where room_num = "+room_num+ ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
            int id = result.getInt(0);
            String voca = result.getString(1);
            //Toast.makeText(this, "index= "+id+" voca="+voca, 0).show();
        }
        result.close();
    }


}
