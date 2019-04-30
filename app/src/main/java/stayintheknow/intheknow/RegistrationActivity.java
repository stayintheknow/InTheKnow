package stayintheknow.intheknow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etfullName;
    private EditText etnewUserName;
    private EditText etnewPassword;
    private EditText etconfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etfullName = findViewById(R.id.etfullName);
        etnewUserName = findViewById(R.id.etnewUserName);
        etnewPassword = findViewById(R.id.etnewPassword);
        etconfirmPassword = findViewById(R.id.etconfirmPassword);
    }




}
