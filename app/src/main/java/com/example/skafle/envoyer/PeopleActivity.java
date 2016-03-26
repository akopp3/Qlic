package com.example.skafle.envoyer;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class PeopleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        Intent intent = getIntent();
        String message = intent.getStringExtra(SendActivity.PEOPLE_KEY);
        Receiver receiver = new Receiver(message);

        TextView infoText = (TextView) findViewById(R.id.people_info_txt);
        infoText.setText(receiver.toString());
    }
}
