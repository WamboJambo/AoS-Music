package me.aosmusic.testprogram;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import me.aosmusic.constants.Globals;
import me.aosmusic.db.HTTPDownload;
import me.aosmusic.db.HTTPRequest;

/**
 * Created by corsijn on 5/27/2015.
 */
public class MainActivity extends Activity {

    public final String TAG = "MainActivity";
    public static String[][] music;
    private AsyncTask<String, Void, String> selectAsyncTask;
    private AsyncTask<String, Void, String> dlAsyncTask;
    private static MainActivity mainPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainPage = this;
        setTheme(Globals.getThemeNum());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        selectAsyncTask = new HTTPRequest().execute("SELECT * FROM music");
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
                        if (StreamFragment.getMediaPlayer() != null)
                            StreamFragment.getMediaPlayer().stop();
                        StreamFragment.destroyMP();
                        if (DownloadFragment.getMediaPlayer() != null)
                            DownloadFragment.getMediaPlayer().stop();
                        DownloadFragment.destroyMP();
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
                if (StreamFragment.getMediaPlayer() != null)
                    StreamFragment.getMediaPlayer().stop();
                StreamFragment.destroyMP();
                if (DownloadFragment.getMediaPlayer() != null)
                    DownloadFragment.getMediaPlayer().stop();
                DownloadFragment.destroyMP();
                Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                break;
            case R.id.action_downloaded:
                Intent downloadedIntent = new Intent(MainActivity.this, DownloadActivity.class);
                downloadedIntent.putExtra("URL", getMusic()[0][Globals.URL]);
                downloadedIntent.putExtra("Title", getMusic()[0][Globals.TITLE]);
                downloadedIntent.putExtra("Artist", getMusic()[0][Globals.ARTIST]);
                downloadedIntent.putExtra("Album", getMusic()[0][Globals.ALBUM]);
                downloadedIntent.putExtra("ID", getMusic()[0][Globals.ID]);
                startActivity(downloadedIntent);
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static MainActivity getMainActivity() {
        return mainPage;
    }

    public void buildMainPage(String queryReturn) {
        String[][] songs = parseQueryReturn(queryReturn);

        for (int i = 0; i < songs.length; i++) {
            createRow(songs[i]);
        }

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
                                      AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                      builder.setTitle("Stream or Download?");
                                      builder.setMessage("Choose to either stream this track or download for future listening.");
                                      builder.setPositiveButton("Stream", new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialog, int which) {
                                              playStream(rowInfo);
                                          }
                                      });
                                      builder.setNeutralButton("Download", new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialog, int which) {
                                              download(rowInfo);
                                          }
                                      });
                                      builder.setNegativeButton("Cancel", null);
                                      builder.show();
                                  }
                              });

        tl.addView(tr);
    }

    private String[][] parseQueryReturn(String toParse) {
        String[] splitString;

        splitString = toParse.split("x\\|x");

        music = new String[(splitString.length / 5)][5];

        for (int i = 0; i < splitString.length / 5; i++) {
            music[i][Globals.ID] = splitString[(5 * i)];
            music[i][Globals.TITLE] = splitString[(5 * i) + 1];
            music[i][Globals.ARTIST] = splitString[(5 * i) + 2];
            music[i][Globals.ALBUM] = splitString[(5 * i) + 3];
            music[i][Globals.URL] = splitString[(5 * i) + 4];
        }

        return music;
    }

    public void playStream(String[] rowInfo) {
        Intent i = new Intent(MainActivity.this, StreamActivity.class);
        i.putExtra("ID", rowInfo[Globals.ID]);
        i.putExtra("URL", rowInfo[Globals.URL]);
        i.putExtra("Title", rowInfo[Globals.TITLE]);
        i.putExtra("Artist", rowInfo[Globals.ARTIST]);
        i.putExtra("Album", rowInfo[Globals.ALBUM]);
        startActivity(i);
    }

    public void download(String[] rowInfo) {
        dlAsyncTask = new HTTPDownload().execute(rowInfo[Globals.URL], rowInfo[Globals.ID]);

        Intent i = new Intent(MainActivity.this, DownloadActivity.class);
        i.putExtra("ID", rowInfo[Globals.ID]);
        i.putExtra("URL", rowInfo[Globals.URL]);
        i.putExtra("Title", rowInfo[Globals.TITLE]);
        i.putExtra("Artist", rowInfo[Globals.ARTIST]);
        i.putExtra("Album", rowInfo[Globals.ALBUM]);
        startActivity(i);
    }

    public static String[][] getMusic() {
        return music;
    }
}
