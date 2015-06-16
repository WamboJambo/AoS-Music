package me.aosmusic.db;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import me.aosmusic.testprogram.LoginActivity;

/**
 * Created by corsijn on 6/16/2015.
 */
public class HTTPLogin extends AsyncTask<String, String, Void> {

    @Override
    protected Void doInBackground(String...params) {
        HttpURLConnection conn;
        OutputStreamWriter request;
        URL url;

        final String TAG = "HTTPLogin";

        String parameters = "user=" + params[0] + "&pass=" + params[1];

        try {
            url = new URL("http://aosmusic.me/loginRequest.php");
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
            int response = Character.getNumericValue(reader.readLine().charAt(0));
            LoginActivity loginPage = LoginActivity.getLoginActivity();
            // Necessary (and recommended against?) to call UI functions outside of main thread
            Looper.prepare();
            loginPage.login(response);
            returnParams.close();
            reader.close();
            return null;
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }
}
