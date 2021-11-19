package com.example.userapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private IMyItemClickListener myItemClickListener;
    private ArrayList<HashMap<String, String>> mData;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, ArrayList<HashMap<String, String>> list){
        this.mData = list;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_readings_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        HashMap<String, String> map = mData.get(position);

        viewHolder.numberBox.setText(Integer.toString(position+1));
        viewHolder.dateAndTimeBox.setText(map.get("dateAndTime"));
        viewHolder.tempBox.setText(map.get("temperature"));
        viewHolder.humidityBox.setText(map.get("humidity"));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView numberBox, dateAndTimeBox, tempBox, humidityBox;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            numberBox = itemView.findViewById(R.id.numberBox);
            dateAndTimeBox = itemView.findViewById(R.id.dateAndTimeBox);
            tempBox = itemView.findViewById(R.id.tempBox);
            humidityBox = itemView.findViewById(R.id.humidityBox);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            if(myItemClickListener != null){
                myItemClickListener.onMyItemClick(view, getAdapterPosition());
            }
        }
    }

    HashMap<String, String> getItem(int position){
        return mData.get(position);
    }

    void setClickListener(IMyItemClickListener itemClickListener){
        myItemClickListener = itemClickListener;
    }

    public interface IMyItemClickListener{
        void onMyItemClick(View view, int position);
    }

    //String getItem(int id){
        //return mData.get(id);
    //}
}
