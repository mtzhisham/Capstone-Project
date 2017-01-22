package com.moataz.eventboard.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.gson.Gson;
import com.moataz.eventboard.DataUtil.EventsProvider;

import com.moataz.MultiDexApplication.eventboard.R;
import com.moataz.eventboard.ParserUtil.Event;

/**
 * Created by moataz on 23/09/16.
 */



public class EventWidgetRemoteViewService extends RemoteViewsService {

    static final Uri CONTENT_URL =
            Uri.parse("content://com.moataz.eventboard.DataUtil.EventsProvider/cpevents");

    private AppWidgetTarget appWidgetTarget;

    private static final String LOG_TAG = EventWidgetRemoteViewService.class.getSimpleName();
    public EventWidgetRemoteViewService() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetItemRemoteView(this.getApplicationContext(),intent);
    }

    class WidgetItemRemoteView implements RemoteViewsService.RemoteViewsFactory{
        Context mContext;
        Cursor mCursor;
        Intent mIntent;

        public WidgetItemRemoteView(Context mContext, Intent mIntent) {
            this.mContext = mContext;
            this.mIntent = mIntent;
        }

        @Override
        public void onCreate() {
            // nothing To DO

        }

        @Override
        public int getCount() {
            return mCursor != null ? mCursor.getCount() : 0;
        }

        @Override
        public void onDataSetChanged() {


            if (mCursor!=null)
                mCursor.close();

            final long pId = Binder.clearCallingIdentity();
            String[] projection = new String[]{"id", "eDBID","event"};
            mCursor = getContentResolver().query(
                   CONTENT_URL,
                    projection, null, null, null
            );

            Binder.restoreCallingIdentity(pId);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            try{
                mCursor.moveToPosition(position);


                Gson gson = new Gson();
                Event event = gson.fromJson(mCursor.getString(mCursor.getColumnIndex("event")), Event.class);


                RemoteViews listItemRemoteView = new RemoteViews(mContext.getPackageName(), R.layout.list_item_event_widget);
                listItemRemoteView.setTextViewText(R.id.event_name,event.getName().getText());
                listItemRemoteView.setTextViewText(R.id.event_date,event.getStart().getLocal());






                // set Onclick Item Intent
                Intent onClickItemIntent = new Intent();

                onClickItemIntent.putExtra("Event", event);
                listItemRemoteView.setOnClickFillInIntent(R.id.list_item_event_widget,onClickItemIntent);
                return listItemRemoteView;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getLong(mCursor.getColumnIndex("id"));
        }

        @Override
        public void onDestroy() {
            if (mCursor!=null)
                mCursor.close();
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}

