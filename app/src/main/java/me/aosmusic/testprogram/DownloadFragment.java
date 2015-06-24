package me.aosmusic.testprogram;

import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import me.aosmusic.constants.Globals;

/**
 * Created by James on 24-06-2015.
 */
public class DownloadFragment extends Fragment {
    public static MediaPlayer mPlayer;
    private File files[];
    private int currentIndex;
    private ImageButton playPause;
    private ImageButton prevButton;
    private ImageButton nextButton;
    private Handler handler;
    private SeekBar seekBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (StreamFragment.getMediaPlayer() != null)
            StreamFragment.getMediaPlayer().stop();
        StreamFragment.destroyMP();
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        currentIndex = 0;

        File folder = new File(Environment.getExternalStorageDirectory() + "/aosmusic/");
        files = folder.listFiles();

        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }

        try {
            mPlayer.setDataSource(files[currentIndex].getAbsolutePath());
            mPlayer.prepare();
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        mPlayer.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_download, container, false);

        seekBar = (SeekBar)v.findViewById(R.id.seekBar);
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
        });

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

        prevButton = (ImageButton)v.findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (currentIndex == 0) {
                        mPlayer.stop();
                        mPlayer.reset();
                        mPlayer.setDataSource(files[files.length - 1].getAbsolutePath());
                        mPlayer.prepare();
                        mPlayer.start();
                        songChanged(currentIndex);
                    } else {
                        mPlayer.stop();
                        mPlayer.reset();
                        mPlayer.setDataSource(files[--currentIndex].getAbsolutePath());
                        mPlayer.prepare();
                        mPlayer.start();
                        songChanged(currentIndex);
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextButton = (ImageButton)v.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    currentIndex++;
                    mPlayer.stop();
                    if (currentIndex >= files.length)
                        currentIndex = 0;

                    mPlayer.stop();
                    mPlayer.reset();
                    mPlayer.setDataSource(files[currentIndex].getAbsolutePath());
                    mPlayer.prepare();
                    mPlayer.start();
                    songChanged(currentIndex);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        TextView songInfo = (TextView)v.findViewById(R.id.songInfo);
        TextView moreInfo = (TextView)v.findViewById(R.id.moreInfo);
        songInfo.setText(getActivity().getIntent().getStringExtra("Title"));
        moreInfo.setText(getActivity().getIntent().getStringExtra("Artist") + " - "
                + getActivity().getIntent().getStringExtra("Album"));

        String songUrl = getActivity().getIntent().getStringExtra("URL");

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

    public void songChanged(int track) {
        String parse = files[track].getAbsolutePath();
        parse = parse.replaceAll("\\D+","");
        parse = parse.substring(0,parse.length() - 1);
        int id = Integer.parseInt(parse);
        id--;

        TextView songTitle = (TextView)getActivity().findViewById(R.id.songInfo);
        songTitle.setText(MainActivity.getMusic()[id][Globals.TITLE]);
        TextView songInfo = (TextView)getActivity().findViewById(R.id.moreInfo);
        songInfo.setText(MainActivity.getMusic()[id][Globals.ARTIST] + " - "
                + MainActivity.getMusic()[id][Globals.ALBUM]);
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

    public static MediaPlayer getMediaPlayer() {
        return mPlayer;
    }
}
