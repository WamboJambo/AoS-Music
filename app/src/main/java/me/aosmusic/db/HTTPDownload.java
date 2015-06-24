package me.aosmusic.db;

import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import me.aosmusic.testprogram.MainActivity;

/**
 * Created by James on 24-06-2015.
 */
public class HTTPDownload extends AsyncTask<String, Void, String> {

    ProgressBar pb = new ProgressBar(MainActivity.getMainActivity());

    @Override
    protected String doInBackground(String...params) {

        int count;

        try {


            URL url = new URL(params[0]);

            URLConnection conn = url.openConnection();
            conn.connect();
            int length = conn.getContentLength();
            pb.setMax(length);

            boolean success = true;

            File folder = new File(Environment.getExternalStorageDirectory() + "/aosmusic/");
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                File song = new File(Environment.getExternalStorageDirectory() + "/aosmusic/" + params[1] + ".mp3");
                InputStream in = new BufferedInputStream(url.openStream());
                OutputStream out = new FileOutputStream(song);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = in.read(data)) != -1) {
                    total += count;

                    showProgress((int)(total * 100 / length));
                    out.write(data, 0, count);
                }

                return "File downloaded successfully!";
            }
            return "Error - File not successfully downloaded";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String dlReturn) {
        Toast.makeText(MainActivity.getMainActivity(), dlReturn, Toast.LENGTH_LONG).show();
    }

    public void showProgress(int progress) {
        ProgressBar pb = new ProgressBar(MainActivity.getMainActivity());
        pb.setProgress(progress);
    }
}
