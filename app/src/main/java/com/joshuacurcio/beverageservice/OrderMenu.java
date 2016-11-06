package com.joshuacurcio.beverageservice;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.joshuacurcio.beverageservice.R;
import com.joshuacurcio.beverageservice.Singleton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static com.joshuacurcio.beverageservice.Singleton.mDatabase;

public class OrderMenu extends AppCompatActivity implements View.OnClickListener {
    public ListView menuList;
    private String TAG;
    private ListAdapter lAdap;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_menu);


        findViewById(R.id.butFoodMenu).setOnClickListener(this);
        findViewById(R.id.butDrinkMenu).setOnClickListener(this);
        res = Resources.getSystem();
        menuList = (ListView) findViewById(R.id.menuListView);
        // this will be a hashmap of the items
        // food and drink
        //from there we will have another view depending on what one they selected.
        // everything that gets clicked in the menu gets stored into userOrder
        // userOrder contains a hashmap that connects a drink/food to its price and qty of the order.

        lAdap = new CustomAdapter(OrderMenu.this, Singleton.CustomFoodListViewValuesArr, res);
        menuList.setAdapter(lAdap);
        menuList.setFocusableInTouchMode(true);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String item = ((TextView)view).getText().toString();
                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
            }


        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.butFoodMenu) {
            lAdap = new CustomAdapter(OrderMenu.this, Singleton.CustomFoodListViewValuesArr, res);
            menuList.setAdapter(lAdap);
            menuList.setFocusableInTouchMode(true);
        }
        else if (id == R.id.butDrinkMenu) {

            lAdap = new CustomAdapter(OrderMenu.this, Singleton.CustomDrinkListViewValuesArr, res);
            menuList.setAdapter(lAdap);
            menuList.setFocusableInTouchMode(true);
        }

    }
}
