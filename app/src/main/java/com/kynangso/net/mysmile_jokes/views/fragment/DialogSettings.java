package com.kynangso.net.mysmile_jokes.views.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kynangso.net.mysmile_jokes.MainActivity;
import com.kynangso.net.mysmile_jokes.R;
import com.kynangso.net.mysmile_jokes.interfaces.RestartListener;

public class DialogSettings extends BottomSheetDialogFragment implements View.OnClickListener {
    public static String DARK_MODE = "dark_mode_settings";
    public static String AUTO_READ = "auto_read_settings";
    ImageView imvClose;
    ImageView imvDarkMode;
    ImageView imvAutoRead;
    SharedPreferences sharedPreferences;
    RestartListener restartListener;

    public static DialogSettings getInstance() {
        DialogSettings dialogSettings = new DialogSettings();
        return dialogSettings;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            this.restartListener = (RestartListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_settings, null);
        findView(view);
        checkAndSetViewFromLocalDatabase();
        imvClose.setOnClickListener(this);
        imvDarkMode.setOnClickListener(this);
        imvAutoRead.setOnClickListener(this);
        return view;
    }

    private void checkAndSetViewFromLocalDatabase() {
        if (sharedPreferences.getBoolean(DARK_MODE, false)){
            imvDarkMode.setImageResource(R.drawable.toggle_on);
        }else {
            imvDarkMode.setImageResource(R.drawable.toggle_off);
        }
        if (sharedPreferences.getBoolean(AUTO_READ, false)){
            imvAutoRead.setImageResource(R.drawable.toggle_on);
        }else {
            imvAutoRead.setImageResource(R.drawable.toggle_off);
        }
    }

    private void findView(View view) {
        imvClose = view.findViewById(R.id.imvClose);
        imvDarkMode = view.findViewById(R.id.imvDarkMode);
        imvAutoRead = view.findViewById(R.id.imvAutoRead);
        sharedPreferences = getActivity().getSharedPreferences(MainActivity.SAVE_SETTING_LOCAL_DATABASE, getActivity().MODE_PRIVATE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvClose:
                //do
                dismiss();
                break;
            case R.id.imvDarkMode:
                //do
                if (sharedPreferences.getBoolean(DARK_MODE, false)){
                    imvDarkMode.setImageResource(R.drawable.toggle_off);
                    updateSettings(DARK_MODE, false);
                    if (MainActivity.darkMode){
                        this.restartListener.restart(true);
                    }else {
                        this.restartListener.restart(false);
                    }
                }else {
                    imvDarkMode.setImageResource(R.drawable.toggle_on);
                    updateSettings(DARK_MODE, true);
                    if (!MainActivity.darkMode){
                        this.restartListener.restart(true);
                    }else {
                        this.restartListener.restart(false);
                    }
                }
                break;
            case R.id.imvAutoRead:
                //do
                if (sharedPreferences.getBoolean(AUTO_READ, false)){
                    imvAutoRead.setImageResource(R.drawable.toggle_off);
                    updateSettings(AUTO_READ, false);
                    //TODO: Turn on dark mode all system
                }else {
                    imvAutoRead.setImageResource(R.drawable.toggle_on);
                    updateSettings(AUTO_READ, true);
                    //TODO: Turn on auto read animation all system
                }
                break;
        }
    }
    public void updateSettings(String key, boolean isChecked){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, isChecked);
        editor.apply();
        editor.commit();
    }
}
