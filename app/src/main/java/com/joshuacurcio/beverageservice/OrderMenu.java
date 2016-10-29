package com.joshuacurcio.beverageservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class OrderMenu extends AppCompatActivity {
    private ListView menuList;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_menu);

        menuList = (ListView) findViewById(R.id.menuListView);
        // this will be a hashmap of the items
        // food and drink
        //from there we will have another view depending on what one they selected.
        // everything that gets clicked in the menu gets stored into userOrder
        // userOrder contains a hashmap that connects a drink/food to its price and qty of the order.
        Singleton.mDatabase.child("courses").child(Singleton.selectedCourse).child("menu").child("food").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Singleton.foodItems.put(dataSnapshot.child("name").getValue().toString(), dataSnapshot.getValue(FoodItem.class));
                Singleton.foodMenu.add(dataSnapshot.child("name").getValue().toString() + " - " + Singleton.foodItems.get(dataSnapshot.child("name").getValue().toString()).getPrice());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Singleton.foodMenu.remove(dataSnapshot.child("name").getValue().toString());
                Singleton.foodItems.remove(dataSnapshot.child("name").getValue().toString());
                Singleton.foodItems.put(dataSnapshot.child("name").getValue().toString(), dataSnapshot.getValue(FoodItem.class));
                Singleton.foodMenu.add(dataSnapshot.child("name").getValue().toString() + " - " + Singleton.foodItems.get(dataSnapshot.child("name").getValue().toString()).getPrice());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Singleton.foodMenu.remove(dataSnapshot.child("name").getValue().toString());
                Singleton.foodItems.remove(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ArrayAdapter lAdap = new ArrayAdapter(this,android.R.layout.simple_list_item_1, Singleton.foodMenu);
        menuList.setAdapter(lAdap);
        menuList.setFocusableInTouchMode(true);
    }
}
