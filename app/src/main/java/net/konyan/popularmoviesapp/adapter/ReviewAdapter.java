package net.konyan.popularmoviesapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.konyan.popularmoviesapp.R;
import net.konyan.popularmoviesapp.data.ReviewColumn;

/**
 * Created by zeta on 8/14/16.
 */
public class ReviewAdapter extends  CursorRecyclerViewAdapter<ReviewAdapter.VideoHolder>{

    final Context context;

    public ReviewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
        //Log.d("cursor", ""+cursor.getCount());
    }

    @Override
    public void onBindViewHolder(VideoHolder viewHolder, Cursor cursor) {
        if (cursor != null){
            DatabaseUtils.dumpCursor(cursor);
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ReviewColumn.AUTHOR));
            String comment = cursor.getString(cursor.getColumnIndexOrThrow(ReviewColumn.CONTENT));
            viewHolder.tvAuthor.setText(name);
            viewHolder.tvComment.setText(comment);
        }

    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false));
    }

    class VideoHolder extends RecyclerView.ViewHolder{
        TextView tvAuthor, tvComment;
        public VideoHolder(View itemView) {
            super(itemView);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_review_name);
            tvComment = (TextView) itemView.findViewById(R.id.tv_review_content);
        }
    }
}
