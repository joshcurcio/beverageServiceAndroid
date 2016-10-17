package com.joshuacurcio.beverageservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Console;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;


public class UserHome extends AppCompatActivity implements View.OnClickListener {
    String TAG = "User Home";
    DatabaseReference mDatabase;
    Spinner courseSpinner;

    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        Singleton.mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        courseSpinner = (Spinner) findViewById(R.id.courseSpinner);
        LinkedList<String> courseList = new LinkedList<String>();

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courseList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(spinnerArrayAdapter);

        // Buttons
        findViewById(R.id.buttonPlaceOrder).setOnClickListener(this);
        findViewById(R.id.buttonSignOut).setOnClickListener(this);

        mDatabase.child("courses").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
        int i = v.getId();
        if (i == R.id.buttonPlaceOrder) {
            Singleton.selectedCourse = courseSpinner.getSelectedItem().toString();
        }
        else if (i == R.id.buttonSignOut) {
            signOut();
        }

        Toast.makeText(UserHome.this, Singleton.selectedCourse,
                Toast.LENGTH_SHORT).show();

    }

}
