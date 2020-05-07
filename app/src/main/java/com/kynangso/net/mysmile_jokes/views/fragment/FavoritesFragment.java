package com.kynangso.net.mysmile_jokes.views.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kynangso.net.mysmile_jokes.models.StoryV2;
import com.kynangso.net.mysmile_jokes.MainActivity;
import com.kynangso.net.mysmile_jokes.R;
import com.kynangso.net.mysmile_jokes.adapter.FavoriteRecyclerAdapter;
import com.kynangso.net.mysmile_jokes.interfaces.UpdateFavorite;
import com.kynangso.net.mysmile_jokes.models.Story;

import java.util.ArrayList;

interface FavoriteUpdatable {
    void update(boolean isFavorite, StoryV2 story);

    void reloadData();
}

public class FavoritesFragment extends Fragment implements FavoriteUpdatable, UpdateFavorite {
    ArrayList<StoryV2> stories;
    RecyclerView recyclerView;
    Handler handler;
    boolean isLoading = false;
    ProgressBar loadMoreProgress;
    SwipeRefreshLayout srLayout;
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
        setupRecyclerView();
        getDataFromLocalDatabase();
        setViewForNull();
        setupSwipeRefreshLayout();
        loadMore();
        return view;
    }

    @SuppressLint("HandlerLeak")
    private void loadMore() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        isLoading = true;
                        loadMoreProgress.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        isLoading = false;
                        loadMoreProgress.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "finish", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
                int lastItem = linearLayoutManager.findLastVisibleItemPosition();
                int lastCompleteItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (!isLoading && lastCompleteItem == stories.size() - 1) {
                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                handler.sendEmptyMessage(0);
                                Thread.sleep(3000);
                                handler.sendEmptyMessage(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }
            }
        });

    }

    private void getDataFromLocalDatabase() {
//        DatabaseManager databaseManager = new DatabaseManager(getContext());
//        Log.d("getdata", "truoc khi = list: " + stories.size());
//        stories = databaseManager.getDataFromTable("tblFavorites");
//        Log.d("getdata", "sau khi = list: " + stories.size());
    }

    private void findView(View view) {
        loadMoreProgress = view.findViewById(R.id.loadMoreProgress);
        srLayout = view.findViewById(R.id.srLayout);
        stories = new ArrayList<>();
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
    public void update(boolean isFavorite, StoryV2 story) {
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

    @Override
    public void reloadData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("tblFavorites").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    StoryV2 story = ds.getValue(StoryV2.class);
                    stories.add(story);
                    adapter.notifyDataSetChanged();
                    setViewForNull();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupSwipeRefreshLayout() {
        srLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srLayout.setRefreshing(false);
                        reloadData();
                    }
                }, 1000);
            }
        });
    }
}
