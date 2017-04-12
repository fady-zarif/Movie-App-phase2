package com.example.fady.movienanoapp.Fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fady.movienanoapp.Adapter.TrailersAdapter;
import com.example.fady.movienanoapp.BuildConfig;
import com.example.fady.movienanoapp.Hight_of_list;
import com.example.fady.movienanoapp.DetailsActivity;
import com.example.fady.movienanoapp.Model.Movie;
import com.example.fady.movienanoapp.Model.MoviesProvider;
import com.example.fady.movienanoapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

    public MovieDetailsFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            Movie movie_details = getActivity().getIntent().getExtras().getParcelable("Movie_Details");
            String Id = movie_details.getId();
            MovieTrailers trailers = new MovieTrailers();
            trailers.execute("http://api.themoviedb.org/3/movie/" + Id + "/videos"); // passing the url of trailers in Asynctask
            MovieReviews movieReviews = new MovieReviews();
            movieReviews.execute("http://api.themoviedb.org/3/movie/" + Id + "/reviews");
        } catch (Exception exception) {
        }
    }

    ImageView Poster;
    TextView RDate, OverView, Vote;
    Movie movie;
    ListView listView, ReviewsList;
    TrailersAdapter trailersAdapter;
    List<String> Trailers;
    List<String> Reviews;
    ArrayAdapter ReviewsAdapter;
    Button favoriet;
    Movie favoriet_movie_object;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_details, container, false);
        Poster = (ImageView) root.findViewById(R.id.Movie_Poster);
        RDate = (TextView) root.findViewById(R.id.released_date);
        Vote = (TextView) root.findViewById(R.id.vote);
        OverView = (TextView) root.findViewById(R.id.overView);
        listView = (ListView) root.findViewById(R.id.TrailerList);
        ReviewsList = (ListView) root.findViewById(R.id.ReviewsList);
        favoriet = (Button) root.findViewById(R.id.Favoriet_Button);
        movie = getActivity().getIntent().getExtras().getParcelable("Movie_Details");
        ((DetailsActivity) getActivity()).setTitle(movie.getTitle());
        Trailers = new ArrayList<>();
        Reviews = new ArrayList<>();
        String baseUrl = "http://image.tmdb.org/t/p/w185/";
        Picasso.with(getContext()).load(baseUrl + movie.getPoster_path()).placeholder(R.drawable.loading_image).into(Poster);
        RDate.setText(movie.getRelease_date());
        OverView.setText(movie.getOverview());
        Vote.setText(movie.getVote_average());
        trailersAdapter = new TrailersAdapter(Trailers, getActivity());
        ReviewsAdapter = new ArrayAdapter(getActivity(), R.layout.review_textview, R.id.myreview_textview, Reviews);
        ReviewsList.setAdapter(ReviewsAdapter);
        listView.setAdapter(trailersAdapter);
        if (exist(movie.getTitle()) == 1) /// it will Search for title  in the database
        {
            favoriet.setBackgroundResource(android.R.drawable.star_big_on); // turn on the Star
        } else {
            favoriet.setBackgroundResource(android.R.drawable.star_big_off); // Star off
        }

        favoriet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // when favoriet Button clicked it will search for the title in the database

                if (exist(movie.getTitle()) == 1) // if it return 1 it is mean that the movie exist
                {
                    Toast.makeText(getContext(), "The Movie already Exists ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), " Saving....", Toast.LENGTH_SHORT).show();
                    ContentValues detailsvalues = new ContentValues();
                    detailsvalues.put(MoviesProvider.ORIGINAL_TITLE,
                            movie.getTitle());
                    detailsvalues.put(MoviesProvider.POSTER,
                            movie.getPoster_path());
                    detailsvalues.put(MoviesProvider.DATE,
                            movie.getRelease_date().substring(0, 4));
                    detailsvalues.put(MoviesProvider.RATE,
                            movie.getVote_average());
                    detailsvalues.put(MoviesProvider.OVERVIEW,
                            movie.getOverview());
                    Uri uri = getActivity().getContentResolver().insert(
                            MoviesProvider.CONTENT_URI, detailsvalues);
                    Toast.makeText(getContext(), " successfully saved  ", Toast.LENGTH_SHORT).show();
                    favoriet.setBackgroundResource(android.R.drawable.star_big_on);
                }
            }
        });

        return root;
    }

    public int exist(String Title) {
        int flag = 0;
        String URL = "content://com.movies.provider.movies/details";
        Uri movie = Uri.parse(URL);
        Log.e("ee", movie.toString());
        Cursor cursor = getContext().getContentResolver().query(movie, null, null, null, "Id");
        if (!cursor.moveToFirst()) {
            flag = 0;
        } else {
            do {
                if (Title.equals(cursor.getString(cursor.getColumnIndex(MoviesProvider.ORIGINAL_TITLE)))) {
                    flag = 1;
                    return flag;
                }
            } while (cursor.moveToNext());
        }
        return flag;
    }//end of the method

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public class MovieTrailers extends AsyncTask<String, Void, List<String>> {
        HttpURLConnection connection;
        InputStream inputStream;
        BufferedReader reader;
        StringBuffer buffer;

        @Override
        protected List<String> doInBackground(String... params) {
            String BaseUrl = params[0];
            String ApiKey = "?api_key=" + BuildConfig.OPEN_Movie_API_KEY;
            String full_url = BaseUrl.concat(ApiKey);
            try {
                URL url = new URL(full_url);
                Response(url);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Trailers;
        }

        void Response(URL url) throws IOException, JSONException {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            buffer = new StringBuffer();
            String js = null;
            while ((js = reader.readLine()) != null) {
                buffer.append(js + "\n");
            }
            String json = buffer.toString();
            List_of_Trailers(json);
        }

        void List_of_Trailers(String json) throws JSONException {
            JSONObject object = new JSONObject(json);
            JSONArray jsonArray = object.getJSONArray("results");
            int size = 0;
            if (jsonArray.length() > 3) {
                size = 3;
            } else
                size = jsonArray.length();

            for (int i = 0; i < size; i++) {
                JSONObject myobject = jsonArray.getJSONObject(i);
                String Key = myobject.getString("key");
                Trailers.add(Key);
            }
            Log.e("heey", String.valueOf(Trailers.size()));
        }

        @Override
        protected void onPostExecute(final List<String> list) {
            super.onPostExecute(list);
            trailersAdapter.notifyDataSetChanged();
            Hight_of_list.setListViewHeightBasedOnItems(listView);


        }

    }


    public class MovieReviews extends AsyncTask<String, Void, List<String>> {

        HttpURLConnection connection;
        InputStream inputStream;
        BufferedReader reader;
        StringBuffer buffer;

        @Override
        protected List<String> doInBackground(String... params) {
            String BaseUrl = params[0];
            String ApiKey = "?api_key=" + BuildConfig.OPEN_Movie_API_KEY;
            String full_url = BaseUrl.concat(ApiKey);
            try {
                URL url = new URL(full_url);
                Response(url);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Trailers;
        }

        void Response(URL url) throws IOException, JSONException {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            buffer = new StringBuffer();
            String js = null;
            while ((js = reader.readLine()) != null) {
                buffer.append(js + "\n");
            }
            String json = buffer.toString();
            List_of_Reviews(json);
        }

        void List_of_Reviews(String json) throws JSONException {
            JSONObject object = new JSONObject(json);
            JSONArray jsonArray = object.getJSONArray("results");
//            int size = 0;
//            if (jsonArray.length() > 3) {
//                size = 3;
//            } else
//                size = jsonArray.length();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject myobject = jsonArray.getJSONObject(i);
                String content = myobject.getString("content");
                Reviews.add(content);
            }
        }

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            ReviewsAdapter.notifyDataSetChanged();
            Hight_of_list.setListViewHeightBasedOnItems(ReviewsList);
        }
    }

}
