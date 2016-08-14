package net.konyan.popularmoviesapp.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by zeta on 8/14/16.
 */
public interface VideoColumn {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String MOVIE_ID = "movie_id";


    @DataType(DataType.Type.TEXT)
    @NotNull
    String VIDEO_ID = "video_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String VIDEO_KEY = "video_key";


    @DataType(DataType.Type.TEXT)
    @NotNull
    String VIDEO_NAME = "video_name";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String VIDEO_SITE = "video_site";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String VIDEO_QTY = "video_quality";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String VIDEO_TYPE = "video_type";
}
