package com.kynangso.net.mysmile_jokes.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.kynangso.net.mysmile_jokes.models.StoryV2;
import com.kynangso.net.mysmile_jokes.MainActivity;
import com.kynangso.net.mysmile_jokes.R;
import com.kynangso.net.mysmile_jokes.interfaces.IFavoriteListener;
import com.kynangso.net.mysmile_jokes.interfaces.OpenPageReadListener;
import java.util.ArrayList;
import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {
    List<StoryV2> stories;
    List<StoryV2> storiesFavorites;
    Context context;
    int layout;
    private IFavoriteListener favorite;
    private OpenPageReadListener openPageReadListener;
    boolean isFavorite = false;

    public HomeRecyclerAdapter(ArrayList<StoryV2> stories, Context context, int layout) {
        this.stories = stories;
        this.context = context;
        this.layout = layout;
    }

    public void setStoriesFavorites(List<StoryV2> storiesFavorites) {
        this.storiesFavorites = storiesFavorites;
        notifyDataSetChanged();
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        //final DatabaseManager databaseManager = new DatabaseManager(context);
        final StoryV2 story = stories.get(position);
        viewHolder.tvTitle.setText(story.getTitle());
        if (isFavorite(story)) {
            isFavorite = true;
            viewHolder.imvAddToFavorite.setImageResource(R.drawable.like);
        } else {
            isFavorite = false;
            viewHolder.imvAddToFavorite.setImageResource(R.drawable.dont_like);
        }
        viewHolder.imvAddToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorite = !isFavorite;
                if (isFavorite) {
                    viewHolder.imvAddToFavorite.setImageResource(R.drawable.like);
                } else {
                    viewHolder.imvAddToFavorite.setImageResource(R.drawable.dont_like);
                }
                favorite.refreshFavorite(isFavorite, story);
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPageReadListener.open(story);
            }
        });
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_adapter_animation);
        viewHolder.itemView.setAnimation(animation);
    }

    private boolean isFavorite(StoryV2 story) {
        if (storiesFavorites == null) {
            return false;
        }
        for (StoryV2 storyV2 : storiesFavorites) {
            if (TextUtils.equals(storyV2.getTitle(), story.getTitle())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (stories == null) {
            return 0;
        }
        return stories.size();
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
