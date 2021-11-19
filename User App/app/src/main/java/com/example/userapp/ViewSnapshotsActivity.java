package com.example.userapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewSnapshotsActivity extends AppCompatActivity {
    private static final String TAG = "View Snapshots Activity";
    private String dateAndTime;
    private String priority;
    private ProgressDialog progressDialog;
    private ArrayList<String> list;
    private  ArrayList numList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_snapshots);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                dateAndTime = null;
                priority = null;
            } else {
                priority = extras.getString("priority");
                dateAndTime = extras.getString("dateAndTime");
            }
        }

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(dateAndTime);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B58A3F")));
        }

        progressDialog=new ProgressDialog(ViewSnapshotsActivity.this);
        progressDialog.setMessage("Please wait loading snapshots...");
        progressDialog.show();

        list = new ArrayList(); numList = new ArrayList();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        for (int i=0; i<5; i++){
            String imageName = "new_cool_image_"+i+".jpg";
            final int s = i+1;
            storageRef.child(dateAndTime+"/"+priority+"/"+imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Log.i(TAG, "URL : "+uri);
                    list.add(uri.toString());
                    numList.add(Integer.toString(s));
                    progressDialog.dismiss();
                    buildGridView();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }






    }

    private void buildGridView(){
        GridAdapter adapter = new GridAdapter(ViewSnapshotsActivity.this, list, numList);
        final GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String itemValue = (String) gridView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), (String)numList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
