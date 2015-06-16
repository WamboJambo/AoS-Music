package me.aosmusic.testprogram;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import me.aosmusic.constants.Globals;

/**
 * Created by corsijn on 5/28/2015.
 */
public class SettingsActivity extends Activity {

    Bundle savedInstanceState;
    Switch darkTheme;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setTheme(Globals.getThemeNum());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.savedInstanceState = savedInstanceState;

        darkTheme = (Switch)findViewById(R.id.darkTheme);
        darkTheme.setChecked(Globals.isDark());

    }

    protected void recreate(Bundle savedInstanceState) {
        onCreate(savedInstanceState);
    }

    public void onToggleClicked(View view) {
        boolean on = ((Switch) view).isChecked();

        if (on) {
            Globals.setThemeNum(Globals.THEME_DARK);
        } else {
            Globals.setThemeNum(Globals.THEME_LIGHT);
        }

        setContentView(R.layout.activity_settings);
        recreate(savedInstanceState);
    }
}
