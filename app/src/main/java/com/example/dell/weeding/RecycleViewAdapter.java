package com.example.dell.weeding;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 26-Mar-18.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<Viewholder> {

    private final OnItemClickListener listener;

    private Context context;
    private List<Data> data;


    public RecycleViewAdapter(Context context, OnItemClickListener listener) {
        this.data = new ArrayList<>();
        this.context = context;
        this.listener = listener;
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
        holder.bind(data.get(position),listener);
        Glide.with(context).load(item.getHomeImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return data;
    }
}
