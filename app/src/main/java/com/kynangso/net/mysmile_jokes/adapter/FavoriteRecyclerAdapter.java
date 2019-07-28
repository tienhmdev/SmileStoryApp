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
import com.kynangso.net.mysmile_jokes.interfaces.OpenPageReadListener;
import com.kynangso.net.mysmile_jokes.interfaces.UpdateFavorite;
import com.kynangso.net.mysmile_jokes.database.DatabaseManager;
import com.kynangso.net.mysmile_jokes.model.Story;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder> {
    List<Story> stories;
    Context context;
    int layout;
    private UpdateFavorite listenerAddFavorite;
    private OpenPageReadListener openPageReadListener;
    boolean isFavorite = false;

    public FavoriteRecyclerAdapter(List<Story> stories, Context context, int layout) {
        this.stories = stories;
        this.context = context;
        this.layout = layout;
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

        viewHolder.imvAddToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Favorite", "adapter return: " + databaseManager.isFavorite(stories.get(i)));
                if (!databaseManager.isFavorite(story)) {
                    databaseManager.addToFavorite(story);
                    viewHolder.imvAddToFavorite.setImageResource(R.drawable.like);
                    isFavorite = true;
                    Log.d("Favorite", "hành động thêm");
                    Log.d("Favorite", "them: " + stories.get(i).getmViTitle());
                } else {
                    databaseManager.deleteToFavorite(story);
                    viewHolder.imvAddToFavorite.setImageResource(R.drawable.dont_like);
                    Log.d("Favorite", "hành động xóa");
                    stories.remove(i);
                    notifyDataSetChanged();
                    isFavorite = false;
                }
                listenerAddFavorite.refresh();
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPageReadListener.open(stories.get(i));
            }
        });
//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_adapter_animation);
//        viewHolder.itemView.setAnimation(animation);
    }

    @Override
    public int getItemCount() {
        if (stories.size() == 0) {
            return 0;
        }
        return stories.size();
    }

    public void callBackAddFavorite(UpdateFavorite updateFavorite) {
        this.listenerAddFavorite = updateFavorite;
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
