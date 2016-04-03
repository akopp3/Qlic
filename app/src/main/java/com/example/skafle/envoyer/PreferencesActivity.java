package com.example.skafle.envoyer;

import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

public class PreferencesActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_preferences);
            getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferenceScreenFragment()).commit();
        }

        public static class PreferenceScreenFragment extends PreferenceFragment {
            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.preferences); //
        }

    }
}
