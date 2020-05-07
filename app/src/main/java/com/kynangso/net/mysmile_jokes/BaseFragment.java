package com.kynangso.net.mysmile_jokes;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
    public abstract int getLayoutResource();
    public abstract void initViews(View rootView);
    public abstract void listener();
    private static ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        initViews(view);
        listener();

        return view;
    }

    protected void showLoadingProgress() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait...");
        dialog.show();
    }

    protected void hideLoadingProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
