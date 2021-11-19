package com.example.userapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

//import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewReadingsActivity extends AppCompatActivity implements RecyclerViewAdapter.IMyItemClickListener {
    private static final String TAG = "Readings Activity";
    //private DatabaseReference mRef;
    private ArrayList<HashMap<String, String>> list;
    private RecyclerViewAdapter adapter;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground_robot_readings);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Readings of Robots");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B58A3F")));
        }

        progressDialog=new ProgressDialog(ViewReadingsActivity.this);
        progressDialog.setMessage("Please wait loading data...");
        progressDialog.show();

        FirebaseDatabase mFDatabase = FirebaseDatabase.getInstance();
        list = new ArrayList<>();
        DatabaseReference ref = mFDatabase.getReference().child("values");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    list.clear();
                    for (DataSnapshot child : dataSnapshot.getChildren()){

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("temperature", child.child("Temperature").getValue(String.class));
                        hashMap.put("humidity", child.child("Humidity").getValue(String.class));
                        hashMap.put("dateAndTime", child.child("Date").getValue(String.class));
                        hashMap.put("priority", Integer.toString(child.child("Priority").getValue(Integer.class)));
                        list.add(hashMap);

                        Log.i(TAG, " readingsActivity : Date "+child.child("Date").getValue(String.class));
                        Log.i(TAG, " readingsActivity : Humidity "+child.child("Humidity").getValue(String.class));
                        Log.i(TAG, " readingsActivity : Temperature "+child.child("Temperature").getValue(String.class));

                    }
                }
                buildRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void buildRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new RecyclerViewAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        progressDialog.dismiss();
    }

    @Override
    public void onMyItemClick(View view, int position) {
        HashMap<String, String> map = adapter.getItem(position);
        String dateAndTime = map.get("dateAndTime");
        //int priority = Integer.parseInt(map.get("priority"));
        //String temperature = map.get("temperature");
        //String humidity = map.get("humidity");

        Intent intent = new Intent(ViewReadingsActivity.this, ViewSnapshotsActivity.class);
        intent.putExtra("dateAndTime", map.get("dateAndTime"));
        intent.putExtra("priority", map.get("priority"));
        startActivity(intent);
        Toast.makeText(getApplicationContext(), dateAndTime, Toast.LENGTH_SHORT).show();
    }
}
