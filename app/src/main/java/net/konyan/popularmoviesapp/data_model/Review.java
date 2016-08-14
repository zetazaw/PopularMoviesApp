package net.konyan.popularmoviesapp.data_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zeta on 8/13/16.
 */
public class Review {
    @SerializedName("id")
    public int id;
    @SerializedName("page")
    public int page;
    @SerializedName("results")
    public List<ReviewModel> results;
    @SerializedName("total_pages")
    public int total_pages;
    @SerializedName("total_results")
    public int total_results;

    public static class ReviewModel{
        @SerializedName("id")
        public String id;
        @SerializedName("author")
        public String author;
        @SerializedName("content")
        public String content;
        @SerializedName("url")
        public String url;
    }

}
