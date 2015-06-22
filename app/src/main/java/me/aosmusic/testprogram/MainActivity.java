package me.aosmusic.testprogram;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import me.aosmusic.constants.Globals;
import me.aosmusic.db.HTTPRequest;

/**
 * Created by corsijn on 5/27/2015.
 */
public class MainActivity extends Activity {

    public final String TAG = "MainActivity";
    public ActionBar actionBar;
    private AsyncTask<String, Void, String> asyncTask;
    private static MainActivity mainPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainPage = this;
        setTheme(Globals.getThemeNum());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        asyncTask = new HTTPRequest().execute("SELECT * FROM music");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.confirm)
                .setMessage(R.string.logout)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent homeIntent = new Intent(MainActivity.this, LoginActivity.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(homeIntent);
                    }
                })
                .setNegativeButton(R.string.decline, null)
                .show();
                break;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.action_logout:
                Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static MainActivity getMainActivity() {
        return mainPage;
    }

    public void buildMainPage(String queryReturn) {
        String[][] music = parseQueryReturn(queryReturn);

        for (int i = 0; i < music.length; i++) {
            for (int j = 0; j < music[i].length; j++) {
                Log.d(TAG, music[i][j]);
            }
        }
    }

    private String[][] parseQueryReturn(String toParse) {
        String[] splitString;

        splitString = toParse.split("x|x");

        String[][] music = new String[splitString.length][5];

        for (int i = 0; i < splitString.length / 5; i++) {
            music[i][0] = splitString[(5 * i)];
            music[i][1] = splitString[(5 * i) + 1];
            music[i][2] = splitString[(5 * i) + 2];
            music[i][3] = splitString[(5 * i) + 3];
            music[i][4] = splitString[(5 * i) + 4];
        }

        return music;
    }
}
