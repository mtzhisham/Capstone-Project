package com.moataz.eventboard.UI;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.moataz.MultiDexApplication.eventboard.R;

import icepick.Icepick;

public class Detail extends AppCompatActivity {


    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Icepick.restoreInstanceState(this, savedInstanceState);


        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.admob_App_ID));

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(getResources().getString(R.string.test_device)).build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

}

