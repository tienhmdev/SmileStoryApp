package com.kynangso.net.mysmile_jokes.views.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
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
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kynangso.net.mysmile_jokes.MainActivity;
import com.kynangso.net.mysmile_jokes.R;
import com.kynangso.net.mysmile_jokes.adapter.CategoryAdapter;
import com.kynangso.net.mysmile_jokes.interfaces.IClickCategoryListener;
import com.kynangso.net.mysmile_jokes.models.Category;

import java.util.ArrayList;

public class CategoryMenu extends Fragment implements View.OnClickListener, IClickCategoryListener {
    ArrayList<Category> categories;
    RecyclerView recyclerView;
    CategoryAdapter adapter;
    ImageView imvSettings;
    ImageView imvAvatar;
    IClickCategoryListener clickCategoryListener;

    public static String PUT_CATEGORY_MENU_KEY = "category_list";

    public static CategoryMenu getInstance() {
        return new CategoryMenu();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            this.clickCategoryListener = (IClickCategoryListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.categories = getArguments().getParcelableArrayList(PUT_CATEGORY_MENU_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_menu_fragment, container, false);
        findView(view);
        setupMenu();
        imvSettings.setOnClickListener(this);
        setupHeader();
        getAllCategoryFromDatabase();
        return view;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupHeader() {
        imvAvatar.setClipToOutline(true);
    }

    private void setupMenu() {
        adapter = new CategoryAdapter(categories, getContext(), R.layout.item_catogory);
        adapter.callBackClickCategoryListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter.notifyDataSetChanged();
    }

    private void findView(View view) {
        categories = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        imvSettings = view.findViewById(R.id.imvSettings);
        imvAvatar = view.findViewById(R.id.imvAvatar);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imvSettings:
                DialogSettings dialogSettings = DialogSettings.getInstance();
                dialogSettings.show(getChildFragmentManager(), "dialogSettings");
                break;
        }
    }

    @Override
    public void updateList(Category category) {
        this.clickCategoryListener.updateList(category);
    }


    private void getAllCategoryFromDatabase(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("tblCategory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Category category = ds.getValue(Category.class);
                    Log.d("Taggg", "value: " + category.getmTableName());
                    categories.add(category);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
