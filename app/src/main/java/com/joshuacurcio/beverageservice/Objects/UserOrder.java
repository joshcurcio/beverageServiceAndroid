package com.joshuacurcio.beverageservice.Objects;

import java.util.ArrayList;

/**
 * Created by Josh on 11/8/2016.
 */

public class UserOrder {
    private ArrayList<OrderItem> orderItems;
    private double total;
    private double tax;
    private double subTotal;
    private double fees;

    public UserOrder(){

    }

    public UserOrder(ArrayList orderItems, double subTotal, double tax, double fees,double  total){
        this.orderItems = orderItems;
        this.total = total;
        this.tax = tax;
        this.fees = fees;
        this.subTotal = subTotal;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Double getTotal() {
        return total;
    }

    public Double getTax() {
        return tax;
    }

    public double getFees() {
        return fees;
    }

    public Double getSubTotal() {
        return subTotal;
    }
}
