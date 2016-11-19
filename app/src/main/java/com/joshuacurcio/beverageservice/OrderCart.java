package com.joshuacurcio.beverageservice;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.stripe.android.*;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static java.security.AccessController.getContext;


public class OrderCart extends AppCompatActivity implements View.OnClickListener {

    ListView orderList;
    private String TAG;
    private ListAdapter lAdap;
    private Resources res;
    private TextView subTotal;
    private TextView tax;
    private TextView fees;
    private TextView total;
    private Button confirmCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_cart);
        orderList = (ListView) findViewById(R.id.orderCartListView);
        confirmCart = (Button) findViewById(R.id.butConfirmCart);
        // this will be a hashmap of the items
        // food and drink
        //from there we will have another view depending on what one they selected.
        // everything that gets clicked in the menu gets stored into userOrder
        // userOrder contains a hashmap that connects a drink/food to its price and qty of the order

        subTotal = (TextView) findViewById(R.id.txtSubtotal);
        tax = (TextView) findViewById(R.id.txtTax);
        fees = (TextView) findViewById(R.id.txtFees);
        total = (TextView) findViewById(R.id.txtTotal);

        confirmCart.setOnClickListener(this);

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

        subTotal.setText("$" + String.format("%.2f", Singleton.userOrder.getSubTotal()));
        tax.setText("$" +  String.format("%.2f", Singleton.userOrder.getTax()));
        fees.setText("$" +  String.format("%.2f", Singleton.userOrder.getFees()));
        total.setText("$" + String.format("%.2f", Singleton.userOrder.getTotal()));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.butConfirmCart) {

            startActivity(new Intent(OrderCart.this, CardActivity.class));
        }


    }


}
