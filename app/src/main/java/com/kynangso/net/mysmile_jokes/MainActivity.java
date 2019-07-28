package com.kynangso.net.mysmile_jokes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.kynangso.net.mysmile_jokes.adapter.SystemPagerAdapter;
import com.kynangso.net.mysmile_jokes.fragment.DialogSettings;
import com.kynangso.net.mysmile_jokes.interfaces.IClickCategoryListener;
import com.kynangso.net.mysmile_jokes.interfaces.IFavoriteListener;
import com.kynangso.net.mysmile_jokes.interfaces.OpenPageReadListener;
import com.kynangso.net.mysmile_jokes.database.DatabaseManager;
import com.kynangso.net.mysmile_jokes.fragment.CategoryMenu;
import com.kynangso.net.mysmile_jokes.fragment.FavoritesFragment;
import com.kynangso.net.mysmile_jokes.fragment.HistoryFragment;
import com.kynangso.net.mysmile_jokes.fragment.HomeFragment;
import com.kynangso.net.mysmile_jokes.fragment.ReadFragment;
import com.kynangso.net.mysmile_jokes.interfaces.RestartListener;
import com.kynangso.net.mysmile_jokes.model.Category;
import com.kynangso.net.mysmile_jokes.model.Story;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements IClickCategoryListener, OpenPageReadListener, IFavoriteListener, RestartListener {
    ArrayList<Category> categories;
    ArrayList<Story> stories;
    ArrayList<Story> historyStory;
    Fragment[] fragments;
    HomeFragment homeFragment;
    FavoritesFragment favoritesFragment;
    HistoryFragment historyFragment;
    int[] icons;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    int frameLayout;
    ActionBarDrawerToggle toggle;
    TabLayout tabLayout;
    ViewPager viewPager;
    SystemPagerAdapter systemPagerAdapter;
    DatabaseManager databaseManager;
    int frameLayoutOverrideReadFragment;
    FragmentManager fragmentManager;
    String presentNameOfActionBarHome = "HÔM NAY BẠN ĐỌC GÌ?";
    public static String SAVE_SETTING_LOCAL_DATABASE = "sharedPreferences";
    public static boolean darkMode;
    ReadFragment readFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(SAVE_SETTING_LOCAL_DATABASE, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(DialogSettings.DARK_MODE, false)){
            setTheme(R.style.AppThemeDark);
            darkMode = true;
        }else {
            setTheme(R.style.AppThemeLight);
            darkMode = false;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        getAllCategoriesFromLocalDatabase();
        getStoriesFromLocalDatabaseByTableName(categories.get(1).getmTableName());
        setupActionBar();
        setupNavMenu();
        setupPager();

    }

    private void getAllCategoriesFromLocalDatabase() {
        databaseManager = new DatabaseManager(this);
        databaseManager.createDatabase();
        categories = databaseManager.readCategories();
    }

    private void getStoriesFromLocalDatabaseByTableName(String tableName) {
        databaseManager = new DatabaseManager(this);
        databaseManager.createDatabase();
        stories = databaseManager.getDataFromTable(tableName);
    }

    private void setupPager() {
        homeFragment = HomeFragment.getInstance(stories);
        favoritesFragment = new FavoritesFragment();
        historyFragment = HistoryFragment.getInstance(historyStory);
        fragments = new Fragment[]{homeFragment, favoritesFragment, historyFragment};
        icons = new int[]{R.drawable.ic_home, R.drawable.ic_star, R.drawable.ic_history};
        systemPagerAdapter = new SystemPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(systemPagerAdapter);
        systemPagerAdapter.notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < icons.length; i++) {
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isOnBackTask();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                isOnBackTask();
                if (tabLayout.getTabAt(0) == tab){
                    getSupportActionBar().setTitle(presentNameOfActionBarHome);
                }else if (tabLayout.getTabAt(1) == tab){
                    getSupportActionBar().setTitle("DANH SÁCH YÊU THÍCH");
                }else if (tabLayout.getTabAt(2) == tab){
                    getSupportActionBar().setTitle("TRUYỆN ĐÃ ĐỌC");
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        //do
                        getSupportActionBar().setTitle(presentNameOfActionBarHome);
                        HomeFragment homeFragment = (HomeFragment) fragments[position];
                        homeFragment.update();
                        break;
                    case 1:
                        //do
                        getSupportActionBar().setTitle("DANH SÁCH YÊU THÍCH");
                        break;
                    case 2:
                        //do
                        getSupportActionBar().setTitle("TRUYỆN ĐÃ ĐỌC");
                        HistoryFragment historyFragment = (HistoryFragment) fragments[position];
                        historyFragment.update();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupNavMenu() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        CategoryMenu categoryMenu = CategoryMenu.getInstance(categories);
        getSupportFragmentManager().beginTransaction().add(frameLayout, categoryMenu)
                .commit();
    }

    @SuppressLint("NewApi")
    private void setupActionBar() {
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(presentNameOfActionBarHome);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccentDark));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setStatusBarColor(getResources().getColor(R.color.colorStatus));
        }
    }

    private void findView() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        frameLayout = R.id.frameLayout;
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        frameLayoutOverrideReadFragment = R.id.frameLayoutOverrideReadFragment;
        historyStory = new ArrayList<>(); }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void updateList(Category category) {
        presentNameOfActionBarHome = category.getmCategoryTitle();
        getStoriesFromLocalDatabaseByTableName(category.getmTableName());
        reSetArguments();
        Log.d("list", "name:" + stories.get(0).getmViTitle());
        drawerLayout.closeDrawer(GravityCompat.START);
        isOnBackTask();
        HomeFragment homeFragment = (HomeFragment) fragments[0];
        viewPager.setCurrentItem(0);
        homeFragment.updateList();
        getSupportActionBar().setTitle(presentNameOfActionBarHome);
    }

    private void reSetArguments() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(HomeFragment.PUT_STORIES_HOME_ACTIVITY, stories);
        homeFragment.setArguments(bundle);
    }

    @Override
    public void open(Story story) {
        readFragment = ReadFragment.newInstance(story);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayoutOverrideReadFragment, readFragment, "ReadFragment");
        fragmentTransaction.addToBackStack("ReadFragment");
        fragmentTransaction.setCustomAnimations(R.anim.scale_adapter_animation, R.anim.scale_adapter_animation);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(story.getmViTitle());

        for (Story i : historyStory) {
            if (i.getmId() == story.getmId()) {
                return;
            }
        }
        historyStory.add(story);
    }
    private void isOnBackTask(){
        try {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
        }catch (NullPointerException e){
            Log.d("error: ", "error: " + e.toString());
        }
    }

    @Override
    public void refreshFavorite(boolean isFavorite, Story story) {
        Log.d("TAGGG", isFavorite + " \n from main activity");
        FavoritesFragment favoritesFragment = (FavoritesFragment) fragments[1];
        favoritesFragment.update(isFavorite, story);
    }

    @Override
    protected void onDestroy() {
        if (readFragment != null){
            if (readFragment.isInLayout()){
                readFragment.onDetach();
            }
        }
        super.onDestroy();
    }

    @Override
    public void restart(boolean isRestart) {
        if (isRestart){
            recreate();
        }
    }
    public  static void setNotifyDataSetChanged(RecyclerView.Adapter adapter){
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }
}
