package com.example.moviews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowId;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    GridView mGridView;
    ProgressBar loadingbar;
    ArrayList<Movie> mMovies;
    CustomAdapter mCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = findViewById(R.id.gvGridView);
        loadingbar = findViewById(R.id.loadingbar);
        mMovies = new ArrayList<>();
        setTitle("Movie");
        mCustomAdapter = new CustomAdapter(this,R.layout.moview_layout, mMovies);
        mGridView.setAdapter(mCustomAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMovies.get(position);

                Intent intent = new Intent(getApplicationContext(), MovieDetail.class);
                intent.putExtra("id", movie.getId());
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("overview", movie.getOverview());
                intent.putExtra("poster", movie.getPoster());
                intent.putExtra("backdrop", movie.getBackdrop());

                startActivity(intent);
            }
        });

        String url = "https://api.themoviedb.org/3/movie/popular?api_key=b243dc06bac1b60355d79c1938f4da27&page=1&region=SG";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(MainActivity.this, "Failed to Connect to Internet!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(json);

                        JSONArray results = jsonObject.getJSONArray("results");
                        for(int i = 0; i < results.length(); i++) {
                            JSONObject result = results.getJSONObject(i);
                            int id = result.getInt("id");
                            String title = result.getString("title");
                            String overview = result.getString("overview");
                            String poster = "https://image.tmdb.org/t/p/w500" + result.getString("poster_path");
                            String backdrop = "https://image.tmdb.org/t/p/original" + result.getString("backdrop_path");

                            Movie movie = new Movie(id, title, overview, poster, backdrop);

                            mMovies.add(movie);

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mCustomAdapter.notifyDataSetChanged();

                                    loadingbar.setVisibility(View.INVISIBLE);
                                    mGridView.setVisibility(View.VISIBLE);
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
