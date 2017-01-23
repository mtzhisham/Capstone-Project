package com.moataz.eventboard.DataUtil;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.moataz.MultiDexApplication.eventboard.R;
import com.moataz.eventboard.ParserUtil.Event;

/**
 * Created by moataz on 22/01/17.
 */
public class CustomCEventAdapter extends RecyclerView.Adapter<CustomCEventAdapter.ViewHolder> {
    Cursor cursor;

    Context mContext;
    LayoutInflater inflater;

    private static ClickListener clickListener;

    public CustomCEventAdapter(Context context, Cursor cursor) {
        mContext = context;
        this.cursor = cursor;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


    public void setOnItemClickListener(ClickListener clickListener) {
        CustomCEventAdapter.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View eventsView = inflater.inflate(R.layout.raw_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        // Get the data model based on position
        cursor.moveToPosition(position);
        Gson gson = new Gson();

        Event event = gson.fromJson(cursor.getString(cursor.getColumnIndex("event")), Event.class);


        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(event.getName().getText());

        TextView idtextView = holder.dateTextView;
        idtextView.setText(event.getId());

        ImageView posterImageView = holder.imageView;

        try {
            Glide.with(mContext)
                    .load(event.getLogo().getOriginal().getUrl())
                    .crossFade()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(posterImageView);

        } catch (Exception e) {
            e.printStackTrace();

            posterImageView.setImageDrawable(mContext.getDrawable(R.mipmap.ic_launcher));
        }

    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }


    @Override
    public int getItemCount() {
        return cursor.getCount();
    }


}
