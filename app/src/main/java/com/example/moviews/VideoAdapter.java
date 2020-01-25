package com.example.moviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoAdapter extends ArrayAdapter<Video> {

    private Context mContext;
    private int mResource;
    private ArrayList<Video> mVideos;

    private static class ViewHolder {
        ImageView ivThumbnail;
    }

    public VideoAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Video> videos) {
        super(context, resource, videos);

        mContext = context;
        mResource = resource;
        mVideos = videos;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = layoutInflater.inflate(mResource, parent, false);
            viewHolder.ivThumbnail = convertView.findViewById(R.id.ivThumbnail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Video video = mVideos.get(position);

        Picasso.with(mContext).load(video.getThumbnail()).into(viewHolder.ivThumbnail);

        return convertView;
    }
}
