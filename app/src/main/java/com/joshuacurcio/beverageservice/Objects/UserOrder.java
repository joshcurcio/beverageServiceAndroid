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

    public UserOrder(){

    }

    public UserOrder(ArrayList orderItems, double subTotal, double tax, double  total){
        this.orderItems = orderItems;
        this.total = total;
        this.tax = tax;
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

    public Double getSubTotal() {
        return subTotal;
    }
}
