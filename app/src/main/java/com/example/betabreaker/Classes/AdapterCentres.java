package com.example.betabreaker.Classes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.betabreaker.Frags.FragSpecCentre;
import com.example.betabreaker.R;

import java.util.List;

public class AdapterCentres extends RecyclerView.Adapter<AdapterCentres.ViewHolder> {
    private List<ClsCentre> itemList; // List of items to display

    public AdapterCentres(List<ClsCentre> itemList) {
        this.itemList = itemList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create a new ViewHolder
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reyc_layout_display_centre, parent, false);
        return new ViewHolder(itemView, itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to views in the ViewHolder
        ClsCentre item = itemList.get(position);

        Glide.with(holder.itemView.getContext()).load(GlobalUrl.imageUrl + item.getlogo()).apply(RequestOptions.placeholderOf(R.drawable.placeholder_image)).into(holder.imageView);
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

        public ViewHolder(@NonNull View itemView, List<ClsCentre> itemList) {
            super(itemView);
            // Initialize views
            textView = itemView.findViewById(R.id.dsCAddressview);
            textView1 = itemView.findViewById(R.id.dsCNameview);
            imageView = itemView.findViewById(R.id.dsCLogoview);
            // Set click listener for the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ClsCentre centre = itemList.get(position);
                        Log.d("SingleCentre", centre.getCentreName());
                        Log.d("SingleCentre", centre.getIdCentre());
                        Log.d("SingleCentre", centre.getAddress());
                        Log.d("SingleCentre", centre.getEmail());
                        Log.d("SingleCentre", centre.getlogo());
                        Log.d("SingleCentre", centre.getDescription());
                        Context context = itemView.getContext();
                        // Start the Fragment transaction
                        FragSpecCentre fragment = new FragSpecCentre();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("centre", centre);
                        fragment.setArguments(bundle);

                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragmentContainerView, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                        // Hide the RecyclerView
                        RecyclerView recyclerView = itemView.getRootView().findViewById(R.id.dsCRec);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
