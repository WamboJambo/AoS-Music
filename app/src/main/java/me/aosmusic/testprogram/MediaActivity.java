package me.aosmusic.testprogram;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by corsijn on 6/17/2015.
 */
public class MediaActivity  {
    private MediaPlayer mPlayer;

    public void pause() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying())
                mPlayer.pause();
            else
                mPlayer.start();
        }
    }

    public void play(Context c, int index) {
        if (mPlayer != null) {
            mPlayer.start();
        }
        else {
            mPlayer.selectTrack(index);
            mPlayer.start();

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop();
                }
            });
        }
    }

    public void stop() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void stream(Context c, int index) {


    }
}
