package com.example.betabreaker.Classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betabreaker.R;

import java.util.List;

public class AdapterComments  extends RecyclerView.Adapter<AdapterComments.ViewHolder>{

    private List<ClsComment> itemList;

    @NonNull
    @Override
    public AdapterComments.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create a new ViewHolder
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reyc_layout_display_comment, parent, false);
        return new AdapterComments.ViewHolder(itemView, itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterComments.ViewHolder holder, int position) {
        // Bind data to views in the ViewHolder
        ClsComment item = itemList.get(position);


        holder.txtUsername.setText(item.getUsername());
        holder.txtComment.setText(item.getCommentCont());

    }

    @Override
    public int getItemCount() {
        // Return the number of items in the data source
        return itemList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtComment;
        public ViewHolder(@NonNull View itemView, List<ClsComment> itemList) {
            super(itemView);
            txtComment = itemView.findViewById(R.id.crvComment);
            txtUsername = itemView.findViewById(R.id.crvUsername);

        }
    }
}
