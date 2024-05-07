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

public class AdapterRoutes extends RecyclerView.Adapter<AdapterRoutes.ViewHolder> {

    private final List<ClsRoutes> itemList;
    private final Fragment fragment;

    public AdapterRoutes(List<ClsRoutes> itemList, String centreID, Context context, Fragment fragment) {
        this.itemList = itemList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reyc_layout_display_routes, parent, false);
        return new ViewHolder(itemview);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //sets the displayed values for each individual card view
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

            cardView = itemView.findViewById(R.id.dsRRecCar);
            tvColour = itemView.findViewById(R.id.dsRColour);
            tvGrade = itemView.findViewById(R.id.dscRGrade);
            tvArea = itemView.findViewById(R.id.dsRArea);
            ivClimb = itemView.findViewById(R.id.dsRClimbImage);

            //Creates the onclick listener
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
                            NavHostFragment.findNavController(fragment).navigate(R.id.go_spec_route,bundle);
                        }
                    }
                }
            });
        }
    }
}
