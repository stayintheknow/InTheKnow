package stayintheknow.intheknow.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import stayintheknow.intheknow.R;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    private Button setting_changes;
    private Button submit_changes;
    private EditText profileName;
    private EditText profileEmail;
    private EditText profileBio;
    private EditText profileFullName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setting_changes = view.findViewById(R.id.settingBtn);
        submit_changes = view.findViewById(R.id.submit_changes);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileBio = view.findViewById(R.id.profileBio);
        profileFullName = view.findViewById(R.id.etfullName);


        // Retrieve user's current informatio
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            profileName.setText(currentUser.getUsername());
            profileEmail.setText(currentUser.getEmail());
            profileBio.setText(currentUser.get("bio").toString());
            profileFullName.setText(currentUser.get("name").toString());
        }

        setting_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.setEmail(profileEmail.getText().toString());
                currentUser.setUsername(profileName.getText().toString());
                currentUser.put("bio", profileBio.getText().toString());
                currentUser.put("name", profileFullName.getText().toString());

                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null) {
                            Log.e(TAG, "Error: saving user settings");
                            Toast.makeText(getContext(),"Unable to save settings at this time", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            return;
                        }
                        Log.d(TAG, "Success: Saved user settings");
                        Toast.makeText(getContext(), "Settings updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        submit_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Preferences updated", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
