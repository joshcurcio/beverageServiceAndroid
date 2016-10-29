package com.joshuacurcio.beverageservice;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Josh on 10/16/2016.
 */

public class Singleton {
    public static HashMap<String, Course> courseMap;
    public static HashMap<String, String> courseIDNameRelationship;
    public static String selectedCourse;
    public static FirebaseAuth mAuth;
    public static DatabaseReference mDatabase;
    public static userOrder userOrderCart;
    public static HashMap<String, FoodItem> foodItems;
    public static LinkedList<String> foodMenu;

    public static void startFoodMenuListner() {

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
    }

}
