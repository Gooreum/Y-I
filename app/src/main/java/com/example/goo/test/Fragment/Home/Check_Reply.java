package com.example.goo.test.Fragment.Home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.goo.test.Adapter.MyAdapter;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Project;
import com.example.goo.test.Adapter.RecyclerViewAdapter_Show_Reply;
import com.example.goo.test.Item.Item;
import com.example.goo.test.Item.ListItem_Reply;
import com.example.goo.test.Item.ListItem_Show_Project;
import com.example.goo.test.R;
import com.example.goo.test.Util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-15.
 */

public class Check_Reply extends Fragment implements View.OnClickListener {
    private static final String ADD_REPLY = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/add_reply.php";
    private static final String SHOW_REPLY = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/showing_reply.php";
    Button btn_send;
    EditText edit_reply;
    int project_id;
    String project_username;
    String project_email;
    ArrayList<ListItem_Reply> listItems;
    RecyclerView recyclerView;

    public Check_Reply() {

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reply, container, false);

        //댓글 전송 버튼과 에디트 메세지.
        btn_send = rootView.findViewById(R.id.btn_send);
        edit_reply = rootView.findViewById(R.id.edit_reply);

        //댓글을 눌럿을 때 보내준 값을 가지고 옴.
        Bundle bundle = this.getArguments();
        if(bundle != null){
            project_id = bundle.getInt("project_id");
           // project_username = bundle.getString("username");
            //project_email = bundle.getString("email");
        }


        //댓글 전송버튼
        btn_send.setOnClickListener(this);


        //댓글 출력하기
        uploadReply();

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        //팔로잉 친구 arraylist 생성
        listItems = new ArrayList<ListItem_Reply>();

        return rootView;
    }

    //댓글 출력
    public void uploadReply() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOW_REPLY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("reply");
                            // JSONObject data = jsonarray.getJSONObject(0);


                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                String username = object.getString("username");
                                String message = object.getString("message");
                                String url = object.getString("profile");
                                String history = object.getString("history");
                                String email = object.getString("email");
                                int id = object.getInt("id");
                                int project_id = object.getInt("project_id");

                                ListItem_Reply item = new ListItem_Reply();
                                item.message = message;
                                item.url = url;
                                item.username = username;
                                item.history = history;
                                item.id = id;
                                item.project_id = project_id;
                                item.email = email;

                                System.out.println("유저네임 : " + username);
                                System.out.println("유저네임 : " +message);
                                System.out.println("유저네임 : " + url);
                                listItems.add(item);

                            }
                            RecyclerViewAdapter_Show_Reply adapter = new RecyclerViewAdapter_Show_Reply(getContext(), listItems);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            //해쉬맵을 통해 php에 이메일 값을 보내줌.
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                System.out.println(project_id);

                params.put("project_id", String.valueOf(project_id));

                return params;

            }
        };

        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);
    }

    //전송버튼 메서드
    public void send(){

        final String message = edit_reply.getText().toString();
        if(message.equals("") || message.equals(" ") ||  message.equals("  ") ||  message.equals("  ") ||  message.equals("  ") ) {
            Toast.makeText(getContext(), "빈칸 없이 작성해주세요.", Toast.LENGTH_SHORT).show();
        }else{


            StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_REPLY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            // volleyRegister.php의 response로부터 회원가입 쿼리가 성공적으로 작동하였을 때 받아오는 값.
                            if (response.contains("1")) {
                                Toast.makeText(getContext(), "작성 되었습니다.", Toast.LENGTH_LONG).show();




                            } else {
                                Toast.makeText(getContext(), "댓글 작성 실패.", Toast.LENGTH_LONG).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }


                    }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("project_id", String.valueOf(project_id));
                    params.put("email", loadLoginEmail());
                    params.put("message",message);
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send:
                send();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.detach(this).attach(this).commit();
                break;
        }
    }

    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getContext().getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println(email_from_login);

        return email_from_login;
    }
}