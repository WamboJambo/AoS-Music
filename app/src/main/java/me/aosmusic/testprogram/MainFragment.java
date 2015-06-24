package me.aosmusic.testprogram;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.EnumMap;

import me.aosmusic.constants.Globals;

/**
 * Created by corsijn on 6/22/2015.
 */
public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void buildMenu(String[][] music) {
        for (int i = 0; i < music.length; i++) {
                createRow(music[i]);
            }
    }

    private void createRow(String[] rowInfo) {
        TableRow tr = new TableRow(getActivity());
        tr.setId(Integer.parseInt(rowInfo[Globals.ID]));

        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        TextView tvLeft = new TextView(getActivity());
        tvLeft.setLayoutParams(lp);
        tvLeft.setText("OMG");
        TextView tvCenter = new TextView(getActivity());
        tvCenter.setLayoutParams(lp);
        tvCenter.setText("It");
        TextView tvRight = new TextView(getActivity());
        tvRight.setLayoutParams(lp);
        tvRight.setText("WORKED!!!");

        tr.addView(tvLeft);
        tr.addView(tvCenter);
        tr.addView(tvRight);

        //TextView tv = new TextView(getActivity());
        //tv.setText("Song: " + rowInfo[Globals.TITLE] + "\t Artist: " + rowInfo[Globals.ARTIST] + "\t Album: " + rowInfo[Globals.ALBUM]);
    }
}
