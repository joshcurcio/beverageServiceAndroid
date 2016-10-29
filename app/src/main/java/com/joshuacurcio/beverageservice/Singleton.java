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

    public static HashMap<String, DrinkItem> drinkItems;
    public static LinkedList<String> drinkMenu;



}
