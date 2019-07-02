package com.example.goo.test.etc;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Goo on 2018-04-25.
 */


//회원가입 요청
public class RegisterRequest extends StringRequest {
    final static private String URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/UserRegister.php";
    private Map<String, String> parameters;

    public RegisterRequest(String username, String password, String email, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("username",username);
        parameters.put("password",password);
        parameters.put("email",email);



    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
