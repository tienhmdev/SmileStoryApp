package com.kynangso.net.mysmile_jokes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kynangso.net.mysmile_jokes.MainActivity;
import com.kynangso.net.mysmile_jokes.R;
import com.kynangso.net.mysmile_jokes.adapter.HistoryRecyclerAdapter;
import com.kynangso.net.mysmile_jokes.adapter.HomeRecyclerAdapter;
import com.kynangso.net.mysmile_jokes.model.Story;

import java.util.ArrayList;

interface HistoryUpdatable {
    void update();
}

public class HistoryFragment extends Fragment implements HistoryUpdatable{
    public static String PUT_HISTORY_STORY_KEY = "historyStory";
    ArrayList<Story> stories;
    RecyclerView recyclerView;
    HistoryRecyclerAdapter adapter;
    TextView tvDoNotHistoryStory;

    public static HistoryFragment getInstance(ArrayList<Story> stories){
        HistoryFragment historyFragment = new HistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PUT_HISTORY_STORY_KEY, stories);
        historyFragment.setArguments(bundle);
        return historyFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            this.stories = getArguments().getParcelableArrayList(PUT_HISTORY_STORY_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_activity, container, false);
        findView(view);
        setupRecyclerView();
        setupDefaultView();
        return view;
    }

    private void setupDefaultView() {
        if (!stories.isEmpty()){
            tvDoNotHistoryStory.setVisibility(View.GONE);
        }else {
            tvDoNotHistoryStory.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerView() {
        adapter = new HistoryRecyclerAdapter(stories, getContext(), R.layout.item_story_home);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        MainActivity.setNotifyDataSetChanged(adapter);
    }

    private void findView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        tvDoNotHistoryStory = view.findViewById(R.id.tvDoNotHistoryStory);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void update() {
        setupDefaultView();
    }
}
