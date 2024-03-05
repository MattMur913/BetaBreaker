package com.example.betabreaker.Classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betabreaker.R;

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
                .inflate(R.layout.reyc_layout_display_centre, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to views in the ViewHolder
        ClsCentre item = itemList.get(position);
        holder.textView.setText(item.getIdCentre());
        holder.textView1.setText(item.getCentreName());
        // Set other views accordingly
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the data source
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, textView1;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            //cardView = CardView.findViewById(R.id.dsCCard);
            textView = itemView.findViewById(R.id.dsCAddressview);
            textView1 = itemView.findViewById(R.id.dsCNameview);
            imageView = itemView.findViewById(R.id.dsCLogoview);
        }
    }
}