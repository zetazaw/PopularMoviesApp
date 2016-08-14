package net.konyan.popularmoviesapp.utils;

import android.net.Uri;

import net.konyan.popularmoviesapp.data_model.Movie;
import net.konyan.popularmoviesapp.data_model.Review;
import net.konyan.popularmoviesapp.data_model.Video;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by zeta on 6/12/16.
 * Please insert api key @KEY_API to run the project.
 */
public class WebConfig {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static final String YOU_TUBE_URL = "https://www.youtube.com";
    private static final String PATH_VIDEO = "watch";
    private static final String PATH_MOVIE = "movie";
    private static final String QUERY_VIDEO = "v";
    private static final String QUERY_API = "api_key";
    //TODO: change api key here!
    private static final String KEY_API = "44932de373ddb6cfa053f74c1947dd99";

    public static final String PATH_POPULAR = "popular";
    public static final String PATH_TOP_RATED = "top_rated";
    public static final String PATH_FAVORITE = "favorite";

    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE_185 = "w185";
    private static final String POSTER_SIZE_342 = "w342";



    public static Uri getVideoUrl(String key) {
        return Uri.parse(YOU_TUBE_URL).buildUpon().appendPath(PATH_VIDEO)
                .appendQueryParameter(QUERY_VIDEO, key)
                .build();
    }

    public static URL getPosterUrl(String endPoint){
        StringBuilder builder = new StringBuilder(BASE_POSTER_URL).append(POSTER_SIZE_185).append(endPoint);
        try {
            return new URL(builder.toString());
        }catch (MalformedURLException mfe){
            return null;
        }
    }

    /*
    fix api_key query reference from
    https://futurestud.io/blog/retrofit-2-how-to-add-query-parameters-to-every-request
     */
    private static Retrofit retrofit;
    private static Retrofit getRetro(){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        HttpUrl originalHttpUrl = chain.request().url();
                        //fix api_key query
                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("api_key", KEY_API)
                                .build();
                        Request.Builder requestBuilder = chain.request().newBuilder()
                                .url(url);
                        return chain.proceed(requestBuilder.build());
                    }
                })
                .build();

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Call getMovies(String sort){
        return getRetro().create(MovieApi.class).getMovies(sort);
    }

    public static Call getTrailers(int moieId){
        return getRetro().create(MovieApi.class).getTrailers(moieId);
    }

    public static Call getReviews(int moieId){
        return getRetro().create(MovieApi.class).getReviews(moieId);
    }


    private interface MovieApi {
        //http://api.themoviedb.org/3/movie/top_rated?api_key=44932de373ddb6cfa053f74c1947dd99
        @Headers({
                "Content-Type: application/json",
                "Accept-Charset: utf-8",
        })
        @GET("movie/{sort}")
        Call<Movie> getMovies(@Path("sort") String sort);

        //http://api.themoviedb.org/3/movie/244786/videos?api_key=44932de373ddb6cfa053f74c1947dd99
        @Headers({
                "Content-Type: application/json",
                "Accept-Charset: utf-8",
        })
        @GET("movie/{id}/videos")
        Call<Video> getTrailers(@Path("id") int movieId);

        //http://api.themoviedb.org/3/movie/244786/reviews?api_key=44932de373ddb6cfa053f74c1947dd99s
        @Headers({
                "Content-Type: application/json",
                "Accept-Charset: utf-8",
        })
        @GET("movie/{id}/reviews")
        Call<Review> getReviews(@Path("id") int movieId);
    }


}
