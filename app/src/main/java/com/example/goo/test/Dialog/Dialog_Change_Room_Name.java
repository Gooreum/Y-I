package com.example.goo.test.Dialog;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.goo.test.R;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

import static com.example.goo.test.Activity.Chat.ChatRoomActivity.project_id;
import static com.example.goo.test.Activity.Chat.ChatRoomActivity.room_num;


/**
 * Created by Goo on 2018-06-25.
 * 프로젝트 인원을 추가모집 할 수 있다.
 * up버튼을 누르면 인원을 추가할 수 있고
 * dwon 버튼을 누르면 인원을 줄일 수 있다.
 * 확인 버튼을 누르면 서버에 해당 프로젝트 게시글의 모집인원수를 변경시키고,
 * 프로젝트 상태를 '모집중인 프로젝트', '진행중인 프로젝트' 두개의 상태로 활성화 시킨다.
 */

public class Dialog_Change_Room_Name extends AppCompatDialogFragment {
    public String UPDATE_ROOM_NAME = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/get_chat_room_list.php";
    private EditText change_room_name;



    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_room_name, null);

        change_room_name = view.findViewById(R.id.change_room_name);


        builder.setView(view)
                .setTitle("방제목 변경")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //서버에 오픈소스 url을 보낸다.


                        updateProjectStateCompleted(change_room_name.getText().toString(), builder);

                    }
                });

        return builder.create();

    }


    //Toast메세지가 그냥 getActivity()로는 뜨지 않았는데, builder.getcontext()를 하니깐 뜸.
    public void updateProjectStateCompleted(String room_name, AlertDialog.Builder builder) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_ROOM_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        //사진의 갯수가 n개 이고 서버로부터 받아오는 response 값이 n과 같은 경우일 때 메인화면으로 화면 전환이 이루어진다.
                        //이렇게 처리하지 않으면 사진을 서버로 옮길 때 마다 화면 전환이 이루어짐.
                        if (response.contains("1")) {

                            Toast.makeText(builder.getContext(), "success", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(builder.getContext(), "failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(builder.getContext(), "에러", Toast.LENGTH_LONG).show();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("email_change_room_name", loadLoginEmail(builder));
                params.put("room_name", room_name);
                params.put("room_num", String.valueOf(room_num));


                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    //이메일 값 가져오기
    private String loadLoginEmail(AlertDialog.Builder builder) {
        SharedPreferences sp = builder.getContext().getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println("와 이메일 값이 왜 안나와" + email_from_login);

        return email_from_login;
    }
}
