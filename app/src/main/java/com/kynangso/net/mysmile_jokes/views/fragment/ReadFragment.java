package com.kynangso.net.mysmile_jokes.views.fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kynangso.net.mysmile_jokes.models.StoryV2;
import com.kynangso.net.mysmile_jokes.MainActivity;
import com.kynangso.net.mysmile_jokes.R;

import java.util.Locale;

public class ReadFragment extends Fragment {

    TextToSpeech textToSpeech;
    TextView tvContent;
    SeekBar seekBar;
    boolean isFavorite = true;
    public static final String PUT_STORY_KEY = "story1102";
    public static final String TEXT_SIZE = "textSize";
    StoryV2 story;
    SharedPreferences sharedPreferences;
    ImageView imvSpeaker;
    ImageView imvFavorite;

    public static ReadFragment newInstance(StoryV2 story) {
        ReadFragment fragment = new ReadFragment();
        Bundle args = new Bundle();
        args.putParcelable(PUT_STORY_KEY, story);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            story = getArguments().getParcelable(PUT_STORY_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read, container, false);
        findView(view);
        setView();
        setupSeekBar();
        setupTextToSpeech();
        addFavorite();
        return view;
    }

    private void addFavorite() {
        imvFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavorite) {
                    imvFavorite.setImageResource(R.drawable.like);
                    isFavorite = true;
                } else {
                    imvFavorite.setImageResource(R.drawable.dont_like);
                    isFavorite = false;
                }
            }
        });
    }

    private void setupTextToSpeech() {
        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int result = textToSpeech.setLanguage(Locale.ROOT);
                    if (result == TextToSpeech.LANG_NOT_SUPPORTED ||result == TextToSpeech.LANG_MISSING_DATA){

                    }else {

                    }
                }
            }
        });
        imvSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Reading sound of " + story.getTitle() + "!..", Toast.LENGTH_LONG).show();
                textToSpeech.speak(story.getContent(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    private void setupSeekBar() {
        int textSizeSaved = sharedPreferences.getInt(TEXT_SIZE, 20);
        tvContent.setTextSize(textSizeSaved);
        seekBar.setProgress(textSizeSaved);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvContent.setTextSize(progress);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(TEXT_SIZE, progress);
                editor.apply();
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setView() {
        tvContent.setText(story.getContent());
        getActivity().setTitle(story.getTitle());
    }

    private void findView(View view) {
        imvSpeaker = view.findViewById(R.id.imvSpeaker);
        imvFavorite = view.findViewById(R.id.imvFavorite);
        tvContent = view.findViewById(R.id.tvContent);
        seekBar = view.findViewById(R.id.seekBar);
        sharedPreferences = getActivity().getSharedPreferences(MainActivity.SAVE_SETTING_LOCAL_DATABASE, getActivity().MODE_PRIVATE);

    }

    @Override
    public void onDetach() {
        if (textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDetach();
    }
}
