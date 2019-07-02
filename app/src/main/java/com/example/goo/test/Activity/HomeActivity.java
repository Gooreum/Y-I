package com.example.goo.test.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.goo.test.Util.MySingleton;
import com.example.goo.test.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Goo on 2018-04-26.
 */

public class HomeActivity extends AppCompatActivity {
    private TextView Name, Email;
    private ImageView Prof_pic;
    private Button SignOut;
    LoginActivity login = new LoginActivity();


    private static final String REGISTER_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/Home.php";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";


    //public String username, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_new_project);

        Name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        Prof_pic = findViewById(R.id.prof_pic);
       SignOut = findViewById(R.id.bn_logout);


       // loadJson();
        update();
       // login();
    }

 //이메일 값을 보내서 해당 유저의 이름과 이메일 값을 제이슨 형식으로 출력.
    private void update() {
        //username = Name.getText().toString();
       // email = Email.getText().toString();

        Intent intent = getIntent();

        final String email2 = intent.getStringExtra("email");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("member");
                            JSONObject data = jsonarray.getJSONObject(0);

                            String username = data.getString("username");
                            String email = data.getString("email");

                            Name.setText(username);
                            Email.setText(email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();
                parameters.put(KEY_EMAIL, email2);
                return parameters;
            }
        };
        MySingleton.getInstance(HomeActivity.this).addToRequestque(stringRequest);

    }


}
