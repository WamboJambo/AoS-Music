package me.aosmusic.testprogram;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import me.aosmusic.constants.Globals;

/**
 * Created by James on 23-06-2015.
 */
public class StreamFragment extends Fragment {
    public static MediaPlayer mPlayer;
    public static StreamFragment frag;
    private ImageButton playPause;
    //private ImageButton prevButton;
    //private ImageButton nextButton;
    //private SeekBar seekBar;
    private int currentItem;
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DownloadFragment.getMediaPlayer() != null)
            DownloadFragment.getMediaPlayer().stop();
        DownloadFragment.destroyMP();
        frag = this;
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        currentItem = getActivity().getIntent().getIntExtra("ID", 0);

        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stream, container, false);

        /**seekBar = (SeekBar)v.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mPlayer != null && fromUser) {
                    mPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/

        playPause = (ImageButton)v.findViewById(R.id.playButton);
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null) {
                    if (mPlayer.isPlaying()) {
                        mPlayer.pause();
                        playPause.setBackground(getResources().getDrawable(android.R.drawable.ic_media_play));
                    } else {
                        mPlayer.start();
                        playPause.setBackground(getResources().getDrawable(android.R.drawable.ic_media_pause));
                    }
                }
            }
        });

        /**prevButton = (ImageButton)v.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentItem == 0) {
                        currentItem = MainActivity.getMusic().length - 1;
                } else {
                    currentItem--;
                }
                mPlayer.stop();
                stream(MainActivity.getMusic()[currentItem][4]);
            }
        });

        nextButton = (ImageButton)v.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem = (++currentItem) % MainActivity.getMusic().length;
                mPlayer.stop();

                try {
                    StreamFragment.getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
                    StreamFragment.getMediaPlayer().setDataSource(new MainActivity(), Uri.parse(MainActivity.getMusic()[currentItem][Globals.URL]));
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.getMainActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });*/


        TextView songInfo = (TextView)v.findViewById(R.id.songInfo);
        TextView moreInfo = (TextView)v.findViewById(R.id.moreInfo);
        songInfo.setText(getActivity().getIntent().getStringExtra("Title"));
        moreInfo.setText(getActivity().getIntent().getStringExtra("Artist") + " - "
                + getActivity().getIntent().getStringExtra("Album"));

        String songUrl = getActivity().getIntent().getStringExtra("URL");
        stream(songUrl);

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mPlayer != null) {
                    int currentPosition = mPlayer.getCurrentPosition() / 1000;
                    //seekBar.setProgress(currentPosition);
                }
                handler.postDelayed(this, 1000);
            }

        });

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
                    //currentItem = (++currentItem) % MainActivity.getMusic().length;
                    //play(getActivity(), ++index);
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

    public static void destroyMP() {
        mPlayer = null;
    }

    public void stream(String url) {
        if (mPlayer != null) {
            try {
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.setDataSource(getActivity().getApplicationContext(), Uri.parse(url));
                mPlayer.prepare();
                mPlayer.start();
                //seekBar.setMax(mPlayer.getDuration());
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            mPlayer = new MediaPlayer();

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    currentItem = (++currentItem) % MainActivity.getMusic().length;
                    stream(MainActivity.getMusic()[currentItem][4]);
                }
            });
            stream(url);
        }

    }

    public static MediaPlayer getMediaPlayer() {
        return mPlayer;
    }

}
