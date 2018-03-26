package com.example.dell.weeding;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by DELL on 26-Mar-18.
 */

public class Viewholder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView address;

    public Viewholder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.Name);
        address = itemView.findViewById(R.id.textViewAddress);
    }
}
