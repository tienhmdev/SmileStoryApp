package com.kynangso.net.mysmile_jokes.views.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kynangso.net.mysmile_jokes.BaseFragment;
import com.kynangso.net.mysmile_jokes.models.Category;
import com.kynangso.net.mysmile_jokes.models.StoryV2;
import com.kynangso.net.mysmile_jokes.MainActivity;
import com.kynangso.net.mysmile_jokes.R;
import com.kynangso.net.mysmile_jokes.adapter.HomeRecyclerAdapter;
import com.kynangso.net.mysmile_jokes.interfaces.IFavoriteListener;

import java.util.ArrayList;
import java.util.List;

interface HomeUpdatable {
    void update();

    void updateList(Category category);
}

public class HomeFragment extends BaseFragment implements HomeUpdatable, IFavoriteListener {
    ArrayList<StoryV2> stories;
    ArrayList<StoryV2> storiesFavorites;
    RecyclerView recyclerView;
    ProgressBar loadMoreProgress;
    Category category;
    Handler handler;
    boolean isLoading = false;
    HomeRecyclerAdapter adapter;
    IFavoriteListener iFavorite;
    SwipeRefreshLayout srLayout;
    public static String PUT_STORIES_HOME_ACTIVITY = "put_home_activity";
    StoryV2 storyFavorite;
    DatabaseReference databaseReference;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            this.iFavorite = (IFavoriteListener) context;
        }
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

                        private List<StoryV2> loadMoreData() {
                            //TODO: LoadMore
                            //do something!
                            return new ArrayList<>();
                        }
                    });
                    thread.start();
                }
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
                        updateList(category);
                    }
                }, 1000);
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new HomeRecyclerAdapter(stories, getContext(), R.layout.item_story_home);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        MainActivity.setNotifyDataSetChanged(adapter);
    }

    private void findView(View view) {

    }

    @Override
    public void update() {
        MainActivity.setNotifyDataSetChanged(adapter);
    }

    @Override
    public void updateList(Category category) {
        showLoadingProgress();
        this.category = category;
        stories.clear();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(category.getmTableName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get data list stories
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    StoryV2 story = ds.getValue(StoryV2.class);
                    stories.add(story);
                }

                // get data from favorites
                databaseReference.child("tblFavorites").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        storiesFavorites.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            storyFavorite = ds.getValue(StoryV2.class);
                            storiesFavorites.add(storyFavorite);
                        }
                        adapter.setStoriesFavorites(storiesFavorites);
                        hideLoadingProgress();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        hideLoadingProgress();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideLoadingProgress();
            }
        });
    }

    @Override
    public void refreshFavorite(boolean isFavorite, StoryV2 story) {
        if (isFavorite) {
            addToFavorite(story);
        } else {
            removeToFavorite(story);
        }

    }

    private void addToFavorite(final StoryV2 storyV2) {
        showLoadingProgress();
        databaseReference = FirebaseDatabase.getInstance().getReference("tblFavorites");
        databaseReference.child(storyV2.getTitle()).setValue(storyV2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideLoadingProgress();
            }
        });
    }

    private void removeToFavorite(final StoryV2 storyV2) {
        showLoadingProgress();
        databaseReference.child(storyV2.getTitle()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideLoadingProgress();
            }
        });
    }

    public void currentList() {
        showLoadingProgress();
        this.category = new Category(1, "tblTrangQuynh", "tblTrangQuynh");
        stories.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(category.getmTableName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    StoryV2 story = ds.getValue(StoryV2.class);
                    stories.add(story);
                }
                adapter.notifyDataSetChanged();
                hideLoadingProgress();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideLoadingProgress();
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.home_activity;
    }

    @Override
    public void initViews(View rootView) {
        databaseReference = FirebaseDatabase.getInstance().getReference();

        stories = new ArrayList<>();
        storiesFavorites = new ArrayList<>();
        srLayout = rootView.findViewById(R.id.srLayout);
        loadMoreProgress = rootView.findViewById(R.id.loadMoreProgress);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        setupRecyclerView();
        currentList();
    }

    @Override
    public void listener() {
        loadMore();
        setupSwipeRefreshLayout();

    }
}
