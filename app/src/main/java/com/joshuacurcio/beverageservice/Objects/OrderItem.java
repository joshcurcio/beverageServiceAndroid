package com.joshuacurcio.beverageservice.Objects;

/**
 * Created by Josh on 10/20/2016.
 */

public class OrderItem {
    private String name;
    private double price;
    private int quantity;

    public OrderItem (DrinkItem drinkItem, int quantity)
    {
        this.name = drinkItem.getName();
        this.price = drinkItem.getPrice();
        this.quantity = quantity;
    }

    public OrderItem (FoodItem foodItem, int quantity)
    {
        this.name = foodItem.getName();
        this.price = foodItem.getPrice();
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
