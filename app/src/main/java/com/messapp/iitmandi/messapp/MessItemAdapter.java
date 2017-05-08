package com.messapp.iitmandi.messapp;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by root on 23/3/17.
 */

public class MessItemAdapter extends RecyclerView.Adapter<MessItemAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<MenuItem> messItem;


    public MessItemAdapter(Activity activity, ArrayList<MenuItem> messItem){
        this.activity =activity;
        this.messItem = messItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mess_item.setText(messItem.get(position).getMessItem());
    }

    public void setFilter(ArrayList<MenuItem> details){
        messItem = new ArrayList<>();
        messItem.addAll(details);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (messItem !=null ? messItem.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mess_item;

        public ViewHolder(View itemView) {
            super(itemView);
            mess_item = (TextView)itemView.findViewById(R.id.mess_item);
        }
    }
}
