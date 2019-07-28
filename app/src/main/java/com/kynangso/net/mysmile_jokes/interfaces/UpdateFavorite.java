package com.kynangso.net.mysmile_jokes.interfaces;

import com.kynangso.net.mysmile_jokes.model.Story;

public interface UpdateFavorite {
    void refresh();
    void refreshFavorite(boolean isFavorite, Story story);
}
