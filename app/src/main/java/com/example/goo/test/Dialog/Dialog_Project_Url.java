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

import static com.example.goo.test.Activity.Chat.ChatRoomActivity.project_done;
import static com.example.goo.test.Activity.Chat.ChatRoomActivity.project_quit;
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

public class Dialog_Project_Url extends AppCompatDialogFragment {
    private EditText project_url,token_account;
    private TextView up, down;
    private int member_count;


    //안드로이드 현재시간 구하기
    long mNow;
    Date mDate;

    //시간에  대문자 HH를 넣어줘야 24시간 형식으로 출력됨.
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    ChatRoomActivity chatRoomActivity;
    public int project_id_update;
    //프로젝트 모집인원 추가가 성공적으로 이루어지면 '추가모집 취소'버튼으로 전환시켜 준다.
  //  private TextView recruit_cancel;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_project_completed, null);
        chatRoomActivity = new ChatRoomActivity();
        project_id_update = chatRoomActivity.project_id;
        project_url = view.findViewById(R.id.project_url);
        token_account = view.findViewById(R.id.token_account);


        builder.setView(view)
                .setTitle("프로젝트 완료하기")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //서버에 오픈소스 url을 보낸다.


                        updateProjectStateCompleted(project_url.getText().toString(),token_account.getText().toString(), project_id_update, builder);

                    }
                });

        return builder.create();

    }


    //Toast메세지가 그냥 getActivity()로는 뜨지 않았는데, builder.getcontext()를 하니깐 뜸.
    public void updateProjectStateCompleted(String url,String account, int project_id, AlertDialog.Builder builder) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_PROJECT_STATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        //사진의 갯수가 n개 이고 서버로부터 받아오는 response 값이 n과 같은 경우일 때 메인화면으로 화면 전환이 이루어진다.
                        //이렇게 처리하지 않으면 사진을 서버로 옮길 때 마다 화면 전환이 이루어짐.
                        if (response.contains("1")) {

                            Toast.makeText(builder.getContext(), "success", Toast.LENGTH_SHORT).show();
                            //추가모집 버튼, 추가모집 취소버튼,프로젝트 그만두기,프로젝트 완료버튼 안보이게 처리하기.
                            recruit_cancel.setVisibility(View.GONE);
                            recruit.setVisibility(View.GONE);
                            project_quit.setVisibility(View.GONE);
                            project_done.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(builder.getContext(), "failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getView().getContext(), "에러", Toast.LENGTH_LONG).show();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("project_id_done", String.valueOf(project_id));
                params.put("project_url", url);
                params.put("history", getTime());
                params.put("account", account);

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    //현재시간 구하기
    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}
