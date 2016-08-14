package net.konyan.popularmoviesapp.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by zeta on 8/14/16.
 */
public interface ReviewColumn {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String MOVIE_ID = "movie_id";


    @DataType(DataType.Type.TEXT)
    @NotNull
    String REVIEW_ID = "video_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String AUTHOR = "author";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String CONTENT = "content";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String URL = "url";

}
