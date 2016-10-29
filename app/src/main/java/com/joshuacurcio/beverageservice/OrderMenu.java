package com.joshuacurcio.beverageservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static com.joshuacurcio.beverageservice.Singleton.mDatabase;

public class OrderMenu extends AppCompatActivity implements View.OnClickListener {
    private ListView menuList;
    private String TAG;
    private ArrayAdapter lAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_menu);

        // Buttons
        findViewById(R.id.butFoodMenu).setOnClickListener(this);
        findViewById(R.id.butDrinkMenu).setOnClickListener(this);

        menuList = (ListView) findViewById(R.id.menuListView);
        // this will be a hashmap of the items
        // food and drink
        //from there we will have another view depending on what one they selected.
        // everything that gets clicked in the menu gets stored into userOrder
        // userOrder contains a hashmap that connects a drink/food to its price and qty of the order.



        lAdap = new ArrayAdapter(this,android.R.layout.simple_list_item_1, Singleton.foodMenu);
        menuList.setAdapter(lAdap);
        menuList.setFocusableInTouchMode(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.butFoodMenu) {
            lAdap = new ArrayAdapter(this,android.R.layout.simple_list_item_1, Singleton.foodMenu);
            menuList.setAdapter(lAdap);
            menuList.setFocusableInTouchMode(true);
        }
        else if (id == R.id.butDrinkMenu) {

            lAdap = new ArrayAdapter(this,android.R.layout.simple_list_item_1, Singleton.drinkMenu);
            menuList.setAdapter(lAdap);
            menuList.setFocusableInTouchMode(true);
        }

    }
}
