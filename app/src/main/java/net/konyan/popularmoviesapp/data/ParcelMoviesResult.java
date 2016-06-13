package net.konyan.popularmoviesapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zeta on 6/12/16.
 */
public class ParcelMoviesResult implements Parcelable {

    public static final String PAGE = "page";
    public static final String RESULTS = "results";
    public static final String TOTAL_RESULTS = "total_results";
    public static final String TOTAL_PAGES = "total_pages";

    public static final String POSTER_PATH = "poster_path";
    public static final String ADULT = "adult";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DATE = "release_date";
    public static final String GENRE_IDS = "genre_ids";
    public static final String ID = "id";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String ORIGINAL_LANGUAGE = "original_language";
    public static final String TITLE = "title";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String POPULARITY = "popularity";
    public static final String VOTE_COUNT = "vote_count";
    public static final String VIDEO = "video";
    public static final String VOTE_AVERAGE = "vote_average";


    String poster_path;
    String original_title;
    String backdrop_path;
    String overview;
    double vote_average;
    String release_date;

    public ParcelMoviesResult(String poster_path, String original_title, String backdrop_path, String overview, double vote_average, String release_date) {
        this.poster_path = poster_path;
        this.original_title = original_title;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    protected ParcelMoviesResult(Parcel in) {
        this.poster_path = in.readString();
        this.original_title = in.readString();
        this.backdrop_path = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readDouble();
        this.release_date = in.readString();
    }

    public static final Creator<ParcelMoviesResult> CREATOR = new Creator<ParcelMoviesResult>() {
        @Override
        public ParcelMoviesResult createFromParcel(Parcel in) {
            return new ParcelMoviesResult(in);
        }

        @Override
        public ParcelMoviesResult[] newArray(int size) {
            return new ParcelMoviesResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(original_title);
        dest.writeString(backdrop_path);
        dest.writeString(overview);
        dest.writeDouble(vote_average);
        dest.writeString(release_date);
    }
}
