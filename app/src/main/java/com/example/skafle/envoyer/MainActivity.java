package com.example.skafle.envoyer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SharedPreferences sharedPreferences;
    private TextView facebookText;
    private TextView instaText;
    private TextView twitText;
    private TextView phoneText;
    private TextView contactText;
    private TextView linkText;
    private EditText fbEdit;
    private EditText instaEdit;
    private EditText twitEdit;
    private EditText phoneEdit;
    private EditText contactEdit;
    private EditText linkEdit;
    private Button fbButton;
    private Button instaButton;
    private Button twitButton;
    private Button phoneButton;
    private Button contactButton;
    private Button linkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab); //THIS IS SO FAB ;)
        facebookText = (TextView) findViewById(R.id.fb_text);
        instaText = (TextView) findViewById(R.id.inst_text);
        twitText = (TextView) findViewById(R.id.twit_text);
        phoneText = (TextView) findViewById(R.id.phone_text);
        contactText = (TextView) findViewById(R.id.cont_text);
        linkText = (TextView) findViewById(R.id.link_text);
        fbEdit = (EditText) findViewById(R.id.fb_input);
        instaEdit = (EditText) findViewById(R.id.inst_input);
        twitEdit = (EditText) findViewById(R.id.twit_input);
        phoneEdit = (EditText) findViewById(R.id.phone_input);
        contactEdit = (EditText) findViewById(R.id.cont_input);
        linkEdit = (EditText) findViewById(R.id.link_input);
        fbButton = (Button) findViewById(R.id.fb_button);
        instaButton = (Button) findViewById(R.id.inst_button);
        twitButton = (Button) findViewById(R.id.twit_button);
        phoneButton = (Button) findViewById(R.id.phone_button);
        contactButton = (Button) findViewById(R.id.cont_button);
        linkButton = (Button) findViewById(R.id.link_button);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SendActivity.class);
                startActivity(intent);
            }
        });
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        setTextLayouts();
        setEditLayouts();
        /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this); */
    }

    @Override
    public void onResume() {
        super.onResume();
        setTextLayouts();
        setEditLayouts();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setTextLayouts() {
        String[] ids = {"fb", "ig", "twit", "phone", "contact", "link"};
        String[] descript = {"Facebook", "Instagram", "Twitter", "Phone", "Contact", "Linkedin"};
        TextView[] textViews = {facebookText, instaText, twitText, phoneText, contactText, linkText};

        for (int i = 0; i < ids.length; i++) {
            boolean checked = sharedPreferences.getBoolean(ids[i], false);
            if (checked) {
                textViews[i].setVisibility(TextView.VISIBLE);
                textViews[i].setText(descript[i]);
            } else {
                textViews[i].setVisibility(TextView.GONE);
            }
        }
    }
    private void setEditLayouts() {
        String[] ids = {"fb", "ig", "twit", "phone", "contact", "link"};

        String[] keys = {"fb_name", "ig_name", "twit_name", "phone_name", "contact_name", "link_name"};
        final String[] descript = {"Facebook", "Instagram", "Twitter", "Phone", "Contact", "Linkedin"};
        EditText[] editViews = {fbEdit, instaEdit, twitEdit, phoneEdit, contactEdit, linkEdit};
        Button[] buttons = {fbButton, instaButton, twitButton, phoneButton, contactButton, linkButton};

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < ids.length; i++) {
            boolean checked = sharedPreferences.getBoolean(ids[i], false);
            final String editText= editViews[i].getText().toString();
            final String key =  keys[i];
            final String name = descript[i];
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString(key, editText);
                    Snackbar.make(findViewById(android.R.id.content), "Saved: " + name , Snackbar.LENGTH_LONG)
                            .show();
                }
            });
            if (checked) {
                editViews[i].setEnabled(true);
            } else {
                editViews[i].setEnabled(false);
            }
        }
        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            Intent intent = new Intent(getApplicationContext(), PreferencesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        /*
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START); */
        return true; // Will implement when necessary


    }
}
