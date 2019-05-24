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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import stayintheknow.intheknow.fragments.HealthFragment;
import stayintheknow.intheknow.fragments.NYFragment;
import stayintheknow.intheknow.fragments.NewsfeedFragment;
import stayintheknow.intheknow.fragments.PoliticsFragment;
import stayintheknow.intheknow.fragments.ProfileFragment;
import stayintheknow.intheknow.fragments.SettingsFragment;
import stayintheknow.intheknow.fragments.SportsFragment;
import stayintheknow.intheknow.fragments.TechFragment;
import stayintheknow.intheknow.fragments.USFragment;
import stayintheknow.intheknow.fragments.WorldFragment;

/**
 * The NewsFeedActivity handles the menu drawer that features the news feed, setting, profile, as
 * well as all of the news categories and the logout button
 * this activity takes care of navigating the user to the proper fragment based on the button that
 * they select from the menu bar
 */

public class NewsFeedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "NewsFeedActivity";

    private DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Menu menu = navigationView.getMenu();

        MenuItem nav_world = menu.findItem(R.id.nav_cat_world);
        MenuItem nav_us = menu.findItem(R.id.nav_cat_us);
        MenuItem nav_ny = menu.findItem(R.id.nav_cat_ny);
        MenuItem nav_tech = menu.findItem(R.id.nav_cat_tech);
        MenuItem nav_health = menu.findItem(R.id.nav_cat_health);
        MenuItem nav_sport = menu.findItem(R.id.nav_cat_sports);
        MenuItem nav_politics = menu.findItem(R.id.nav_cat_politics);

        /*set menu items from current user*/
        ParseUser currentUser = ParseUser.getCurrentUser();

        boolean world = currentUser.getBoolean("World");
        boolean us = currentUser.getBoolean("US");
        boolean ny = currentUser.getBoolean("NewYork");
        boolean tech = currentUser.getBoolean("tech");
        boolean health = currentUser.getBoolean("health");
        boolean sports = currentUser.getBoolean("sports");
        boolean politics = currentUser.getBoolean("politics");

        if(!world) nav_world.setVisible(false);
        if(!us) nav_us.setVisible(false);
        if(!ny) nav_ny.setVisible(false);
        if(!tech) nav_tech.setVisible(false);
        if(!health) nav_health.setVisible(false);
        if(!sports) nav_sport.setVisible(false);
        if(!politics) nav_politics.setVisible(false);

        /*Set menu items from settings*/
        ArrayList<String> extras = getIntent().getStringArrayListExtra("selectedCategories");
        if (extras != null) {
            for (String extra: extras) {
                switch(extra) {
                    case "nav_cat_world":
                        Log.d(TAG, "Invisible: World News");
                        nav_world.setVisible(true);
                        break;
                    case "nav_cat_us":
                        Log.d(TAG, "Invisible: US News");
                        nav_us.setVisible(true);
                        break;
                    case "nav_cat_ny":
                        Log.d(TAG, "Invisible: NY News");
                        nav_ny.setVisible(true);
                        break;
                    case "nav_cat_politics":
                        Log.d(TAG, "Invisible: Political News");
                        nav_politics.setVisible(true);
                        break;
                    case "nav_cat_tech":
                        Log.d(TAG, "Invisible: Tech News");
                        nav_tech.setVisible(true);
                        break;
                    case "nav_cat_health":
                        Log.d(TAG, "Invisible: Health News");
                        nav_health.setVisible(true);
                        break;
                    case "nav_cat_sports":
                        Log.d(TAG, "Invisible: Sports News");
                        nav_sport.setVisible(true);
                        break;
                    default:
                        Log.e(TAG, "No category");
                        break;
                }
            }
        }


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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WorldFragment()).commit();
                break;
            case R.id.nav_cat_us:
                Toast.makeText(this, "U.S. News", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new USFragment()).commit();
                break;
            case R.id.nav_cat_ny:
                Toast.makeText(this, "New York News", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NYFragment()).commit();
                break;
            case R.id.nav_cat_politics:
                Toast.makeText(this, "Political News", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PoliticsFragment()).commit();
                break;
            case R.id.nav_cat_tech:
                Toast.makeText(this, "Tech News", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TechFragment()).commit();
                break;
            case R.id.nav_cat_health:
                Toast.makeText(this, "Health News", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HealthFragment()).commit();
                break;
            case R.id.nav_cat_sports:
                Toast.makeText(this, "Sports News", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SportsFragment()).commit();
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

    public void updateCategories(List<String> categories) {
        NavigationView navigationView = findViewById(R.id.nav_view);

        // get menu from navigationView
        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        MenuItem nav_world = menu.findItem(R.id.nav_cat_world);
        MenuItem nav_us = menu.findItem(R.id.nav_cat_us);
        MenuItem nav_ny = menu.findItem(R.id.nav_cat_ny);
        MenuItem nav_tech = menu.findItem(R.id.nav_cat_tech);
        MenuItem nav_health = menu.findItem(R.id.nav_cat_health);
        MenuItem nav_sport = menu.findItem(R.id.nav_cat_sports);
        MenuItem nav_politics = menu.findItem(R.id.nav_cat_politics);
    }
}
