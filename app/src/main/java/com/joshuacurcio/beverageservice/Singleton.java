package com.joshuacurcio.beverageservice;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.joshuacurcio.beverageservice.Objects.Course;
import com.joshuacurcio.beverageservice.Objects.DrinkItem;
import com.joshuacurcio.beverageservice.Objects.FoodItem;
import com.joshuacurcio.beverageservice.Objects.OrderItem;
import com.joshuacurcio.beverageservice.Objects.UserOrder;
import com.joshuacurcio.beverageservice.Objects.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Josh on 10/16/2016.
 */

public class Singleton {

    public static FirebaseAuth mAuth;
    public static DatabaseReference mDatabase;

    // gets list of courses
    public static HashMap<String, Course> courseMap;
    public static HashMap<String, String> courseIDNameRelationship;

    //keeps track of what course the golfer is currently at
    public static String selectedCourse;

    //keeps track of courses food options
    public static HashMap<String, FoodItem> foodItems;
    public static LinkedList<String> foodMenu;
    //keeps track of courses drinks
    public static HashMap<String, DrinkItem> drinkItems;
    public static LinkedList<String> drinkMenu;

    public static UserOrder userOrder;

    //used for the menu list
    public static ArrayList CustomFoodListViewValuesArr;
    public static ArrayList CustomDrinkListViewValuesArr;

    //keeps the logged in user information
    public static UserProfile userProfile;

    public static ArrayList<OrderItem> userCart;
    public static HashMap<String, OrderItem> userMenuToCart;


    public static void updateUserCart(){
        double subTotal = 0.00;
        double tax = 0.00;
        double fees = 0.00;
        double total = 0.00;
        int index = 0;
        for(String key: Singleton.userMenuToCart.keySet())
        {
            if(Singleton.userCart.contains(Singleton.userMenuToCart.get(key)))
            {
                Singleton.userCart.remove(Singleton.userMenuToCart.get(key));
            }
            Singleton.userCart.add(Singleton.userMenuToCart.get(key));
            subTotal += Singleton.userCart.get(index).getPrice() * Singleton.userCart.get(index).getQuantity();
            index++;
        }
        tax = subTotal * 0.05;
        fees = (subTotal + .30)* .15;
        total = subTotal + tax + fees;
        Singleton.userOrder = new UserOrder(Singleton.userCart, subTotal, tax,  fees, total);

    }
}
