package com.joshuacurcio.beverageservice.Objects;

import java.util.LinkedList;

/**
 * Created by Josh on 11/1/2016.
 */

public class UserCart
{
    private LinkedList<OrderItem> orderItems;

    public UserCart()
    {
        this.orderItems = new LinkedList<OrderItem>();
    }

    public void addOrderItem(OrderItem orderItem)
    {
        this.orderItems.add(orderItem);
    }

    public void removeOrderItem(OrderItem orderItem)
    {
        this.orderItems.remove(orderItem);
    }

    public LinkedList<OrderItem> getOrderItems() {
        return orderItems;
    }

}
