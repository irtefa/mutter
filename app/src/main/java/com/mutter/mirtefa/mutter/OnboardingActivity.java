package com.mutter.mirtefa.mutter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

import de.greenrobot.event.EventBus;


public class OnboardingActivity extends FragmentActivity implements View.OnClickListener {
    TextView mLoginButton;
    EditText mUsername;
    EditText mPwd;
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        mLoginButton = (TextView) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(this);
        mUsername = (EditText) findViewById(R.id.user_name_field);
        mPwd = (EditText) findViewById(R.id.pwd_field);

        if (ParseUser.getCurrentUser() != null) {
            Intent i = new Intent(this, ChatActivity.class);
            startActivity(i);
        }
        user = new ParseUser();
    }

    @Override
    public void onClick(View v) {
        final int vId = v.getId();

        switch (vId) {
            case(R.id.login_button): {
                final String username = mUsername.getText().toString();
                final String pwd = mPwd.getText().toString();

                user.setUsername(username);
                user.setPassword(pwd);

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", username);
                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            // The query was successful.
                            if (objects.size() == 0) {
                                user.signUpInBackground(new SignUpCallback() {
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                                            startActivity(i);
                                            // Hooray! Let them use the app now.
                                        } else {
                                            // Sign up didn't succeed. Look at the ParseException
                                            // to figure out what went wrong
                                            new AlertDialog.Builder(OnboardingActivity.this)
                                                    .setTitle("ERROR")
                                                    .setMessage(e.getMessage())
                                                    .show();
                                        }
                                    }
                                });
                            } else {
                                ParseUser.logInInBackground(username, pwd, new LogInCallback() {
                                    public void done(ParseUser user, ParseException e) {
                                        if (user != null) {
                                            // Hooray! The user is logged in.
                                            Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                                            startActivity(i);
                                        } else {
                                            // Signup failed. Look at the ParseException to see what happened.
                                            new AlertDialog.Builder(OnboardingActivity.this)
                                                    .setTitle("ERROR")
                                                    .setMessage(e.getMessage())
                                                    .show();
                                        }
                                    }
                                });                            }
                        } else {
                            // Something went wrong.
                            new AlertDialog.Builder(OnboardingActivity.this)
                                    .setTitle("ERROR")
                                    .setMessage(e.getMessage())
                                    .show();
                        }
                    }
                });
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_onboarding, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
