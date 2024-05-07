package com.example.betabreaker.Classes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.betabreaker.R;

import java.util.List;

public class AdapterEditRoutes extends RecyclerView.Adapter<AdapterEditRoutes.ViewHolder> {

    private final List<ClsRoutes> itemList;
    private final Fragment fragment;
    private final String centreID;


    //Creates the adapter
    public AdapterEditRoutes(List<ClsRoutes> itemList, String centreID, Fragment fragment) {
        this.itemList = itemList;
        this.fragment = fragment;
        this.centreID=centreID;
    }

    @NonNull
    @Override
    public AdapterEditRoutes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reyc_layout_display_routes, parent, false);
        return new AdapterEditRoutes.ViewHolder(itemview);
    }

    public void onBindViewHolder(@NonNull AdapterEditRoutes.ViewHolder holder, int position) {
        //Sets the widgets information to match the position
        ClsRoutes item = itemList.get(position);
        Glide.with(holder.itemView.getContext()).load(GlobalUrl.imageUrl + item.getImage())
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder_image))
                .into(holder.ivClimb);
        holder.tvArea.setText(item.getArea());
        holder.tvColour.setText(item.getColour());
        holder.tvGrade.setText(item.getGrades());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvColour, tvArea, tvGrade;
        CardView cardView;
        ImageView ivClimb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Sets each of the itemViews
            cardView = itemView.findViewById(R.id.dsRRecCar);
            tvColour = itemView.findViewById(R.id.dsRColour);
            tvGrade = itemView.findViewById(R.id.dscRGrade);
            tvArea = itemView.findViewById(R.id.dsRArea);
            ivClimb = itemView.findViewById(R.id.dsRClimbImage);

            //Creates an onclick listener for the Cardview
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ClsRoutes route = itemList.get(position);
                        Context context = itemView.getContext();
                        if (context != null && fragment != null) {
                            //Passes necessary values along
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("viewRoute", route);
                            bundle.putSerializable("centreID", centreID);
                            NavHostFragment.findNavController(fragment).navigate(R.id.go_edit_route,bundle);

                        }
                    }
                }
            });
        }
    }
}

