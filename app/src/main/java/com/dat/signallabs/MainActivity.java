package com.dat.signallabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.signallabs.lab1.Lab1Activity;
import com.dat.signallabs.lab2.Lab2Activity;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    public static final String KEY_SAW = "SAW";
    public static final String KEY_TRIANGULAR = "TRIANGULAR";
    public static final String KEY_RECTANGULAR = "RECTANGULAR";

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    protected DrawerLayout drawer;
    @Bind(R.id.nav_view)
    protected NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle =
            new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lab1:
                Lab1Activity.startActivity(this);
                break;
            case R.id.lab2:
                Lab2Activity.startActivity(this);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
