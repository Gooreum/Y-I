package com.example.goo.test.Item;

/**
 * Created by Goo on 2018-05-27.
 */

public class Item_Interesting {
    public String section;
    public String reason;

    public boolean delete = false;
    public boolean motify = false;
    public Item_Interesting(){}
    public Item_Interesting(String section, String reason){
        this.section = section;
        this.reason = reason;
    }
}
