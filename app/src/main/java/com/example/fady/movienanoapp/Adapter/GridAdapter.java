package com.example.fady.movienanoapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.fady.movienanoapp.Model.Movie;
import com.example.fady.movienanoapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Fady on 2017-03-04.
 */

public class GridAdapter extends BaseAdapter {

    List<Movie> movieList;
    Context context;

    public GridAdapter(List<Movie> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    static class Holder {
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.grid_image, null);
            convertView = view;
            holder = new Holder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.PosterImage);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        String baseUrl = "http://image.tmdb.org/t/p/w185/";
        Picasso.with(context).load(baseUrl + movieList.get(position).getPoster_path()).placeholder(R.drawable.loading_image).into(holder.imageView);


        return convertView;
    }
}
