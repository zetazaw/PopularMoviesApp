package net.konyan.popularmoviesapp.data_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zeta on 8/13/16.
 */
public class Video {
    @SerializedName("id")
    public int id;
    @SerializedName("results")
    public List<VideoModel> results;

    public static class VideoModel{
        @SerializedName("id")
        public String id;
        @SerializedName("iso_639_1")
        public String iso_639_1;
        @SerializedName("iso_3166_1")
        public String iso_3166_1;
        @SerializedName("key")
        public String key;
        @SerializedName("name")
        public String name;
        @SerializedName("site")
        public String site;
        @SerializedName("size")
        public int size;
        @SerializedName("type")
        public String type;
    }
}
