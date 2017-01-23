package com.moataz.eventboard.DataUtil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moataz.MultiDexApplication.eventboard.R;
import com.moataz.eventboard.ParserUtil.Event;

import java.util.List;

/**
 * Created by moataz on 19/01/17.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {


    private static ClickListener clickListener;
    // Store a member variable for the events
    private List<Event> mEvents;
    // Store the context for easy access
    private Context mContext;


    // Pass in the events array into the constructor
    public EventsAdapter(Context context, List<Event> mEvents) {
        this.mEvents = mEvents;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        EventsAdapter.clickListener = clickListener;
    }

    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View eventsView = inflater.inflate(R.layout.raw_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventsAdapter.ViewHolder holder, int position) {

        // Get the data model based on position
        Event event = mEvents.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(event.getName().getText());

        TextView datetextView = holder.dateTextView;
        datetextView.setText(event.getStart().getLocal());

        ImageView posterImageView = holder.imageView;

        try {

            Glide.with(getContext())
                    .load(event.getLogo().getOriginal().getUrl())
                    .crossFade()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(posterImageView);

        } catch (Exception e) {
            e.printStackTrace();

            posterImageView.setImageDrawable(getContext().getDrawable(R.mipmap.ic_launcher));
        }


    }

    @Override
    public int getItemCount() {
        return mEvents.size();

    }


    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView dateTextView;
        public ImageView imageView;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            nameTextView = (TextView) itemView.findViewById(R.id.event_name);
            dateTextView = (TextView) itemView.findViewById(R.id.event_date);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }


        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }


    }


}
