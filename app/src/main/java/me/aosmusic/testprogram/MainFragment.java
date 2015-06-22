package me.aosmusic.testprogram;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by corsijn on 6/22/2015.
 */
public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //TODO: Build main page and remove temporary static MainActivity view

        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
