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
            Card card = new Card("4242-4242-4242-4242", 12, 2017, "123");

            if (!card.validateCard()) {
                // Show errors
            }

            try
            {
                Stripe testStripe = new Stripe("pk_test_pTSNGlG76tNyYcGjATRkQ6XC");
                testStripe.createToken(
                        card,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                // Send token to your server

                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                HttpURLConnection urlConn = null;
                                String result = "-1";
                                try {

                                    URL url;
                                    DataOutputStream printout;
                                    String address = "https://ez-bev.herokuapp.com/checkout";
                                    Log.d("sendPost",address);
                                    url = new URL (address);
                                    urlConn = (HttpsURLConnection) url.openConnection();
                                    urlConn.setDoInput (true);
                                    urlConn.setDoOutput (true);
                                    urlConn.setRequestMethod("POST");
                                    urlConn.setChunkedStreamingMode(100);

                                    HashMap<String, String> postDataParams = new HashMap();
                                    postDataParams.put("amount", Singleton.userOrder.getTotal().toString());
                                    postDataParams.put("stripeToken", token.toString());
                                    OutputStream os = urlConn.getOutputStream();
                                    BufferedWriter writer = new BufferedWriter(
                                            new OutputStreamWriter(os, "UTF-8"));
                                    writer.write(getPostDataString(postDataParams));

                                    writer.flush();
                                    writer.close();
                                    os.close();


                                    // Send POST output.

                                    printout = new DataOutputStream(urlConn.getOutputStream());
                                    printout.flush();
                                    result = urlConn.getResponseMessage();
                                    Log.d(TAG, "onSuccess: "+ result);
                                    if(result.equalsIgnoreCase("OK"))
                                    {
                                        Toast.makeText(OrderCart.this, "Payment Successful", Toast.LENGTH_LONG).show();
                                    }

                                    printout.close();
                                    urlConn.connect();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }  finally {
                                    if(urlConn !=null)
                                        urlConn.disconnect();
                                }


                            }
                            public void onError(Exception error) {
                                // Show localized error message
                            /*Toast.makeText(getContext(),
                                    error.getLocalizedString(getContext()),
                                    Toast.LENGTH_LONG
                            ).show();*/
                            }
                        }
                );
            }
            catch (Exception ex)
            {

            }

        }


    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
