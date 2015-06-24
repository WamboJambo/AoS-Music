package me.aosmusic.constants;

import android.app.Application;

/**
 * Created by corsijn on 6/2/2015.
 */
public class Globals extends Application {

    // It pains me to do this, but I tried an enumerated map and evidently they don't allow for multi-dimensional
    // storage (I haven't taken Data Structures, I didn't know about that)
    public static final int ID = 0;
    public static final int TITLE = 1;
    public static final int ARTIST = 2;
    public static final int ALBUM = 3;
    public static final int URL = 4;

    public static final int THEME_LIGHT = android.R.style.Theme_Holo_Light_DarkActionBar;
    public static final int THEME_DARK = android.R.style.Theme_Holo;

    public static int currentTheme = THEME_LIGHT;

    public static void setThemeNum(int theme) {
        currentTheme = theme;
    }

    public static int getThemeNum() {
        return currentTheme;
    }

    public static boolean isDark() {
        return (currentTheme == THEME_DARK) ? true : false;
    }
}
