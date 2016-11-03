package com.joshuacurcio.beverageservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joshuacurcio.beverageservice.Objects.UserProfile;

public class UserProfilePage extends AppCompatActivity
{
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mAddress;
    private TextView mPIN;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);

        mFirstName = (TextView) findViewById(R.id.userFirstName);
        mLastName = (TextView) findViewById(R.id.userLastName);
        mAddress = (TextView) findViewById(R.id.userAddress);
        mPIN = (TextView) findViewById(R.id.userPIN);

        // Get a reference to our users
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = Singleton.mAuth.getCurrentUser();
        if(user != null) {
            Singleton.mDatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Singleton.userProfile = dataSnapshot.getValue(UserProfile.class);
                    updateFields();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            startActivity(new Intent(UserProfilePage.this, MainActivity.class));
        }
        updateFields();

    }

    public void updateFields()
    {
        mFirstName.setText(Singleton.userProfile.getFirstName());
        mLastName.setText(Singleton.userProfile.getLastName());
        mAddress.setText(Singleton.userProfile.getAddress());
        mPIN.setText(Singleton.userProfile.getPIN());
    }
}
