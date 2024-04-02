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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.betabreaker.Frags.FragSpecRoute;
import com.example.betabreaker.R;

import java.util.List;

public class AdapterRoutes extends RecyclerView.Adapter<AdapterRoutes.ViewHolder> {

    private List<ClsRoutes> itemList;
    private String centreID;
    private Context context;
    private Fragment fragment;

    public AdapterRoutes(List<ClsRoutes> itemList, String centreID, Context context, Fragment fragment) {
        this.itemList = itemList;
        this.centreID = centreID;
        this.context = context;
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
                            Log.d("Check", String.valueOf(fragment));

                            FragSpecRoute newFrag = new FragSpecRoute();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("viewRoute", route);
                            newFrag.setArguments(bundle);
//This is displaying over the op of nthe other Fragments, It should make a Fragment display ID of FragOnions which is displaying FragLayoutLettuce display the new fragment
                            fragmentTransaction.replace(R.id.FragOnions, newFrag);
                            /*
                            if (fragment instanceof Lettace) {
                                Log.d("Check", String.valueOf(fragment));
                                FragSpecRoute newFrag = new FragSpecRoute();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("viewRoute", route);
                                newFrag.setArguments(bundle);
//This is displaying over the op of nthe other Fragments, It should make a Fragment display ID of FragOnions which is displaying FragLayoutLettuce display the new fragment
                                fragmentTransaction.replace(R.id.FragOnions, newFrag);
                            } else {
                                FragSpecRoute newFrag = new FragSpecRoute();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("viewRoute", route);
                                newFrag.setArguments(bundle);
                                fragmentTransaction.replace(R.id.fragmentContainerView, newFrag);
                            }

                             */
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }
                }
            });
        }
    }
}