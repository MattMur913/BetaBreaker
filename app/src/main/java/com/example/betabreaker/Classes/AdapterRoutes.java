package com.example.betabreaker.Classes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.betabreaker.BurgerToppings.Lettace;
import com.example.betabreaker.Frags.FragSpecRoute;
import com.example.betabreaker.R;

import java.util.List;

public class AdapterRoutes extends RecyclerView.Adapter<AdapterRoutes.ViewHolder> {

    private List<ClsRoutes> itemList;
    private String centreID;
    private Context context;
    private Fragment fragment;
    private AdapterCallback callback;

    public AdapterRoutes(List<ClsRoutes> itemList, String centreID, Context context, Fragment fragment) {
        this.itemList = itemList;
        this.centreID = centreID;
        this.context = context;
        this.fragment = fragment;
    }

    public interface AdapterCallback {
        void onItemClicked(ClsRoutes route);
    }

    // Method to set the callback
    public void setAdapterCallback(AdapterCallback callback) {
        this.callback = callback;
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
                        Log.d("TAG", String.valueOf(fragment));
                        if (context != null && fragment != null) {
                            FragmentManager fragmentManager = fragment.requireActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            FragSpecRoute newFrag = new FragSpecRoute();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("viewRoute", route);
                            newFrag.setArguments(bundle);

                            if (fragment instanceof Lettace) {
                                //This transaction
                                Log.d("TAG", String.valueOf(fragment));
                                ConstraintLayout dlFrag = ((Activity) context).findViewById(R.id.FragLayoutLettuce);
                                dlFrag.setVisibility(View.GONE);
                                fragmentTransaction.replace(R.id.FragOnions, newFrag);
                            } else {
                                fragmentTransaction.replace(R.id.fragmentContainerView, newFrag);
                                RecyclerView recyclerView;
                                recyclerView = ((Activity) context).findViewById(R.id.dsRRec);
                                recyclerView.setVisibility(View.GONE);
                            }
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }
                }
            });
        }
    }
}
