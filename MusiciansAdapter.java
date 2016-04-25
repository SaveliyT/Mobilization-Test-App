package com.saveliy.third;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;

public class MusiciansAdapter extends ArrayAdapter<Musicians> {

    ArrayList<Musicians> musicianList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;


    public MusiciansAdapter(Context context, int resource, ArrayList<Musicians> objects){
        super(context, resource, objects);

        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        musicianList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
   //     return super.getView(position, convertView, parent);

        View v = convertView;

        if(v == null){
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.imageview = (ImageView) v.findViewById(R.id.ivImage);
            holder.tvArtistName = (TextView) v.findViewById(R.id.tvartistname);
            holder.tvGenre = (TextView) v.findViewById(R.id.tvgenre);
            holder.tvTracks = (TextView) v.findViewById(R.id.tvtracks);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.imageview.setImageResource(R.mipmap.ic_launcher);
       // Picasso.with(getContext()).setIndicatorsEnabled(true);
        Picasso.with(getContext()).
                load(musicianList.get(position).getSmallImage()).into(holder.imageview); //external library picasso is used

        holder.tvArtistName.setText(musicianList.get(position).getName());
        holder.tvGenre.setText(musicianList.get(position).getGenre());
        holder.tvTracks.setText("Songs: " + musicianList.get(position).getSongs() + "  Albums: " +
                musicianList.get(position).getAlbums());
        return v;

    }

    static class ViewHolder{
        public ImageView imageview;
        public TextView tvArtistName;
        public TextView tvGenre;
        public TextView tvTracks;

    }

}
