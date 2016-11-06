package com.joshuacurcio.beverageservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joshuacurcio.beverageservice.Objects.Course;
import com.joshuacurcio.beverageservice.Objects.DrinkItem;
import com.joshuacurcio.beverageservice.Objects.FoodItem;
import com.joshuacurcio.beverageservice.Objects.OrderItem;
import com.joshuacurcio.beverageservice.Objects.UserProfile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;


public class UserHome extends AppCompatActivity implements View.OnClickListener {
    String TAG = "User Home";
    Spinner courseSpinner;


    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        Singleton.CustomFoodListViewValuesArr = new ArrayList();
        Singleton.CustomDrinkListViewValuesArr = new ArrayList();
        Singleton.userCart = new LinkedList<OrderItem>();

        Singleton.mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = Singleton.mAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            Singleton.mDatabase = FirebaseDatabase.getInstance().getReference();
        } else {
            // User is signed out
        }

        Singleton.mDatabase = FirebaseDatabase.getInstance().getReference();



        courseSpinner = (Spinner) findViewById(R.id.courseSpinner);
        LinkedList<String> courseList = new LinkedList<String>();

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courseList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(spinnerArrayAdapter);

        // Buttons
        findViewById(R.id.buttonPlaceOrder).setOnClickListener(this);
        findViewById(R.id.buttonSignOut).setOnClickListener(this);
        findViewById(R.id.butSettings).setOnClickListener(this);

        //listner for when courses are added or changed
        Singleton.mDatabase.child("courses").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Singleton.courseMap.put(dataSnapshot.getKey(), dataSnapshot.getValue(Course.class));
                Singleton.courseMap.get(dataSnapshot.getKey()).setCourseID(dataSnapshot.getKey());
                Singleton.courseIDNameRelationship.put(Singleton.courseMap.get(dataSnapshot.getKey()).getCourseName(), dataSnapshot.getKey().toString());
                spinnerArrayAdapter.add(Singleton.courseMap.get(dataSnapshot.getKey()).getCourseName());

                spinnerArrayAdapter.sort(new Comparator<String>() {
                    @Override
                    public int compare(String lhs, String rhs) {
                        return lhs.compareTo(rhs);
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                spinnerArrayAdapter.remove(Singleton.courseMap.get(dataSnapshot.getKey()).getCourseName());
                Singleton.courseMap.remove(dataSnapshot.getKey());

                Singleton.courseMap.put(dataSnapshot.getKey(), dataSnapshot.getValue(Course.class));
                Singleton.courseMap.get(dataSnapshot.getKey()).setCourseID(dataSnapshot.getKey());
                spinnerArrayAdapter.add(Singleton.courseMap.get(dataSnapshot.getKey()).getCourseName());

                spinnerArrayAdapter.sort(new Comparator<String>() {
                    @Override
                    public int compare(String lhs, String rhs) {
                        return lhs.compareTo(rhs);
                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                spinnerArrayAdapter.remove(Singleton.courseMap.get(dataSnapshot.getKey()).getCourseName());
                Singleton.courseMap.remove(dataSnapshot.getKey());

                spinnerArrayAdapter.sort(new Comparator<String>() {
                    @Override
                    public int compare(String lhs, String rhs) {
                        return lhs.compareTo(rhs);
                    }
                });
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void signOut() {
        Singleton.mAuth.signOut();
        startActivity(new Intent(UserHome.this, MainActivity.class));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.buttonPlaceOrder) {
            Singleton.foodItems = new HashMap<String, FoodItem>();
            Singleton.foodMenu = new LinkedList<String>();
            Singleton.drinkItems = new HashMap<String, DrinkItem>();
            Singleton.drinkMenu = new LinkedList<String>();
            Singleton.selectedCourse = Singleton.courseIDNameRelationship.get(courseSpinner.getSelectedItem().toString()); //test

            Singleton.mDatabase.child("courses").child(Singleton.selectedCourse).child("menu").child("food").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Singleton.CustomFoodListViewValuesArr = new ArrayList();
                    for( DataSnapshot child : dataSnapshot.getChildren())
                    {
                        final OrderItem sched = new OrderItem();
                        sched.setName(child.getValue(FoodItem.class).getName());
                        sched.setPrice(child.getValue(FoodItem.class).getPrice());
                        sched.setQuantity(0);
                        /******** Take Model Object in ArrayList **********/
                        Singleton.CustomFoodListViewValuesArr.add(sched);

                        Singleton.foodItems.put((child.child("name").getValue()).toString(), child.getValue(FoodItem.class));
                        Singleton.foodMenu.add(child.child("name").getValue().toString() + " - " + Singleton.foodItems.get(child.child("name").getValue().toString()).getPrice());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Singleton.mDatabase.child("courses").child(Singleton.selectedCourse).child("menu").child("drinks").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Singleton.CustomDrinkListViewValuesArr = new ArrayList();
                    for( DataSnapshot child : dataSnapshot.getChildren())
                    {
                        final OrderItem sched = new OrderItem();
                        sched.setName(child.getValue(DrinkItem.class).getName());
                        sched.setPrice(child.getValue(DrinkItem.class).getPrice());
                        sched.setQuantity(0);
                        /******** Take Model Object in ArrayList **********/
                        Singleton.CustomDrinkListViewValuesArr.add(sched);

                        Singleton.drinkItems.put((child.child("name").getValue()).toString(), child.getValue(DrinkItem.class));
                        Singleton.drinkMenu.add(child.child("name").getValue().toString() + " - " + Singleton.drinkItems.get(child.child("name").getValue().toString()).getPrice());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            startActivity(new Intent(UserHome.this, OrderMenu.class));
            Log.d(TAG, Singleton.selectedCourse);
        }
        else if (id == R.id.buttonSignOut)
        {
            signOut();
        }
        else if (id == R.id.butSettings)
        {
            startActivity(new Intent(UserHome.this, UserProfilePage.class));
        }

    }

}
