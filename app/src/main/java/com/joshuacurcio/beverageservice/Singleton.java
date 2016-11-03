package com.joshuacurcio.beverageservice;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.joshuacurcio.beverageservice.Objects.Course;
import com.joshuacurcio.beverageservice.Objects.DrinkItem;
import com.joshuacurcio.beverageservice.Objects.FoodItem;
import com.joshuacurcio.beverageservice.Objects.UserProfile;

import java.util.ArrayList;
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
    public static HashMap<String, FoodItem> foodItems;
    public static LinkedList<String> foodMenu;
    public static ArrayList CustomFoodListViewValuesArr;
    public static ArrayList CustomDrinkListViewValuesArr;

    public static HashMap<String, DrinkItem> drinkItems;
    public static LinkedList<String> drinkMenu;

    public static UserProfile userProfile;



}
