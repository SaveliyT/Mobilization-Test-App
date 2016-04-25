package com.saveliy.third;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;


import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import android.util.Log;

import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;



import com.squareup.picasso.Picasso;

import java.security.spec.ECField;


public class SecondActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";
    private static final float SLIDE_LENGHT_IN_DP = 200.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);





        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        final TextView artistName = (TextView) findViewById(R.id.name);
        ImageView imageView = (ImageView) findViewById(R.id.ggImage);
        TextView songsAndAlbums = (TextView) findViewById(R.id.songsandalbums);
        TextView description = (TextView) findViewById(R.id.saDescription);
        TextView website = (TextView) findViewById(R.id.tvWebsite);
        TextView genres = (TextView) findViewById(R.id.genresCS);

        int id = intent.getIntExtra("pstn", 0); //getting position in ArrayList
        Picasso.with(this).load(MainActivity.musiciansList.get(id).getBigInage()).into(imageView);


        final String name = MainActivity.musiciansList.get(id).getName();
        setTitle(name);


        description.setText(MainActivity.musiciansList.get(id).getDescription());

        songsAndAlbums.setText(MainActivity.musiciansList.get(id).getSongs() + " Tracks "
        + MainActivity.musiciansList.get(id).getAlbums() + " Albums");

        genres.setText(MainActivity.musiciansList.get(id).getGenre());

        if (MainActivity.musiciansList.get(id).getWebsite() == null){
            ImageView imView = (ImageView) findViewById(R.id.icWebsite);
            imView.setVisibility(View.INVISIBLE);
            website.setVisibility(View.INVISIBLE);
        } else {
            website.setText(MainActivity.musiciansList.get(id).getWebsite());
        }

        artistName.setText(name);
     //   textView.setSelected(true);


        final float scale = getResources().getDisplayMetrics().density;
        final float lenght = (int) (SLIDE_LENGHT_IN_DP * scale + 0.5f); //lenght in dp when TextView witn musiciant name is disappear
        Log.d(LOG_TAG, "lenght = " + lenght );

        AppBarLayout mAppBar = (AppBarLayout) findViewById(R.id.app_bar);
        try {
            mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (verticalOffset <= 0 && verticalOffset >= -lenght) {
                        //Expanded
                        setTitle("");
                        artistName.setText(name);
                    }
                    if (verticalOffset <= -lenght) {

                        artistName.setText("");
                    }
                }
            });
        } catch(NullPointerException e) {e.printStackTrace();}
    }
}
