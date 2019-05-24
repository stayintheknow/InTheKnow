package stayintheknow.intheknow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import stayintheknow.intheknow.fragments.SettingsFragment;

public class RegistrationActivity extends AppCompatActivity {
    public static final String TAG = "RegistrationActivity";

    private EditText etfullName;
    private EditText etnewUserName;
    private EditText etnewPassword;
    private EditText etconfirmPassword;
    private EditText etEmail;
    private Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etfullName = findViewById(R.id.etfullName);
        etnewUserName = findViewById(R.id.etnewUserName);
        etnewPassword = findViewById(R.id.etnewPassword);
        etconfirmPassword = findViewById(R.id.etconfirmPassword);
        etEmail = findViewById(R.id.etEmail);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Register button has been clicked");
                String name = etfullName.getText().toString();
                String email = etEmail.getText().toString();
                String username = etnewUserName.getText().toString();
                String password = etnewPassword.getText().toString();
                String passwordConfirm = etconfirmPassword.getText().toString();

                if(username.length() < 1){
                    Toast.makeText(getApplicationContext(), "Must provide username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(email.length() < 1){
                    Toast.makeText(getApplicationContext(), "Must provide email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!password.equals(passwordConfirm)){
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    etnewPassword.setText("");
                    etconfirmPassword.setText("");
                    return;
                }

                if(password.length() < 7) {
                    Toast.makeText(getApplicationContext(),"Password must be at least 7 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                signUp(name, username, password, email);
            }
        });
    }

    private void signUp(String name, final String username, final String password, String email) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set properties
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.put("name", name);

//        Toast.makeText(getApplicationContext(), "Creating Account", Toast.LENGTH_LONG).show();
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Toast.makeText(RegistrationActivity.this, "Unable to create an account at this time", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Issue with signup");
                    e.printStackTrace();
                    return;
                }
                Toast.makeText(RegistrationActivity.this, "Account Created successfully, Logging in", Toast.LENGTH_SHORT).show();
                login(username, password);
            }
        });
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null) {
                    //TODO: report to user that they have entered wrong login information
                    Toast.makeText(RegistrationActivity.this, "Invalid Username or Password! Please Try Again.", Toast.LENGTH_SHORT).show();
                    etnewPassword.setText("");
                    etconfirmPassword.setText("");
                    etfullName.setText("");
                    etnewUserName.setText("");
                    etEmail.setText("");
                    Log.e(TAG, "Issue with login");
                    e.printStackTrace();
                    return;
                }
                //TODO: navigate to newsfeed if the user has signed in properly
                goToNewsfeed();
            }
        });
    }

    private void goToNewsfeed() {
        Log.d(TAG, "Login successful");
        Intent i = new Intent(this, NewsFeedActivity.class);
        startActivity(i);
        finish();
    }

}
