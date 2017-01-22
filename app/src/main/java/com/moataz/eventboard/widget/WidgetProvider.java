package com.moataz.eventboard.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.moataz.MultiDexApplication.eventboard.R;
import com.moataz.eventboard.Syncing.EventsIntentService;
import com.moataz.eventboard.UI.Detail;

/**
 * Created by moataz on 21/01/17.
 */
public class WidgetProvider extends AppWidgetProvider {

    public static final String ACTION_DATA_UPDATED = "com.moataz.ACTION_DATA_UPDATED";

    private static final String LOG_TAG = WidgetProvider.class.getSimpleName();
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // update all widgets
        for (int widgetId: appWidgetIds){
            Intent intent = new Intent(context, EventWidgetRemoteViewService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,widgetId);

            // create Widget
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.collection_widget_layout);
            views.setRemoteAdapter(R.id.widgetCollectionList,intent);


            Intent intentEventDetail = new Intent(context, Detail.class);
            PendingIntent pendingIntent = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(intentEventDetail)
                    .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widgetCollectionList,pendingIntent);

            // Update Widget on HomeScreen
            appWidgetManager.updateAppWidget(widgetId,views);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_DATA_UPDATED)){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.widgetCollectionList);

        }
    }
}
