package com.kynangso.net.mysmile_jokes.interfaces;

import com.kynangso.net.mysmile_jokes.models.StoryV2;

public interface IFavoriteListener {
    void refreshFavorite(boolean isFavorite, StoryV2 story);
}
