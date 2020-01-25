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
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Movie> {

    private Context mContext;
    private int mResource;
    private ArrayList<Movie> mMovies;

    public static class ViewHolder {
        ImageView poster;
    }

    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Movie> movies) {
        super(context, resource, movies);

        mContext = context;
        mResource = resource;
        mMovies = movies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = layoutInflater.inflate(mResource, parent, false);
            viewHolder.poster = convertView.findViewById(R.id.ivCover);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Movie movie = mMovies.get(position);

        Picasso.with(mContext).load(movie.getPoster()).into(viewHolder.poster);

        return convertView;
    }
}
