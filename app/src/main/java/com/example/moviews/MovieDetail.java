package com.example.moviews;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieDetail extends AppCompatActivity {

    ImageView ivPoster;
    ImageView ivBackdrop;
    TextView tvTitle;
    TextView tvOverview;

    ArrayList<Video> mVideos;
    ListView mListView;
    VideoAdapter mVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mVideos = new ArrayList<>();

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("title");
        String overview = intent.getStringExtra("overview");
        String poster = intent.getStringExtra("poster");
        String backdrop = intent.getStringExtra("backdrop");

        ivPoster = findViewById(R.id.ivPoster);
        ivBackdrop = findViewById(R.id.ivBackdrop);
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        mListView = findViewById(R.id.horizontalvideo);

        mVideoAdapter = new VideoAdapter(this, R.layout.video_layout, mVideos);
        mListView.setAdapter(mVideoAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video video = mVideos.get(position);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getVideo()));
                startActivity(intent);

            }
        });

        setTitle(title);

        Picasso.with(this).load(poster).into(ivPoster);
        Picasso.with(this).load(backdrop).into(ivBackdrop);

        tvTitle.setText(title);
        tvOverview.setText(overview);

        String url = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=b243dc06bac1b60355d79c1938f4da27&language=en-US";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("###", "onFailure: " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Log.d("###", "onResponse: " + json);

                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray results = jsonObject.getJSONArray("results");

                        for (int i=0;i< results.length();i++) {
                            JSONObject result = results.getJSONObject(i);

                            String site = result.getString("site");
                            String key = result.getString("key");


                            if (site.equals("YouTube")) {
                                String videoUrl = "https://www.youtube.com/watch?v=" + key;
                                String thumbnail = "https://img.youtube.com/vi/" + key + "/hqdefault.jpg";
                                Video video = new Video(thumbnail, videoUrl);

                                Log.d("###", "onResponse: " + thumbnail);
                                Log.d("###", "onResponse: " + videoUrl);

                                mVideos.add(video);
                            }

                            MovieDetail.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mVideoAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }
}
