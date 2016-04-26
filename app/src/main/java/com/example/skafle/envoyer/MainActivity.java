package com.example.skafle.envoyer;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    public static final String[] keys = {"fb_name", "ig_name", "twit_name", "phone_name", "contact_name", "link_name"};
    public static final String[] types = {"Facebook", "Instagram", "Twitter", "Phone", "Contact Info", "LinkedIn"};
    public static final String[] enabledKeys = {"fb_enabled", "ig_enabled", "twit_enabled", "phone_enabled", "contact_enabled", "link_enabled"};

    SharedPreferences sharedPreferences;
    public static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab); //THIS IS SO FAB ;)

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), SendActivity.class);
                    if (Build.VERSION.SDK_INT >= 21) {
                        ActivityOptions options = ActivityOptions
                                .makeSceneTransitionAnimation(MainActivity.this, fab, "fab");
                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            });
        }


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }
}
