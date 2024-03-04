package com.example.betabreaker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ClsCentre> itemList; // List of items to display

    public MyAdapter(List<ClsCentre> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create a new ViewHolder
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_display_centre, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to views in the ViewHolder
        ClsCentre item = itemList.get(position);
        holder.textView.setText(item.getDescription());
        // Set other views accordingly
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the data source
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            textView = itemView.findViewById(R.id.dsCAddressview);
            imageView = itemView.findViewById(R.id.dsCLogoview);
        }
    }
}


