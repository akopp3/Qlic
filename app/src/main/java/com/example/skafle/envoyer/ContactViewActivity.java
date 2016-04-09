package com.example.skafle.envoyer;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContactViewActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    FloatingActionButton fab;
    String info;
    Receiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);


        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());

        fab = (FloatingActionButton) findViewById(R.id.fab);

        Intent intent = getIntent();
        if (intent.hasExtra(SendActivity.PEOPLE_KEY)) {
            info = intent.getStringExtra(SendActivity.PEOPLE_KEY);
            receiver = new Receiver(info);
            String name = receiver.getName();
            //getSupportActionBar().setTitle(name);
            for (String typeVar : MainActivity.types) {
                Social social = receiver.getSocial(typeVar);
                if (social != null) {
                    Log.i("test", "added");
                    String type = social.type();
                    String handle = social.keyInfo();
                    final RelativeLayout tableRow = (RelativeLayout) layoutInflater.inflate(R.layout.contact_view_row, null, false);
                    ImageView imageView = (ImageView) tableRow.findViewById(R.id.imageView);
                    TextView handleTextView = (TextView) tableRow.findViewById(R.id.handleTextView);
                    TextView typeTextView = (TextView) tableRow.findViewById(R.id.typeTextView);
                    handleTextView.setText(handle);
                    typeTextView.setText(type);
                    linearLayout.addView(tableRow);
                }
            }

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent saveContactsIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    saveContactsIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                    saveContactsIntent.putExtra(ContactsContract.Intents.Insert.NAME, receiver.getName());

                    Social email = receiver.getSocial(MainActivity.types[4]);
                    if (email != null) {
                        saveContactsIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, email.keyInfo());
                    }

                    Social phoneNumber = receiver.getSocial(MainActivity.types[3]);
                    if (email != null) {
                        saveContactsIntent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber.keyInfo());
                    }

                    startActivity(saveContactsIntent);
                }
            });
        }

    }
}

