package com.joshuacurcio.beverageservice;

/**
 * Created by Josh on 10/27/2016.
 */

public class FoodItem {


    private String name;
    private double price;

    public FoodItem(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }


    @Override
    public String toString()
    {
        return this.name + "-" + " $" + this.price;
    }

}
