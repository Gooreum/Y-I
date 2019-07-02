package com.example.goo.test.Dialog;

import android.content.DialogInterface;
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
import com.example.goo.test.Activity.Chat.ChatRoomActivity;
import com.example.goo.test.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.goo.test.Activity.Chat.ChatRoomActivity.recruit;
import static com.example.goo.test.Activity.Chat.ChatRoomActivity.recruit_cancel;
import static com.example.goo.test.Activity.Home.Check_Join_Member.UPDATE_PROJECT_STATE;

/**
 * Created by Goo on 2018-06-25.
 * 프로젝트 인원을 추가모집 할 수 있다.
 * up버튼을 누르면 인원을 추가할 수 있고
 * dwon 버튼을 누르면 인원을 줄일 수 있다.
 * 확인 버튼을 누르면 서버에 해당 프로젝트 게시글의 모집인원수를 변경시키고,
 * 프로젝트 상태를 '모집중인 프로젝트', '진행중인 프로젝트' 두개의 상태로 활성화 시킨다.
 */

public class Dialog  extends AppCompatDialogFragment{
    private EditText num;
    private TextView up,down;
    private int member_count;

    ChatRoomActivity chatRoomActivity ;
    public  int project_id_update;




    //안드로이드 현재시간 구하기
    long mNow;
    Date mDate;

    //시간에  대문자 HH를 넣어줘야 24시간 형식으로 출력됨.
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_recruit,null);
        chatRoomActivity = new ChatRoomActivity();
        project_id_update = chatRoomActivity.project_id;
        num = view.findViewById(R.id.num);
        up = view.findViewById(R.id.up);
        down = view.findViewById(R.id.down);

        //만약 num값이 null이라면 추가모집인원수를 0으로 해준다. 이렇게 하지 않으면
        //null오류가 생기기 때문
        if(num.getText().toString().isEmpty() || num.getText().toString().isEmpty()){
            member_count = 0;
            num.setText(0+"");
        }
        //추가 모집인원수가 0이면 감소버튼을 안보이게 처리.
        if(member_count == 0){
            down.setVisibility(View.INVISIBLE);
        }else{
            down.setVisibility(View.VISIBLE);
        }
        //모집인원수 증가버튼
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //만약 num값이 null이라면 추가모집인원수를 0으로 해준다. 이렇게 하지 않으면
                //null오류가 생기기 때문
                if(num.getText().toString().isEmpty() || num.getText().toString().isEmpty()){
                    member_count = 0;
                    num.setText(0+"");
                }
                if(num.getText().toString() != null || !num.getText().toString().isEmpty()){
                    member_count = Integer.parseInt(num.getText().toString()) + 1;
                    num.setText(member_count+"");

                }



                if(member_count == 0){
                    down.setVisibility(View.INVISIBLE);
                }else{
                    down.setVisibility(View.VISIBLE);
                }

            }
        });


        //모집인원수 감소버튼
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //만약 num값이 null이라면 추가모집인원수를 0으로 해준다. 이렇게 하지 않으면
                //null오류가 생기기 때문
                if(num.getText().toString().isEmpty() || num.getText().toString().isEmpty()){
                    member_count = 0;
                    num.setText(0+"");
                }
                if(num.getText().toString() != null || !num.getText().toString().isEmpty()){
                    member_count = Integer.parseInt(num.getText().toString()) - 1;
                    num.setText(member_count+"");

                }
                if(member_count == 0){
                    down.setVisibility(View.INVISIBLE);
                }else{
                    down.setVisibility(View.VISIBLE);
                }


            }
        });

        builder.setView(view)
                .setTitle("모집인원 추가")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    //서버에 추가모집인원수를 보낸다.
                        System.out.println("추가 모집인원수는 : " + num.getText().toString());
                        System.out.println("프로젝트 아이디는 : " + project_id_update);
                        updateProjectMember(member_count,project_id_update,builder);
                    }
                });

        return builder.create();

    }

    //서버에 추가모집인원수를 보내고 프로젝트 상태를 '추가모집중' 상태로 변경시켜준다.
    public void updateProjectMember(int member_count,int project_id,AlertDialog.Builder builder){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_PROJECT_STATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        //사진의 갯수가 n개 이고 서버로부터 받아오는 response 값이 n과 같은 경우일 때 메인화면으로 화면 전환이 이루어진다.
                        //이렇게 처리하지 않으면 사진을 서버로 옮길 때 마다 화면 전환이 이루어짐.
                        if (response.contains("1")) {
                            //내가 보낸 메세지는 서버를 거치지 않고 바로 받아 볼 수 있도록 한다.
                            Toast.makeText(builder.getContext(), "추가모집 되었습니다.", Toast.LENGTH_SHORT).show();
                            recruit_cancel.setVisibility(View.VISIBLE);
                            recruit.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(builder.getContext(), "failed", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error while uploading image", Toast.LENGTH_LONG).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("project_id_more_recruting", String.valueOf(project_id));
                params.put("member_count", String.valueOf(member_count));
                params.put("history", getTime());



                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    //현재시간 구하기
    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}
