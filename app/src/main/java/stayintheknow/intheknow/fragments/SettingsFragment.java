package stayintheknow.intheknow.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import de.hdodenhof.circleimageview.CircleImageView;
import stayintheknow.intheknow.NewsFeedActivity;
import stayintheknow.intheknow.R;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    private Button setting_changes;
    private Button submit_changes;
    private Button imageBtn;
    private CircleImageView civProfileImage;
    private EditText profileName;
    private EditText profileEmail;
    private EditText profileBio;
    private EditText profileFullName;
    private EditText etpassword;
    private EditText etpasswordConfirm;

    private CheckBox cbWorld;
    private CheckBox cbNewYork;
    private CheckBox cbUS;
    private CheckBox cbTech;
    private CheckBox cbPolitics;
    private CheckBox cbSports;
    private CheckBox cbHealth;

    private File profileImgFile;
    private Bitmap takenImage;


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
        etpassword = view.findViewById(R.id.etPassword);
        etpasswordConfirm = view.findViewById(R.id.etPasswordConfirm);

        //Preferences
        cbHealth = view.findViewById(R.id.cbHealth);
        cbNewYork = view.findViewById(R.id.cbNewYork);
        cbPolitics = view.findViewById(R.id.cbPolitics);
        cbSports = view.findViewById(R.id.cbSports);
        cbTech = view.findViewById(R.id.cbTech);
        cbUS = view.findViewById(R.id.cbUS);
        cbWorld = view.findViewById(R.id.cbWorld);
//        imageBtn = view.findViewById(R.id.imageBtn);
//        civProfileImage = view.findViewById(R.id.civProfileImage);


        // Retrieve user's current informatio
        final ParseUser currentUser = ParseUser.getCurrentUser();
        Object currentBio = currentUser.get("bio");
        Object curFullName = currentUser.get("name");
        if (currentUser != null ) {
            profileName.setText(currentUser.getUsername());
            profileEmail.setText(currentUser.getEmail());
            if(currentBio != null) {
                profileBio.setText(currentBio.toString());
            }
            if( curFullName != null) {
                profileFullName.setText(curFullName.toString());
            }
//            ParseFile image = currentUser.getParseFile("profileImage");
//            Log.d(TAG, "onViewCreated: img null " + (image == null));
//            if(image != null) {
//                Glide.with(getContext()).load(image.getUrl()).into(civProfileImage);
//            }

            // Set Preferences
            boolean world = currentUser.getBoolean("World");
            boolean us = currentUser.getBoolean("US");
            boolean ny = currentUser.getBoolean("NewYork");
            boolean tech = currentUser.getBoolean("tech");
            boolean health = currentUser.getBoolean("health");
            boolean sports = currentUser.getBoolean("sports");
            boolean politics = currentUser.getBoolean("politics");

            if(world) cbWorld.setChecked(true);
            if(us) cbUS.setChecked(true);
            if(ny) cbNewYork.setChecked(true);
            if(tech) cbTech.setChecked(true);
            if(health) cbHealth.setChecked(true);
            if(sports) cbSports.setChecked(true);
            if(politics) cbPolitics.setChecked(true);

        }

