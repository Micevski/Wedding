package com.example.dell.weeding;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 26-Mar-18.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<Viewholder> {

    private List<Data> data;

    public RecycleViewAdapter() {
        this.data = new ArrayList<>();
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        Data item = data.get(position);
        holder.name.setText(item.getName());
        holder.address.setText(item.getAddress());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
