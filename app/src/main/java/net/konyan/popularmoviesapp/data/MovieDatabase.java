package net.konyan.popularmoviesapp.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by zeta on 8/14/16.
 */
/*
reference : SchematicPlanets (https://github.com/schordas/SchematicPlanets)
 */
@Database(version = MovieDatabase.VERSION)
public final class MovieDatabase {

    public static final int VERSION = 1;

    private MovieDatabase(){}

    @Table(MovieColumn.class) public static final String MOVIES = "movies";

    @Table(VideoColumn.class) public static final String VIDEOS = "videos";

    @Table(ReviewColumn.class) public static final String REVIEWS = "reviews";
}
