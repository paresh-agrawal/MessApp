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

public class AdminOnLeaveAdapter extends RecyclerView.Adapter<AdminOnLeaveAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<AdminOnLeaveGetter> onLeaveItem;


    public AdminOnLeaveAdapter(Activity activity, ArrayList<AdminOnLeaveGetter> onLeaveItem){
        this.activity =activity;
        this.onLeaveItem = onLeaveItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.list_item_admin_on_leave,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.user_email_tv.setText(onLeaveItem.get(position).getUserEmail());
        holder.to_date_tv.setText(onLeaveItem.get(position).getToDate());
        holder.reason_tv.setText(onLeaveItem.get(position).getReason());
        holder.from_date_tv.setText(onLeaveItem.get(position).getFromDate());

    }

    public void setFilter(ArrayList<AdminOnLeaveGetter> details){
        onLeaveItem = new ArrayList<>();
        onLeaveItem.addAll(details);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (onLeaveItem !=null ? onLeaveItem.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView user_email_tv, to_date_tv, from_date_tv, reason_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            user_email_tv = (TextView)itemView.findViewById(R.id.user_email_tv);
            to_date_tv = (TextView)itemView.findViewById(R.id.to_tv);
            reason_tv = (TextView)itemView.findViewById(R.id.reason_tv);
            from_date_tv = (TextView)itemView.findViewById(R.id.from_tv);
        }
    }
}
