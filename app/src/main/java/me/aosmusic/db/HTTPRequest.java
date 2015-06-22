package me.aosmusic.db;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import me.aosmusic.testprogram.MainActivity;

/**
 * Created by corsijn on 6/22/2015.
 */
public class HTTPRequest extends AsyncTask<String, Void, String> {

    final String TAG = "HTTPRequest";
    private MainActivity mainActivity;

    @Override
    protected String doInBackground(String...params) {
        HttpURLConnection conn;
        OutputStreamWriter request;
        URL url;
        String parameters = "query=" + params[0];

        try {
            url = new URL("http://aosmusic.me/android/mysqlRequest.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");

            request = new OutputStreamWriter(conn.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader returnParams = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(returnParams);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            String response = sb.toString();
            returnParams.close();
            reader.close();
            return response;
        } catch (IOException e){
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String queryReturn) {
        mainActivity = MainActivity.getMainActivity();
        mainActivity.buildMainPage(queryReturn);
    }
}
