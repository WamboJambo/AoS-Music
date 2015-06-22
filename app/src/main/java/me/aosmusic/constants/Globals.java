package me.aosmusic.constants;

import android.app.Application;

/**
 * Created by corsijn on 6/2/2015.
 */
public class Globals extends Application {

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
