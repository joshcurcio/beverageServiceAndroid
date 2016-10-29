package com.joshuacurcio.beverageservice;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by Josh on 10/20/2016.
 */

public class userOrder {
    private static HashMap<String, OrderItem> drinkItems;
    private static HashMap<String, OrderItem> foodItems;

    public userOrder()
    {
        drinkItems = new HashMap<String, OrderItem>();
        foodItems = new HashMap<String, OrderItem>();
    }

    public void addDrinkItem(String beverageName, OrderItem drinkItem)
    {
        drinkItems.put(beverageName, drinkItem);
    }

    public void removeDrinkItem(String beverageName)
    {
        drinkItems.remove(beverageName);
    }

    public void addFoodItem(String foodName, OrderItem foodItem)
    {
        foodItems.put(foodName, foodItem);
    }

    public void removeFoodItem(String foodName)
    {
        foodItems.remove(foodName);
    }

}
