package com.example.goo.test.Activity.MyInfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.goo.test.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

/**
 * Created by Goo on 2018-07-10.
 */

public class Profile_Image_Preview extends AppCompatActivity implements View.OnClickListener {
    private ImageView profile_image;
    private RelativeLayout relative_background;
    Button btn_save;
    Bitmap bitmap;
    Intent intent;
    String path;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image_preview);
        profile_image = findViewById(R.id.profile_image);
        relative_background = findViewById(R.id.relative_background);
        btn_save = findViewById(R.id.btn_save);

        //opencv에서 캡쳐한 이미지를 인텐트로 받아낸다.
        //이미지 경로를 파일 이름으로 받아 냈고,
        // 파일을 이미지뷰에 그려주기 위해서는 아래와 같은 방식으로 코드를 작성해주어야 한다.
        intent = getIntent();
        path = intent.getStringExtra("uri");
        System.out.println("ㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂ"+path);

            bitmap = BitmapFactory.decodeFile(path);
            profile_image.setImageBitmap(bitmap);


            //배경 클릭시 저장버튼 보이게 하기
            relative_background.setOnClickListener(this);
            //이미지 저장
        btn_save.setOnClickListener(this);

        // Picasso.with(this).load(Uri.parse(intent.getStringExtra("uri"))).into(profile_image);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_save :
                //이미지 저장하기
                File pictureFile = (File)intent.getExtras().get("file");
               try {
                   Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                   mediaScanIntent.setData(Uri.fromFile(pictureFile));
                   sendBroadcast(mediaScanIntent);
                   Toast.makeText(Profile_Image_Preview.this, "프로필 이미지가 저장 되었습니다.", Toast.LENGTH_SHORT).show();

                   Intent img_intent = new Intent(Profile_Image_Preview.this,ProfileActivity.class);
                   intent = getIntent();
                   path = intent.getStringExtra("uri");
                   img_intent.putExtra("uri", path);
                   //로그인화면에 이메일 값을 보내줌.
                   startActivity(img_intent);
                   finish();
               }catch (IllegalArgumentException  e) {
                   e.printStackTrace();
               }
                break;

            case R.id.relative_background :
            //배경화면을 클릭했을 때, 저장버튼이 있다면 저장버튼을 안보이도록하고, 저장버튼이 안보인다면 보이도록 처리한다.

                if(btn_save.getVisibility() == View.VISIBLE){
                    btn_save.setVisibility(View.INVISIBLE);
                }else if(btn_save.getVisibility() == View.GONE){
                    btn_save.setVisibility(View.VISIBLE);
                }else if(btn_save.getVisibility() == View.INVISIBLE){
                    btn_save.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
