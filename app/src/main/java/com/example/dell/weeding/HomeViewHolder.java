package com.example.dell.weeding;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by DELL on 31-May-18.
 */

public class HomeViewHolder extends RecyclerView.ViewHolder {


    public TextView name;
    public ImageView image;

    public HomeViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.Name);
        image = itemView.findViewById(R.id.imageView);
    }

    public void bind(final Data item, final OnItemClickListener listener) {

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
    }

}
