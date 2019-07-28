package com.kynangso.net.mysmile_jokes.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kynangso.net.mysmile_jokes.R;
import com.kynangso.net.mysmile_jokes.interfaces.IClickCategoryListener;
import com.kynangso.net.mysmile_jokes.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    List<Category> categories;
    Context context;
    int layout;
    IClickCategoryListener clickCategoryListener;

    public CategoryAdapter(List<Category> categories, Context context, int layout) {
        this.categories = categories;
        this.context = context;
        this.layout = layout;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layout, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.tvTitle.setText(categories.get(i).getmCategoryTitle());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCategoryListener.updateList(categories.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (categories.size() == 0) {
            return 0;
        }
        return categories.size();
    }

    public void callBackClickCategoryListener(IClickCategoryListener clickCategoryListener){
        this.clickCategoryListener = clickCategoryListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
