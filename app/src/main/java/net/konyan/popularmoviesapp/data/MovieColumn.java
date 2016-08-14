package net.konyan.popularmoviesapp.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by zeta on 8/14/16.
 */
/*
reference : SchematicPlanets (https://github.com/schordas/SchematicPlanets)
 */
public interface MovieColumn {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String POSTER_PATH = "poster_path";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String ORIGINAL_TITLE = "original_title";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String BACK_DROP_URL = "back_drop";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String PLOT = "plot";

    @DataType(DataType.Type.REAL)
    @NotNull
    String RATING = "rating";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String RELEASE_DATE = "release";

    /*extra column to filter user selected sorting and favorite*/
    @DataType(DataType.Type.TEXT)
    @NotNull
    String USER_SORT = "user_sort";

    @DataType(DataType.Type.TEXT)
    String USER_FAVORITE = "user_favorite";
}
