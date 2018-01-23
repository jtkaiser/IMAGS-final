package jtkaiser.imags;

/**
 * Created by jtkai on 1/23/2018.
 */

import android.support.annotation.Nullable;

public interface Player {

    void play(String url);

    void pause();

    void resume();

    boolean isPlaying();

    @Nullable
    String getCurrentTrack();

    void release();
}
