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

public class AdminFeedItemAdapter extends RecyclerView.Adapter<AdminFeedItemAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<AdminFeedGetter> feedItem;


    public AdminFeedItemAdapter(Activity activity, ArrayList<AdminFeedGetter> feedItem){
        this.activity =activity;
        this.feedItem = feedItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.list_item_admin_feedback,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.user_email_tv.setText(feedItem.get(position).getUserEmail());
        holder.item_name_tv.setText(feedItem.get(position).getItemName());
        holder.rating_tv.setText(feedItem.get(position).getRating());
        holder.feed_text_tv.setText(feedItem.get(position).getFeedText());
        holder.meal_tv.setText(feedItem.get(position).getMeal());

    }

    public void setFilter(ArrayList<AdminFeedGetter> details){
        feedItem = new ArrayList<>();
        feedItem.addAll(details);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (feedItem !=null ? feedItem.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView user_email_tv,item_name_tv,feed_text_tv,rating_tv,meal_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            user_email_tv = (TextView)itemView.findViewById(R.id.user_email_tv);
            item_name_tv = (TextView)itemView.findViewById(R.id.item_name_tv);
            rating_tv = (TextView)itemView.findViewById(R.id.rating_tv);
            feed_text_tv = (TextView)itemView.findViewById(R.id.feed_text_tv);
            meal_tv = (TextView)itemView.findViewById(R.id.meal_tv);
        }
    }
}
