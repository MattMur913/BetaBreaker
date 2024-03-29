package com.example.betabreaker.Classes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.betabreaker.ActAdminViews;
import com.example.betabreaker.Frags.FragEditSpecRoute;
import com.example.betabreaker.Frags.FragSpecRoute;
import com.example.betabreaker.R;

import java.util.List;

public class AdapterRoutes extends RecyclerView.Adapter<AdapterRoutes.ViewHolder> {

    private List<ClsRoutes> itemList;
    private String centreID;
    private Context context;

    public AdapterRoutes(List<ClsRoutes> itemList, String centreID, Context context) {
        this.itemList = itemList;
        this.centreID = centreID;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reyc_layout_display_routes, parent, false);
        return new ViewHolder(itemview, itemList, centreID);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvColour, tvArea, tvGrade;
        CardView cardView;
        ImageView ivClimb;

        public ViewHolder(@NonNull View itemView, List<ClsRoutes> itemList, String centreID) {
            super(itemView);

            cardView = itemView.findViewById(R.id.dsRRecCar);
            tvColour = itemView.findViewById(R.id.dsRColour);
            tvGrade = itemView.findViewById(R.id.dscRGrade);
            tvArea = itemView.findViewById(R.id.dsRArea);
            ivClimb = itemView.findViewById(R.id.dsRClimbImage);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ClsRoutes route = itemList.get(position);
                        Context context = itemView.getContext();
                        if (context instanceof AppCompatActivity) {
                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            if (context instanceof ActAdminViews) {
                                FragEditSpecRoute fragment = new FragEditSpecRoute();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("viewRoute", route);
                                bundle.putSerializable("centreID", centreID);
                                fragment.setArguments(bundle);

                                fragmentTransaction.replace(R.id.dsRLayout, fragment);
                            }else{
                                FragSpecRoute fragment = new FragSpecRoute();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("viewRoute", route);
                                fragment.setArguments(bundle);

                                fragmentTransaction.replace(R.id.dsRLayout, fragment);
                            }
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            RecyclerView recyclerView = ((Activity) context).findViewById(R.id.dsRRec);
                            recyclerView.setVisibility(View.GONE);


                        }
                    }
                }
            });
        }
    }
}