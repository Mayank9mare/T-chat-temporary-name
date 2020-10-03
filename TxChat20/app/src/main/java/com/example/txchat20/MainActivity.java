package com.example.txchat20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Boolean signupmode=true;
    TextView textView;
    Button button;
    public void showUserList(){
        Intent intent=new Intent(getApplicationContext(),userActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.textView){
            if(signupmode){
                signupmode=false;
                textView.setText("Sign up");
                button.setText("Log In");

            }
            else{
                signupmode=true;
                textView.setText("Log in");
                button.setText("Sign Up");
            }
        }
    }

    public  void signup(View view) {
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) {
            Toast.makeText(MainActivity.this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
        }
        if (signupmode) {
            ParseUser user = new ParseUser();
            user.setUsername(usernameEditText.getText().toString());
            user.setPassword(passwordEditText.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("sign up", "sucessful");
                        showUserList();

                    } else {
                        Log.i("sign up", "failed");
                    }
                }

            });


        }
        else{
            ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(user!=null){
                        Log.i("log in","sucessful");
                        showUserList();
                    }
                    else{
                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.textView);
        textView.setOnClickListener((View.OnClickListener) this);
        button=(Button)findViewById(R.id.button);
        if(ParseUser.getCurrentUser()!=null){
            showUserList();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}