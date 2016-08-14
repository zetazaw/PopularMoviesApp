package net.konyan.popularmoviesapp.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by zeta on 8/14/16.
 */
/*
reference : SchematicPlanets (https://github.com/schordas/SchematicPlanets)
 */
@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public class MovieProvider {

    public static final String AUTHORITY = "net.konyan.popularmoviesapp";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String MOVIES = "movies";
        String VIDEOS = "videos";
        String REVIEWS = "reviews";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.MOVIES)
    public static class Movies {
        @ContentUri( path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movie"/*,
                defaultSort = MovieColumn.ORIGINAL_TITLE+ " ASC"*/)
        public static final Uri CONTENT_URI = buildUri(Path.MOVIES);

        @InexactContentUri(
                name = "MOVIE_ID",
                path = Path.MOVIES + "/#",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumn._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.MOVIES, String.valueOf(id));
        }

    }

    @TableEndpoint(table = MovieDatabase.VIDEOS)
    public static class Videos {
        @ContentUri( path = Path.VIDEOS,
                type = "vnd.android.cursor.dir/videos")
        public static final Uri CONTENT_URI = buildUri(Path.VIDEOS);

        @InexactContentUri(
                name = "VIDEO_ID",
                path = Path.VIDEOS + "/#",
                type = "vnd.android.cursor.item/video",
                whereColumn = VideoColumn._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.VIDEOS, String.valueOf(id));
        }
    }

    @TableEndpoint(table = MovieDatabase.REVIEWS)
    public static class Reviews {
        @ContentUri( path = Path.REVIEWS,
                type = "vnd.android.cursor.dir/reviews")
        public static final Uri CONTENT_URI = buildUri(Path.REVIEWS);

        @InexactContentUri(
                name = "REVIEW_ID",
                path = Path.REVIEWS + "/#",
                type = "vnd.android.cursor.item/review",
                whereColumn = ReviewColumn._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.REVIEWS, String.valueOf(id));
        }
    }
}