//        imageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(),"Upload image", Toast.LENGTH_SHORT).show();
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, 1);
//            }
//        });

        setting_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = profileName.getText().toString();
                String password = etpassword.getText().toString();
                String passwordConfirm = etpasswordConfirm.getText().toString();

                if(username.length() < 1) {
                    Toast.makeText(getContext(),"You must have a username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length() > 0) {
                    if(!password.equals(passwordConfirm)){
                        Toast.makeText(getContext(),"Password must match", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(password.length() < 7) {
                        Toast.makeText(getContext(),"Password must be at least 7 characters", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    currentUser.setPassword(password);
                }

                currentUser.setEmail(profileEmail.getText().toString());
                currentUser.setUsername(profileName.getText().toString());
                currentUser.put("bio", profileBio.getText().toString());
                currentUser.put("name", profileFullName.getText().toString());
                if(profileImgFile == null || civProfileImage.getDrawable() == null) {
                    Toast.makeText(getContext(), "No photo to save",Toast.LENGTH_SHORT).show();
                } else {
//                    currentUser.put("profileImage", new ParseFile(profileImgFile, takenImage));
                    Toast.makeText(getContext(), "Photo saved",Toast.LENGTH_SHORT).show();
                }

                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null) {
                            Log.e(TAG, "Error: saving user settings", e);
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
                ArrayList<String> selectedCat = new ArrayList<>();

                if(!cbHealth.isChecked()) {
                    currentUser.put("health", false);
                } else if (cbHealth.isChecked()) {
                    selectedCat.add("nav_cat_health");
                    currentUser.put("health", true);
                }
                if(!cbNewYork.isChecked()) {
                    currentUser.put("NewYork", false);
                } else if(cbNewYork.isChecked()) {
                    selectedCat.add("nav_cat_ny");
                    currentUser.put("NewYork", true);
                }
                if(!cbPolitics.isChecked()) {
                    currentUser.put("politics", false);
                } else if(cbPolitics.isChecked()) {
                    selectedCat.add("nav_cat_politics");
                    currentUser.put("politics", true);
                }
                if(!cbSports.isChecked()) {
                    currentUser.put("sports", false);
                } else if(cbSports.isChecked()) {
                    selectedCat.add("nav_cat_sports");
                    currentUser.put("sports", true);
                }
                if(!cbTech.isChecked()) {
                    currentUser.put("tech", false);
                } else if(cbTech.isChecked()) {
                    selectedCat.add("nav_cat_tech");
                    currentUser.put("tech", true);
                }
                if(!cbUS.isChecked()) {
                    currentUser.put("US", false);
                } else if(cbUS.isChecked()) {
                    selectedCat.add("nav_cat_us");
                    currentUser.put("US", true);
                }
                if(!cbWorld.isChecked()) {
                    currentUser.put("World", false);
                } else if(cbWorld.isChecked()) {
                    selectedCat.add("nav_cat_world");
                    currentUser.put("World", true);
                }

                try {
                    currentUser.save();
                    Log.d(TAG, "Success: preferences saves");
                } catch (ParseException e) {
                    Log.e(TAG, "Error: while saving preferences", e);
                    e.printStackTrace();
                }

                Intent intent = new Intent(getContext(), NewsFeedActivity.class);
                intent.putStringArrayListExtra("selectedCategories", selectedCat);
                startActivity(intent);

                Toast.makeText(getContext(), "Preferences updated", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            Log.d(TAG, "onActivityResult: request code 1");
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "onActivityResult: result code OK");
                Uri selectedImage = data.getData();

                String filePath = getPath(selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
//                image_name_tv.setText(filePath);

                try {
                    if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
                        //FINE
                        Toast.makeText(getContext(), "Image retrieved", Toast.LENGTH_SHORT).show();
                        if (filePath != null) {
                            profileImgFile = new File(filePath);
                            Log.d(TAG, "UPLOAD: file length = " + profileImgFile.length());
                            Log.d(TAG, "UPLOAD: file exist = " + profileImgFile.exists());
//                            Glide.with(getContext()).load(filePath).into(civProfileImage);
//                            Picasso.get().load(filePath).resize(940, 688).into(civProfileImage);
                            // by this point we have the photo on disk
                            takenImage = BitmapFactory.decodeFile(profileImgFile.getAbsolutePath());
                            // Load the taken image into a preview
                            civProfileImage.setImageBitmap(takenImage);

                            ParseUser user = ParseUser.getCurrentUser();
                            user.put("profileImage", filePath);
                        }
                    } else {
                        //NOT IN REQUIRED FORMAT
                        Log.e(TAG, "onActivityResult: Improper image format");
                        Toast.makeText(getContext(), "Improper image format", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.e(TAG, "onActivityResult: File Exception thrown", e);
                    e.printStackTrace();
                }
            }
        }
    }

    public String getPath(Uri uri) {
        Log.d(TAG, "getPath: getting image path");
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);
        Log.d(TAG, "getPath: returning image path");

        return cursor.getString(column_index);
    }

}
