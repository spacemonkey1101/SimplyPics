package com.example.parse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Boolean signupmodeActive   = true;
    Button button;
    TextView changeSignup ;
    EditText username ;
    EditText password ;

    public void showuserList(){
        Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
        startActivity(intent);
    }

    public void signUp(View view){
         username = findViewById(R.id.usernameeditText);

         password = findViewById(R.id.passwordeditText);

        if(username.getText().toString().matches("") || password.getText().toString().matches(""))
            Toast.makeText(this, "A username and password are required", Toast.LENGTH_SHORT).show();
        else

        {

            if(signupmodeActive) {

                ParseUser user = new ParseUser();

                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Signup", "successfull");
                            showuserList();
                        }
                            else
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }else{
                ParseUser.logInInBackground(username.getText().toString(),password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e)
                    {
                        if(user != null)
                        {
                            Log.i("Sign Up","login successful");
                            showuserList();

                        }else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Simply Pics");

        changeSignup = findViewById(R.id.textView);
         changeSignup.setOnClickListener(this);

         RelativeLayout relativeLayout =findViewById(R.id.backgroundLayout);

        ImageView logo  = findViewById(R.id.imageView);


         relativeLayout.setOnClickListener(this);
        logo.setOnClickListener(this);

        password = findViewById(R.id.passwordeditText);
         password.setOnKeyListener(this);

    if(ParseUser.getCurrentUser() != null){
        showuserList();
    }


        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

    @Override
    public void onClick(View view) {
     if (view.getId() == R.id.textView)
     {
         if(signupmodeActive)
         {
              button = findViewById(R.id.button);
              signupmodeActive = false;
              button.setText("Login");
              changeSignup.setText("or Signup");
         }else{
             button = findViewById(R.id.button);
             signupmodeActive = true;
             button.setText("Signup");
             changeSignup.setText("or Login");
         }
     }else if(view.getId() == R.id.backgroundLayout  || view.getId() == R.id.imageView){
         InputMethodManager inputMethodManager  = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
         inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);


     }

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if(i== keyEvent.KEYCODE_ENTER  && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            signUp(view);
        }

        return false;
    }
}
