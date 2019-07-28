package com.kynangso.net.mysmile_jokes.interfaces;

import com.kynangso.net.mysmile_jokes.model.Story;

public interface IFavoriteListener {
    void refreshFavorite(boolean isFavorite, Story story);
}
