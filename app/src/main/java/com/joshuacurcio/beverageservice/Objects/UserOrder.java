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
    private double lattitude;
    private double longitude;
    private String user;
    private boolean delivered;

    public UserOrder(){

    }

    public UserOrder(String user, ArrayList orderItems, double subTotal, double tax, double fees,double  total){
        this.orderItems = orderItems;
        this.total = total;
        this.tax = tax;
        this.fees = fees;
        this.subTotal = subTotal;
        this.lattitude = 0.00;
        this.longitude = 0.00;
        this.user = user;
        this.delivered = false;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getUser() {
        return user;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
