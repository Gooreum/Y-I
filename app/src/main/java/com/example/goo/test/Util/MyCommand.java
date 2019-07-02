package com.example.goo.test.Util;

import android.content.Context;

import com.android.volley.Request;

import java.util.ArrayList;

/**
 * Created by Goo on 2018-05-28.
 */

public class MyCommand<T> {
    private ArrayList<Request<T>> requestList = new ArrayList<>();

    private Context context;
    public MyCommand(Context context){
        this.context = context;

    }

    public void add(Request<T> request){
        requestList.add(request);
    }

    public void remove(Request<T> request){
        requestList.remove(request);
    }

    public void execute(){
        for(Request<T> request: requestList){
            CustomVolleyRequest.getInstance(context).addToRequestque(request);
        }
    }
}
