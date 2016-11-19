package com.joshuacurcio.beverageservice;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.joshuacurcio.beverageservice.Objects.DrinkItem;
import com.joshuacurcio.beverageservice.Objects.FoodItem;
import com.joshuacurcio.beverageservice.Objects.UserOrder;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class CardActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText cardNum;
    private EditText expMonth;
    private EditText expYear;
    private EditText CVC;
    private Button purchase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        cardNum = (EditText) findViewById(R.id.cardNumber);
        expMonth = (EditText) findViewById(R.id.expMonth);
        expYear = (EditText) findViewById(R.id.expYear);
        CVC = (EditText) findViewById(R.id.txtCVC);
        purchase = (Button) findViewById(R.id.butPurchase);


        purchase.setOnClickListener(this);


    }

    public void makePayment() {

        Card card = new Card(
                cardNum.getText().toString(),
                Integer.parseInt(expMonth.getText().toString()),
                Integer.parseInt(expYear.getText().toString()),
                CVC.getText().toString()
        );
        if (!validateForm()) {
            return;
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
                                postDataParams.put("stripeToken", token.getId());
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
                                Log.d("", "onSuccess: "+ result);
                                if(result.equalsIgnoreCase("OK"))
                                {
                                    Toast.makeText(CardActivity.this, "Payment Successful", Toast.LENGTH_LONG).show();
                                }

                                printout.close();
                                urlConn.connect();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }  finally {
                                if(urlConn !=null)
                                    urlConn.disconnect();
                            }

                            Singleton.userOrder.setLattitude(Singleton.userLocation.latitude);
                            Singleton.userOrder.setLongitude(Singleton.userLocation.longitude);
                            Singleton.userOrderID = Singleton.mDatabase.child("courses").child(Singleton.selectedCourse).child("orders").push().getKey();
                            Singleton.mDatabase.child("courses").child(Singleton.selectedCourse).child("orders").child(Singleton.userOrderID).setValue(Singleton.userOrder);
                        }
                        public void onError(Exception error) {
                            // Show localized error message
                            Toast.makeText(CardActivity.this, "Payment Declined", Toast.LENGTH_LONG).show();
                        }
                    }
            );
        }
        catch (Exception ex)
        {

        }
    }


    private boolean validateForm() {
        boolean valid = true;

        String cardNumber = cardNum.getText().toString();
        if (TextUtils.isEmpty(cardNumber)) {
            cardNum.setError("Required.");
            valid = false;
        } else if (cardNumber.length() != 16) {
            cardNum.setError("Enter Valid Card Num");
            valid = false;
        } else {
            cardNum.setError(null);
        }

        String expireMonth = expMonth.getText().toString();
        if (TextUtils.isEmpty(expireMonth)) {
            expMonth.setError("Required.");
            valid = false;
        } else if (expireMonth.length() != 2 || (Integer.parseInt(expireMonth) < 1 || Integer.parseInt(expireMonth) > 12) || Integer.parseInt(expireMonth) <= Calendar.getInstance().get(Calendar.MONTH)) {
            expMonth.setError("Invalid Month");
            valid = false;
        } else {
            expMonth.setError(null);
        }

        String expYr = expYear.getText().toString();
        if (TextUtils.isEmpty(expYr)) {
            expYear.setError("Required.");
            valid = false;
        } else if (expYr.length() != 4 || Integer.parseInt(expYr) < Calendar.getInstance().get(Calendar.YEAR)){
            expYear.setError("Invalid Year.");
            valid = false;
        } else {
            expYear.setError(null);
        }

        String txtCVC = CVC.getText().toString();
        if (TextUtils.isEmpty(txtCVC)) {
            CVC.setError("Required.");
            valid = false;
        } else if (txtCVC.length() != 3 ){
            CVC.setError("Invalid CVC.");
            valid = false;
        } else {
            CVC.setError(null);
        }

        return valid;
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


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.butPurchase) {
            if(Singleton.userOrder == null)
            {
                Toast.makeText(this, "Order Cart is Empty", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CardActivity.this, UserHome.class));
                return;
            }
            makePayment();


            startActivity(new Intent(CardActivity.this, UserMapsActivity.class));
        }
    }
}
