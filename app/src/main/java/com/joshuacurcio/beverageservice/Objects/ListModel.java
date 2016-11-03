package com.joshuacurcio.beverageservice.Objects;

/**
 * Created by Josh on 11/3/2016.
 */

public class ListModel {

    private String name = "";
    private double price = 0.00;
    private int quantity = 0;

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
