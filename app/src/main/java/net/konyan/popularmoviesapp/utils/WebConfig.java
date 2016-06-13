package net.konyan.popularmoviesapp.utils;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zeta on 6/12/16.
 * Please insert api key @KEY_API to run the project.
 */
public class WebConfig {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static final String PATH_MOVIE = "movie";
    private static final String QUERY_API = "api_key";
    //TODO: change api key here!
    private static final String KEY_API = "your api key";

    public static final String PATH_POPULAR = "popular";
    public static final String PATH_TOP_RATED = "top_rated";

    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE_185 = "w185";
    private static final String POSTER_SIZE_342 = "w342";



    public static URL getMoviesUrl(String sort) throws MalformedURLException {
        Uri urlBuilder = Uri.parse(BASE_URL).buildUpon().appendPath(PATH_MOVIE)
                .appendPath(sort)
                .appendQueryParameter(QUERY_API, KEY_API)
                .build();
        return new URL(urlBuilder.toString());
    }

    public static URL getPosterUrl(String endPoint) throws MalformedURLException {
        StringBuilder builder = new StringBuilder(BASE_POSTER_URL).append(POSTER_SIZE_185).append(endPoint);
        return new URL(builder.toString());
    }

}
