package com.moataz.eventboard.Syncing;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moataz on 19/01/17.
 */
public class EventsSyncAdapter extends AbstractThreadedSyncAdapter {

    private final AccountManager mAccountManager;

    public EventsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
    }


    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {


//        Log.d("udinic", "onPerformSync for account[" + account.name + "]");
//        try {
//            // Get the auth token for the current account
//            String authToken = mAccountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);
//            ParseComServerAccessor parseComService = new ParseComServerAccessor();
//
//            // Get shows from the remote server
//            List remoteTvShows = parseComService.getShows(authToken);
//
//            // Get shows from the local storage
//            ArrayList localTvShows = new ArrayList();
//            Cursor curTvShows = provider.query(TvShowsContract.CONTENT_URI, null, null, null, null);
//            if (curTvShows != null) {
//                while (curTvShows.moveToNext()) {
//                    localTvShows.add(TvShow.fromCursor(curTvShows));
//                }
//                curTvShows.close();
//            }
//            // TODO See what Local shows are missing on Remote
//
//            // TODO See what Remote shows are missing on Local
//
//            // TODO Updating remote tv shows
//
//            // TODO Updating local tv shows
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }
}
