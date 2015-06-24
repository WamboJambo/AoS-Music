package me.aosmusic.testprogram;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.net.URL;

/**
 * Created by corsijn on 6/17/2015.
 */
public class MediaActivity extends Activity {
    private MediaPlayer mPlayer;
    public ImageButton playPause;
    public ImageButton prevButton;
    public ImageButton nextButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        playPause = (ImageButton)findViewById(R.id.playButton);
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null) {
                    if (mPlayer.isPlaying()) {
                        mPlayer.pause();
                        playPause.setBackground(getDrawable(android.R.drawable.ic_media_play));
                    } else {
                        mPlayer.start();
                        playPause.setBackground(getDrawable(android.R.drawable.ic_media_pause));
                    }
                }
            }
        });

        prevButton = (ImageButton)findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        nextButton = (ImageButton)findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        String songUrl = getIntent().getStringExtra("URL");
        stream(songUrl);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }

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

    public void stream(String url) {
        if (mPlayer != null) {
            try {
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.setDataSource(this, Uri.parse(url));
                mPlayer.prepare();
                mPlayer.start();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            mPlayer = new MediaPlayer();

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop();
                }
            });
            stream(url);
        }

    }
}
