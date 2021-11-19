package com.example.userapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;


public class ServerIPAddress {
    private String serverIPAddress = "";
    private static ServerIPAddress instance = new ServerIPAddress();

    private ServerIPAddress(){
        FirebaseDatabase mFDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = mFDatabase.getReference().child("raspberryPiServer");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    serverIPAddress = dataSnapshot.child("ip_address").getValue(String.class);
                    Log.i("Server IP Address", serverIPAddress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("Server IP Address", databaseError.getMessage());
            }
        });
    }

    public static ServerIPAddress getInstance(){
        if(instance == null){
            instance = new ServerIPAddress();
        }
        return instance;
    }

    public String getServerIPAddress(){
        if (serverIPAddress.isEmpty()){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return serverIPAddress;
    }
}
