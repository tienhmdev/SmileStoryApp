package com.kynangso.net.mysmile_jokes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kynangso.net.mysmile_jokes.MainActivity;
import com.kynangso.net.mysmile_jokes.R;
import com.kynangso.net.mysmile_jokes.adapter.FavoriteRecyclerAdapter;
import com.kynangso.net.mysmile_jokes.interfaces.UpdateFavorite;
import com.kynangso.net.mysmile_jokes.database.DatabaseManager;
import com.kynangso.net.mysmile_jokes.model.Story;

import java.util.ArrayList;

interface FavoriteUpdatable {
    void update(boolean isFavorite, Story story);
}

public class FavoritesFragment extends Fragment implements FavoriteUpdatable, UpdateFavorite {
    ArrayList<Story> stories;
    RecyclerView recyclerView;
    TextView tvDoNotFavoriteStory;
    FavoriteRecyclerAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorites_activity, container, false);
        findView(view);
        getDataFromLocalDatabase();
        setupRecyclerView();
        setViewForNull();
        return view;
    }

    private void getDataFromLocalDatabase() {
        stories = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager(getContext());
        Log.d("getdata", "truoc khi = list: " + stories.size());
        stories = databaseManager.getDataFromTable("tblFavorites");
        Log.d("getdata", "sau khi = list: " + stories.size());
    }

    private void findView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        tvDoNotFavoriteStory = view.findViewById(R.id.tvDoNotFavoriteStory);
    }

    private void setupRecyclerView() {
        adapter = new FavoriteRecyclerAdapter(stories, getContext(), R.layout.item_story_home);
        adapter.callBackAddFavorite(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        MainActivity.setNotifyDataSetChanged(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void refreshView() {
        getDataFromLocalDatabase();
        setupRecyclerView();
        setViewForNull();
    }

    @Override
    public void refresh() {
        refreshView();
        Log.d("Interface", "refresh");
    }

    @Override
    public void refreshFavorite(boolean isFavorite, Story story) {
        Toast.makeText(getActivity(), isFavorite + "-", Toast.LENGTH_SHORT).show();
    }

    private void setViewForNull() {
        if (stories.isEmpty()) {
            tvDoNotFavoriteStory.setVisibility(View.VISIBLE);
        } else {
            tvDoNotFavoriteStory.setVisibility(View.GONE);
        }
    }

    @Override
    public void update(boolean isFavorite, Story story) {
        if (isFavorite) {
            stories.add(story);
        } else {
            stories.remove(story);
        }
        MainActivity.setNotifyDataSetChanged(adapter);
        setViewForNull();
        Log.d("Interface", "update");
        Log.d("TAGGG", isFavorite + " FavoriteFragment");
    }
}
