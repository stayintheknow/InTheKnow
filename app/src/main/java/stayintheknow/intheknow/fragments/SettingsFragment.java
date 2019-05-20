package stayintheknow.intheknow.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;

import stayintheknow.intheknow.R;

public class SettingsFragment extends Fragment {

    private Button nameBtn;
    private Button emailBtn;
    private Button bioBtn;
    private Button submit_changes;
    private EditText profileName;
    private EditText profileEmail;
    private EditText profileBio;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameBtn = view.findViewById(R.id.nameBtn);
        emailBtn = view.findViewById(R.id.emailBtn);
        bioBtn = view.findViewById(R.id.bioBtn);
        submit_changes = view.findViewById(R.id.submit_changes);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileBio = view.findViewById(R.id.profileBio);


        // Retrieve user's current information

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            profileName.setText(currentUser.getUsername());
            profileEmail.setText(currentUser.getEmail());
            profileBio.setText(currentUser.get("bio").toString());

        }




    }
}
