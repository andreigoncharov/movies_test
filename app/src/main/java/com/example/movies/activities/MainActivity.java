package com.example.movies.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movies.R;
import com.example.movies.data.MovieAdapter;
import com.example.movies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movies;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movies = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

    }

    private void getMovie(String name) {
        String url = String.format("http://www.omdbapi.com/?apikey=95d41d4d&s=%s", name);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Search");

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String title = jsonObject.getString("Title");
                        String year = jsonObject.getString("Year");
                        String posterURL = jsonObject.getString("Poster");

                        Movie movie = new Movie();
                        movie.setTitle(title);
                        movie.setYear(year);
                        movie.setPosterURL(posterURL);

                        movies.add(movie);
                    }

                    movieAdapter =  new MovieAdapter(MainActivity.this,  movies);
                    recyclerView.setAdapter(movieAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }

    public void search(View view) {
        TextView nameTextView = findViewById(R.id.editTextTextPersonName);
        String name = nameTextView.getText().toString();
        getMovie(name);
    }
}