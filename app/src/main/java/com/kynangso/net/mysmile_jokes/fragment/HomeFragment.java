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

import com.kynangso.net.mysmile_jokes.MainActivity;
import com.kynangso.net.mysmile_jokes.R;
import com.kynangso.net.mysmile_jokes.adapter.HomeRecyclerAdapter;
import com.kynangso.net.mysmile_jokes.interfaces.IFavoriteListener;
import com.kynangso.net.mysmile_jokes.model.Story;

import java.util.ArrayList;

interface HomeUpdatable {
    void update();
    void updateList();
}

public class HomeFragment extends Fragment implements HomeUpdatable, IFavoriteListener {
    ArrayList<Story> stories;
    RecyclerView recyclerView;
    HomeRecyclerAdapter adapter;
    IFavoriteListener iFavorite;
    public static String PUT_STORIES_HOME_ACTIVITY = "put_home_activity";


    public static HomeFragment getInstance(ArrayList<Story> stories) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PUT_STORIES_HOME_ACTIVITY, stories);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgumentsFromActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            this.iFavorite = (IFavoriteListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_activity, container, false);
        findView(view);
        setupRecyclerView();
        return view;
    }

    private void setupRecyclerView() {
        adapter = new HomeRecyclerAdapter(stories, getContext(), R.layout.item_story_home);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        MainActivity.setNotifyDataSetChanged(adapter);
    }

    private void findView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    public void update() {
        MainActivity.setNotifyDataSetChanged(adapter);
    }

    @Override
    public void updateList() {
        refreshView();
    }

    public void refreshView(){
        getArgumentsFromActivity();
        setupRecyclerView();
        MainActivity.setNotifyDataSetChanged(adapter);
        Log.d("list", stories.get(0).getmViTitle());
    }
    private void getArgumentsFromActivity(){
        if (getArguments() != null) {
            this.stories = getArguments().getParcelableArrayList(PUT_STORIES_HOME_ACTIVITY);
        }
    }

    @Override
    public void refreshFavorite(boolean isFavorite, Story story) {
        Log.d("TAGGG", isFavorite + "");
        iFavorite.refreshFavorite(isFavorite, story);

    }

}
