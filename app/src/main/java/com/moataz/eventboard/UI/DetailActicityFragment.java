package com.moataz.eventboard.UI;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.moataz.MultiDexApplication.eventboard.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActicityFragment extends Fragment {

    public DetailActicityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_detail_acticity, container, false);
        Intent intent = new Intent();
        ImageView imageView = (ImageView) view.findViewById(R.id.photo);
        TextView textView = (TextView) view.findViewById(R.id.article_title);
        textView.setText(intent.getStringExtra("name"));



        Glide.with(getContext())
                .load(intent.getStringExtra("url"))
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);

        return view;
    }
}
