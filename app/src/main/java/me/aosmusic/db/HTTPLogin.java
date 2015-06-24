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
public class HTTPLogin extends AsyncTask<String, String, String> {

    final String TAG = "HTTPLogin";
    private LoginActivity loginActivity;

    @Override
    protected String doInBackground(String...params) {
        HttpURLConnection conn;
        OutputStreamWriter request;
        URL url;

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
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String response = sb.toString();
            returnParams.close();
            reader.close();
            return response;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String status) {
        if (Character.isDigit(status.charAt(0))) {
            loginActivity = LoginActivity.getLoginActivity();
            loginActivity.login(Character.getNumericValue(status.charAt(0)));
        } else {
            Log.e(TAG, status);
        }
    }
}
