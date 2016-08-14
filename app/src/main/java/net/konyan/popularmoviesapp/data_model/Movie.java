package net.konyan.popularmoviesapp.data_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zeta on 8/13/16.
 */
public class Movie {

    @SerializedName("page")
    public int page;

    @SerializedName("results")
    public List<MovieModel> movies;

    public static class MovieModel{
        @SerializedName("poster_path")
        public String poster_path;
        @SerializedName("adult")
        public boolean adult;
        @SerializedName("overview")
        public String overview;
        @SerializedName("release_date")
        public String release_date;
        @SerializedName("id")
        public int id;
        @SerializedName("original_title")
        public String original_title;
        @SerializedName("original_language")
        public String original_language;
        @SerializedName("title")
        public String title;
        @SerializedName("backdrop_path")
        public String backdrop_path;
        @SerializedName("popularity")
        public float popularity;
        @SerializedName("vote_count")
        public int vote_count;
        @SerializedName("video")
        public boolean video;
        @SerializedName("vote_average")
        public float vote_average;
    }

    //total_results: 5146,
    //total_pages: 258
    @SerializedName("total_results")
    public int total_results;
    @SerializedName("total_pages")
    public int total_pages;

}
