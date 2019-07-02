package com.example.goo.test.Fragment.MyInfo;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.goo.test.Activity.LoginActivity;

import com.example.goo.test.R;
import com.example.goo.test.Util.Pager_MyInfo;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Goo on 2018-05-03.
 */

public class MyInfoFragment extends Fragment implements View.OnClickListener {
   /* TextView email, name, txt_content_count, txt_follower_count, txt_following_count;
    LinearLayout post, line_follower, line_following;
    CircleImageView prof_img;*/
    Toolbar toolbar2;
    LoginActivity loginActivity;
    //로그인 후 메인액티비티로부터 이메일 값을 가져오기 위해 선언.
    String email2;
    String url;

    private DrawerLayout drawer;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    //마지막으로 뒤로가기 버튼이 터치된 시간
    private long lastTimeBackPressed;


    public MyInfoFragment() {

    }

    private static final String HOME_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/Home.php";
    private static final String FOLLOWING_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/followCount.php";
    private static final String FOLLOWER_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/followerCount.php";
    private static final String POST_URL = "http://ec2-13-125-216-157.ap-northeast-2.compute.amazonaws.com/postCount.php";
    public static final String KEY_EMAIL = "email";
    Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myinfo, container, false);
        loginActivity = new LoginActivity();

        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        mTabLayout.addTab(mTabLayout.newTab().setText("프로필"));
        mTabLayout.addTab(mTabLayout.newTab().setText("프로젝트"));

        String strColor = "#ffffff";
        mTabLayout.setTabTextColors(Color.parseColor(strColor),Color.parseColor(strColor));

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // mSelectionsPagerAdapter = new SectionsPagerAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager());

        mViewPager = (ViewPager) rootView.findViewById(R.id.container);

        Pager_MyInfo adapter = new Pager_MyInfo(getFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(adapter);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setScrollPosition(position, 0, true);
                mTabLayout.setSelected(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return rootView;


    }
    //툴바설정
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu_for_myinfo, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    //myInfo 옵션메뉴
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        //로그아웃 메뉴
        if (res_id == R.id.action_logout) {

            // ((LoginActivity) getActivity()).signOut();
            //로그아웃 할 경우에 쉐어드에 저장해둔 이메일을 삭제하고, 로그인 화면으로 이동한다.
            SharedPreferences pref = getContext().getSharedPreferences("Login", MODE_PRIVATE); // 선언
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);

            Toast.makeText(getContext(), "You selected logout", Toast.LENGTH_SHORT).show();
        }

        //프로필 메뉴
        if (res_id == R.id.action_profile) {

            Intent intent = new Intent(getContext(), ChatProfileActivity.class);
            startActivity(intent);


            //Bundle bundle = new Bundle();
            //bundle.putInt("project_id", listItems.get(position).getId()); // Put anything what you want
            //bundle.putString("email", listItems.get(position).getEmail()); // Put anything what you want
            //bundle.putString("username", listItems.get(position).getUsername()); // Put anything what you want

            // Check_Like_Member check_like_member = new Check_Like_Member();
            //check_like_member.setArguments(bundle);

           *//* ((AppCompatActivity) getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    //뒤로가기 눌렀을 때 다시 여기 화면으로 돌아오기 위해선 addtoBackStack메서드를 사용.
                    .replace(R.id.tab_new_project_myInfo, check_like_member).addToBackStack(null)
                    .commit();*//*

        }
        if(res_id == R.id.action_concern){
            Intent intent = new Intent(getContext(), Profile_Detail.class);
            startActivity(intent);

        }

        return true;
    }
*/


    //서버로부터 프로필 값을 가져오는 메서드
  /*  private void update() {

        //메인액티비티에서 이메일 값을 보내는 경우 받아온다.
        *//*if (getArguments() != null) {
            email2 = this.getArguments().getString("email").toString();

        }*//*

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("member");
                            JSONObject data = jsonarray.getJSONObject(0);

                            String username = data.getString("username");
                            String email5 = data.getString("email");
                            String url = data.getString("profile");

                            System.out.println("url은 " + url);

                            Picasso.with(getContext()).invalidate(url);

                            Picasso.with(getContext()).load(url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).error(R.mipmap.ic_launcher_round).into(prof_img);

                         *//*   Picasso.with(getContext())
                                    .load(url)
                                    .into(prof_img);

*//*
                            name.setText(username);
                            email.setText(email5);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put(KEY_EMAIL, loadLoginEmail());
                return parameters;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);

        // RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //Adding request to the queue
        // requestQueue.add(stringRequest);

    }


    //서버로부터 팔로우 수 가져오기
    private void update_following_count() {

        //메인액티비티에서 이메일 값을 보내는 경우 받아온다.
        if (getArguments() != null) {
            email2 = this.getArguments().getString("email").toString();

        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FOLLOWING_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("follow");

                            //JSONObject data = jsonarray.getJSONObject(1);

                            int following = jsonarray.length();
                            //String email5 = data.getString("email");

                            txt_following_count.setText(following + "");
                            // email.setText(email5);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put(KEY_EMAIL, loadLoginEmail());
                return parameters;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);

    }

    //서버로부터 팔로워 수 가져오기
    private void update_follower_count() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, FOLLOWER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("follow");

                            //JSONObject data = jsonarray.getJSONObject(0);

                            int follower = jsonarray.length();


                            txt_follower_count.setText(follower + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put(KEY_EMAIL, loadLoginEmail());
                return parameters;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);

    }

    //서버로부터 팔로워 수 가져오기
    private void getPostCount() {

        //메인액티비티에서 이메일 값을 받아온다.
        if (getArguments() != null) {
            email2 = this.getArguments().getString("email").toString();

        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray jsonarray = jsonobject.getJSONArray("project_content");

                            //JSONObject data = jsonarray.getJSONObject(0);

                            int post = jsonarray.length();


                            txt_content_count.setText(post + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put(KEY_EMAIL, loadLoginEmail());
                return parameters;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestque(stringRequest);

    }
*/

    //버튼 이벤트 모음.
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.following:


                Following_Fragment following_fragment = new Following_Fragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.myinfo, following_fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;

            case R.id.follower:

                Follower_Fragment follower_fragment = new Follower_Fragment();
                FragmentTransaction fragmentTransaction2 = getFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.myinfo, follower_fragment);
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();
                break;
        }
    }

    //이메일 값 가져오기
    private String loadLoginEmail() {
        SharedPreferences sp = getContext().getSharedPreferences("Login", MODE_PRIVATE);
        String email_from_login = sp.getString("Login", null);
        System.out.println("로그인 후 가지고 온 이메일 값은 : " + email_from_login);

        return email_from_login;
    }


}
