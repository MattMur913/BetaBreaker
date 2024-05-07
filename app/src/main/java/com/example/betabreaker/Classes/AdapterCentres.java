package com.example.betabreaker.Classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.betabreaker.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterCentres extends RecyclerView.Adapter<AdapterCentres.ViewHolder> {
    private List<ClsCentre> itemList; // List of items to display

    private List<ClsCentre> masterList; // List of items to display
    private Fragment fragmentCur;

    public AdapterCentres(List<ClsCentre> itemList, Context context, Fragment fragment) {
        this.itemList = itemList;
        this.masterList = itemList;
        this.fragmentCur = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reyc_layout_display_centre, parent, false);
        return new ViewHolder(itemView, itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ClsCentre item = itemList.get(position);
        Glide.with(holder.itemView.getContext()).load(GlobalUrl.imageUrl + item.getlogo()).apply(RequestOptions.placeholderOf(R.drawable.placeholder_image)).into(holder.imageView);
        holder.txtAddress.setText(item.getCentreName());

    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAddress, txtName;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView, List<ClsCentre> itemList) {
            super(itemView);
            // Initialize views
            txtAddress = itemView.findViewById(R.id.dsCAddressview);
            imageView = itemView.findViewById(R.id.dsCLogoview);
            // Set click listener for the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ClsCentre centre = itemList.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("centre", centre);

                        NavHostFragment.findNavController(fragmentCur).navigate(R.id.go_spec_centre,bundle);
                    }
                }

            });
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void filter(String searchText) {
        //creates a filtered list to add the centres to
        List<ClsCentre> filteredList = new ArrayList<>();
        //loops through the master list
        for (ClsCentre centre : masterList) {
            //checks centre name
            if (centre.getCentreName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(centre);
            }
        }

        itemList = filteredList;
        notifyDataSetChanged();
    }


}
