package com.kynangso.net.mysmile_jokes.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.kynangso.net.mysmile_jokes.MainActivity;
import com.kynangso.net.mysmile_jokes.R;
import com.kynangso.net.mysmile_jokes.database.DatabaseManager;
import com.kynangso.net.mysmile_jokes.interfaces.IFavoriteListener;
import com.kynangso.net.mysmile_jokes.interfaces.OpenPageReadListener;
import com.kynangso.net.mysmile_jokes.model.Story;

import java.util.ArrayList;
import java.util.List;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {
    List<Story> stories;
    Context context;
    int layout;
    private IFavoriteListener favorite;
    private OpenPageReadListener openPageReadListener;
    boolean isFavorite = false;

    public HistoryRecyclerAdapter(ArrayList<Story> stories, Context context, int layout) {
        this.stories = stories;
        this.context = context;
        this.layout = layout;
    }

    public void setListener(IFavoriteListener favorite) {
        this.favorite = favorite;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout, null, false);
        if (context instanceof MainActivity) {
            this.openPageReadListener = (OpenPageReadListener) context;
        }
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final DatabaseManager databaseManager = new DatabaseManager(context);
        final Story story = stories.get(i);
        viewHolder.tvTitle.setText(story.getmViTitle());
        if (databaseManager.isFavorite(story)) {
            viewHolder.imvAddToFavorite.setImageResource(R.drawable.like);
        } else {
            viewHolder.imvAddToFavorite.setImageResource(R.drawable.dont_like);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPageReadListener.open(stories.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (stories.size() == 0) {
            return 0;
        }
        return stories.size();
    }

    public void callBackOpenPageRead(OpenPageReadListener openPageReadListener) {
        this.openPageReadListener = openPageReadListener;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imvAddToFavorite;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imvAddToFavorite = itemView.findViewById(R.id.imvAddToFavorite);
        }
    }
}
