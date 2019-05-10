package stayintheknow.intheknow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import stayintheknow.intheknow.fragments.NewsfeedFragment;
import stayintheknow.intheknow.fragments.ProfileFragment;
import stayintheknow.intheknow.fragments.SettingsFragment;

/**
 * The NewsFeedActivity handles the menu drawer that features the news feed, setting, profile, as
 * well as all of the news categories and the logout button
 * this activity takes care of navigating the user to the proper fragment based on the button that
 * they select from the menu bar
 */

public class NewsFeedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "NewsFeedActivity";

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*Open Newsfeed as soon as activity is started*/
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewsfeedFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_newsfeed);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_newsfeed:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewsfeedFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.nav_cat_world:
                Toast.makeText(this, "World News", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_cat_us:
                Toast.makeText(this, "U.S. News", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_cat_ny:
                Toast.makeText(this, "New York News", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_cat_tech:
                Toast.makeText(this, "Tech News", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_cat_health:
                Toast.makeText(this, "Health News", Toast.LENGTH_SHORT).show();;
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                logoutAccount();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutAccount() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Toast.makeText(NewsFeedActivity.this, "Unable to logout", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Issue logging out");
                    e.printStackTrace();
                    return;
                }
                goToLogin();
            }
        });
    }

    private void goToLogin() {
        Log.d(TAG, "Navigating to Login Activity");
        Intent intent = new Intent(NewsFeedActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
