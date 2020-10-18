package com.example.inspectorapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.inspectorapp.Common.CommonConstants;
import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    /**
     * This is on create method of Home Activity
     *
     * Assign Toolbar
     * Assign Navigation Drawer
     * Set values for Navigation Drawer
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbarHome);
        toolbar.setTitle(CommonConstants.TOOLBAR_HEADING_FOR_HOME);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.home_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameHome,
                    new ScanFragment()).commit();
            navigationView.setCheckedItem(R.id.menu_home);
        }
    }

    /**
     * This is happened in When press the back navigation
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            } else {
                Toast.makeText(getBaseContext(), CommonConstants.TOAST_CLOSE_APP, Toast.LENGTH_SHORT).show();
            }
            mBackPressed = System.currentTimeMillis();
        }
    }

    /**
     * Navigation Drawer Item Select Option
     * <p>
     * When Select Logout, Intent move to MainAvtivity
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                Toast.makeText(getApplicationContext(), CommonConstants.TOAST_LOGOUT, Toast.LENGTH_LONG).show();
                Intent mainAct = new Intent(Home.this, MainActivity.class);
                startActivity(mainAct);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}