package net.konyan.popularmoviesapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.konyan.popularmoviesapp.R;
import net.konyan.popularmoviesapp.data.VideoColumn;

/**
 * Created by zeta on 8/14/16.
 */
public class VideoAdapter extends  CursorRecyclerViewAdapter<VideoAdapter.VideoHolder>{

    final Context context;

    final TrailerClickListener listener;

    public VideoAdapter(Context context, Cursor cursor, TrailerClickListener listener) {
        super(context, cursor);
        this.context = context;
        this.listener = listener;
        //Log.d("cursor", ""+cursor.getCount());
    }

    @Override
    public void onBindViewHolder(VideoHolder viewHolder, Cursor cursor) {
        if (cursor != null){
            String name = cursor.getString(cursor.getColumnIndexOrThrow(VideoColumn.VIDEO_NAME));
            final String videoKey = cursor.getString(cursor.getColumnIndexOrThrow(VideoColumn.VIDEO_KEY));
            viewHolder.tvTrailerName.setText(name);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(videoKey);
                }
            });
        }

    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent, false));
    }

    class VideoHolder extends RecyclerView.ViewHolder{
        TextView tvTrailerName;
        public VideoHolder(View itemView) {
            super(itemView);
            tvTrailerName = (TextView) itemView.findViewById(R.id.tv_trailer_name);
        }
    }

    public interface TrailerClickListener{
        void onItemClick(String videoKey);
    }
}
