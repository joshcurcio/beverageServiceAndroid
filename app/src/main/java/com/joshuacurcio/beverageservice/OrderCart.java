package com.joshuacurcio.beverageservice;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.stripe.android.*;


public class OrderCart extends AppCompatActivity {

    ListView orderList;
    private String TAG;
    private ListAdapter lAdap;
    private Resources res;
    private TextView subTotal;
    private TextView tax;
    private TextView total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_cart);
        orderList = (ListView) findViewById(R.id.orderCartListView);
        // this will be a hashmap of the items
        // food and drink
        //from there we will have another view depending on what one they selected.
        // everything that gets clicked in the menu gets stored into userOrder
        // userOrder contains a hashmap that connects a drink/food to its price and qty of the order.

        subTotal = (TextView) findViewById(R.id.txtSubtotal);
        tax = (TextView) findViewById(R.id.txtTax);
        total = (TextView) findViewById(R.id.txtTotal);

        lAdap = new CustomAdapterForCart(OrderCart.this, Singleton.userCart, res);
        orderList.setAdapter(lAdap);
        orderList.setFocusableInTouchMode(true);

        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String item = ((TextView)view).getText().toString();
                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
            }


        });

        subTotal.setText("$" + Singleton.userOrder.getSubTotal());
        tax.setText("$" + Singleton.userOrder.getTax());
        total.setText("$" + Singleton.userOrder.getTotal());
    }

}
