package com.example.fady.movienanoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fady.movienanoapp.R;

import java.util.List;

/**
 * Created by Fady on 2017-03-30.
 */

public class TrailersAdapter extends BaseAdapter {
    List<String> list;
    Context context;

    public TrailersAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class viewHolder {
        ImageView imageView;
        LinearLayout layout;
        TextView textView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewHolder holder = new viewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.trailer_box, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.video_image);
            holder.textView = (TextView) convertView.findViewById(R.id.video_text);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.trailer_layout);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        int x = position + 1;
        holder.textView.setText("Trailer " + x);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = list.get(position);
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key))); // start new intent for trailer
            }
        });
        return convertView;
    }
}
