package com.joshuacurcio.beverageservice;

/**
 * Created by Josh on 10/29/2016.
 */

public class DrinkItem {
    private String name;
    private double price;

    public DrinkItem(){

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

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString()
    {
        return this.name + "-" + " $" + this.price;
    }

}
