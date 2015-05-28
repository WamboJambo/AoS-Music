package me.aosmusic.testprogram;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by corsijn on 5/28/2015.
 */
public class SettingsActivity extends Activity {

    Switch darkTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        darkTheme = (Switch)findViewById(R.id.darkTheme);

        darkTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setTheme(android.R.style.Theme_Holo);
                }
                else {
                    setTheme(android.R.style.Theme_Holo_Light);
                }
            }
        });
    }
}
