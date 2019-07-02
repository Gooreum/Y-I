package com.example.goo.test.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.goo.test.Activity.Chat.ChatRoomActivity;
import com.example.goo.test.Activity.MyInfo.ProfileActivity;
import com.example.goo.test.R;
import com.google.android.gms.vision.text.Line;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.goo.test.Activity.Chat.ChatRoomActivity.recruit;
import static com.example.goo.test.Activity.Chat.ChatRoomActivity.recruit_cancel;
import static com.example.goo.test.Activity.Home.Check_Join_Member.UPDATE_PROJECT_STATE;

/**
 * Created by Goo on 2018-07-10.
 */

public class Dialog_Profile_Image_Camera_Or_Gallery extends AppCompatDialogFragment implements View.OnClickListener {

    private LinearLayout line_camera, line_gallery;


    //안드로이드 현재시간 구하기
    long mNow;
    Date mDate;

    //시간에  대문자 HH를 넣어줘야 24시간 형식으로 출력됨.
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_profile_image, null);

        line_camera = view.findViewById(R.id.line_camera);
        line_gallery = view.findViewById(R.id.line_gallery);


        line_camera.setOnClickListener(this);
        line_gallery.setOnClickListener(this);

        builder.setView(view)
                .setTitle("프로필 이미지 변경")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


        return builder.create();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //사진 촬영으로 이미지를 바꾼다면 오픈시브이 화면으로 넘겨준다.
            case R.id.line_camera :

                break;

                //갤러리 화면을 넘겨준다.
            case R.id.line_gallery :

                break;
        }
    }
}
