package com.android.ubclaunchpad.driver.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class sessionInfoActivity extends AppCompatActivity {

    private static final String TAG = "sessionInfoActivity";
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final ArrayList<String> itemsArray = new ArrayList<String>();
        final ListView listView = (ListView) findViewById(R.id.sessionItemsList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, itemsArray);
        listView.setAdapter(adapter);
        String sessionName = getIntent().getStringExtra(getString(R.string.dSessionName));
        final String passengerDistance = "\nP\n\t\t\t\t";
        final String driverDistance = "\nD\n\t\t\t\t";
        TextView  SessionName = (TextView) findViewById(R.id.viewSessionName);
        SessionName.setText(sessionName);

        //Adding Drivers
        mDatabase.child("Session Group").child(sessionName).child("drivers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String driverInfo = (String) dataSnapshot.child("title").getValue(String.class);
                driverInfo =  driverDistance + driverInfo;
                if(driverInfo != null) {
                    itemsArray.add(driverInfo);
                    adapter.notifyDataSetChanged();
                }
                Log.d(TAG, "Populating");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG,"Data Changed");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                final String removableDriver = driverDistance + (String) dataSnapshot.child("title").getValue();
                adapter.remove(removableDriver);
                adapter.notifyDataSetChanged();
                Log.d(TAG,"Data Removed");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG,"Data Moved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"Data Cancelled");
            }
        });

        //Adding Passengers
        mDatabase.child("Session Group").child(sessionName).child("passengers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                 String passengerInfo = (String) dataSnapshot.child("title").getValue(String.class);
                passengerInfo =  passengerDistance + passengerInfo ;
                if(passengerInfo != null) {
                    adapter.add(passengerInfo);
                }
                Log.d(TAG, "Populating");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG,"Data Changed");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Data Removed");
                final String removablePassenger = passengerDistance + (String) dataSnapshot.child("title").getValue();
                adapter.remove(removablePassenger);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG,"Data Moved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"DataCancelled");
            }
        });


        //Testing
//        mDatabase.child("Session Group").child("UBC").child("drivers").push().child("title").setValue("before :D");
//        mDatabase.child("Session Group").child("UBC").child("passengers").push().child("title").setValue("So even if the name is really long. Still it will look better then before :D");

    }
}