package com.example.userapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> picURL;
    private final ArrayList<String> numList;

    public GridAdapter(@NonNull Context context, ArrayList<String> picURL, ArrayList<String> numList) {
        super(context, R.layout.custom_images_layout, picURL);
        this.context = context;
        this.picURL = picURL;
        this.numList = numList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_images_layout, null, true);

        TextView textView = rowView.findViewById(R.id.textView);
        ImageView imageView = rowView.findViewById(R.id.image);

        textView.setText(numList.get(position));
        Log.i("PIC URL", picURL.get(position));
        Picasso.get().load(picURL.get(position)).into(imageView);

        return rowView;
    }
}
