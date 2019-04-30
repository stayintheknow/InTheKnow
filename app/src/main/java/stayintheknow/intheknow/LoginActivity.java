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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_REGISTRATION = 0;

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Set on click listeners
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                login(username, password);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate user to the Registration Screen if they are creating an account for the first time
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivityForResult(intent, REQUEST_REGISTRATION);
            }
        });
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with login");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_LONG).show();
                    return;
                }
                // TODO: navigate to new activity if user has signed in properly
                goNewsFeed();
            }
        });
    }


    private void goNewsFeed() {
        Log.d(TAG, "Login successful");
        Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this,NewsFeedActivity.class);
        startActivity(i);
        finish();
    }
}
