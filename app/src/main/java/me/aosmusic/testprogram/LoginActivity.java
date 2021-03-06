package me.aosmusic.testprogram;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import me.aosmusic.constants.Globals;
import me.aosmusic.db.HTTPLogin;

public class LoginActivity extends Activity {

    public ActionBar actionBar;
    public Button loginButton;
    public EditText userText;
    public EditText passText;
    public final String TAG = "LoginActivity";
    private static LoginActivity loginPage;
    private AsyncTask<String, String, String> asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loginPage = this;
        setTheme(Globals.getThemeNum());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        loginButton = (Button)findViewById(R.id.loginButton);
        userText = (EditText)findViewById(R.id.userText);
        passText = (EditText)findViewById(R.id.passText);
        makeLoginButton();
    }

    /**
     * Singleton getter for LoginActivity page
     *
     * @return LoginActivity loginPage the created LoginActivity
     */
    public static LoginActivity getLoginActivity() {
        return loginPage;
    }

    public void login(int status) {
        switch (status) {
            case 0:
                Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                asyncTask.cancel(true);
                makeLoginButton();
                break;
            case 1:
                Toast.makeText(LoginActivity.this,
                        "You have not verified your account.  Please check your registered email's inbox and spam for a verification message.", Toast.LENGTH_LONG).show();
                asyncTask.cancel(true);
                makeLoginButton();
                break;
            case 2:
                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                break;
        }
        // Necessary (and recommended against?) to call UI functions outside of main thread
        Looper.loop();
    }

    public void makeLoginButton() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userText.getText().toString() == "" || passText.getText().toString() == "")
                    Toast.makeText(LoginActivity.this, "Please enter a valid username and password", Toast.LENGTH_SHORT).show();
                else
                    asyncTask = new HTTPLogin().execute(userText.getText().toString(), passText.getText().toString());
            }
        });
    }
}