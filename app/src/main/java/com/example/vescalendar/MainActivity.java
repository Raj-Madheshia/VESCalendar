package com.example.vescalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    SqliteDatabaseHelper sqliteDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        sqliteDatabaseHelper = new SqliteDatabaseHelper(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nv);

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        try{
            Bundle extras = getIntent().getExtras();
            String toOpen = extras.getString("toOpen");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentEvents()).commit();
        }catch (Exception e){
            Log.w( "Bunddleeeeeeeeeeee", e );
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentCalendar()).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentCalendar()).commit();
            navigationView.setCheckedItem(R.id.calendar);
        }

//        if (savedInstanceState == null) {
//
//        }



    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.calendar:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentCalendar()).commit();
                break;
            case R.id.events:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentEvents()).commit();
                break;
            case R.id.logout:
                Toast.makeText(this, "Logout Pressed", Toast.LENGTH_SHORT).show();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
