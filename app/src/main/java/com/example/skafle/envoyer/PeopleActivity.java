package com.example.skafle.envoyer;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PeopleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        TextView importantInfo = (TextView) findViewById(R.id.people_info_txt);
        Intent intent = getIntent();

        if (intent.hasExtra(SendActivity.PEOPLE_KEY)) {
            String resourceText = intent.getStringExtra(SendActivity.PEOPLE_KEY);
            importantInfo.setText(resourceText);
        }
    }
}
