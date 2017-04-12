package com.example.fady.movienanoapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fady on 2017-03-04.
 */

public class Movie implements Parcelable {


    String id;
    String title;
    String overview;
    String poster_path;
    String release_date;
    String vote_average;

    public Movie() {
    }

    public Movie(String id, String title, String overview, String poster_path, String release_date, String vote_average) {
        this.title = title;
        this.id = id;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.vote_average = vote_average;
    }


    protected Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        release_date = in.readString();
        vote_average = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(String poster, String overview, String date, String title, String rate) {
        this.poster_path = poster;
        this.overview = overview;
        this.release_date = date;
        this.title = title;
        this.vote_average = rate;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getId() {
        return id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(poster_path);
        dest.writeString(release_date);
        dest.writeString(vote_average);
    }
}
