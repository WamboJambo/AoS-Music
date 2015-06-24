package me.aosmusic.testprogram;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.EnumMap;

import me.aosmusic.constants.Globals;
import me.aosmusic.db.HTTPRequest;

/**
 * Created by corsijn on 5/27/2015.
 */
public class MainActivity extends Activity {

    public static enum Data {
      id, title, artist, album, url
    };

    public final String TAG = "MainActivity";
    private AsyncTask<String, Void, String> asyncTask;
    private static MainActivity mainPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainPage = this;
        setTheme(Globals.getThemeNum());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        asyncTask = new HTTPRequest().execute("SELECT * FROM music");

        /**FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new MainFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }*/
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
            createRow(music[i]);
        }

        /**FragmentManager fm = getFragmentManager();
        MainFragment fragment = (MainFragment) fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new MainFragment();
        fm.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit();
        }

        fragment.buildMenu(music);*/

    }

    private void createRow(final String[] rowInfo) {
        TableRow tr = new TableRow(this);
        tr.setPadding(10,20,10,20);

        //Layout params for each row
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        //Layout params to even out artist/album titles
        //LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, )

        tr.setLayoutParams(lp);

        TextView tv = new TextView(this);
        tv.setLayoutParams(lp);
        tv.setText(rowInfo[Globals.TITLE] + "\n" + rowInfo[Globals.ARTIST] + " - " + rowInfo[Globals.ALBUM]);

        tr.setBackgroundResource(R.drawable.row_border);

        tr.addView(tv);

        TableLayout tl = (TableLayout)this.findViewById(R.id.fragmentContainer);

        tr.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      Intent i = new Intent(MainActivity.this, MediaActivity.class);
                                      i.putExtra("URL", rowInfo[Globals.URL]);
                                      startActivity(i);
                                  }
                              });

        tl.addView(tr);
    }

    private String[][] parseQueryReturn(String toParse) {
        String[] splitString;

        splitString = toParse.split("x\\|x");

        String[][] music = new String[(splitString.length / 5)][5];

        for (int i = 0; i < splitString.length / 5; i++) {
            music[i][Globals.ID] = splitString[(5 * i)];
            music[i][Globals.TITLE] = splitString[(5 * i) + 1];
            music[i][Globals.ARTIST] = splitString[(5 * i) + 2];
            music[i][Globals.ALBUM] = splitString[(5 * i) + 3];
            music[i][Globals.URL] = splitString[(5 * i) + 4];
        }

        return music;
    }
}
