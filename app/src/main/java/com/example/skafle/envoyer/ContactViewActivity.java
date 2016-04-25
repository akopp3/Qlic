package com.example.skafle.envoyer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContactViewActivity extends AppCompatActivity {

    public static final int[] SOCIAL_ICON_IDS = {R.drawable.facebook, R.drawable.instagram,
            R.drawable.twitter, R.drawable.phone, R.drawable.email, R.drawable.linkedin};

    LinearLayout linearLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab;
    String info;
    Receiver receiver;

    Intent intent;
    public static SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        linearLayout.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        intent = getIntent();
        if (intent.hasExtra(SendActivity.PEOPLE_KEY)) {
            info = intent.getStringExtra(SendActivity.PEOPLE_KEY);
            receiver = new Receiver(info);
            String name = receiver.getName();
            collapsingToolbarLayout.setTitle(name);
            for (String typeVar : MainActivity.types) {
                Social social = receiver.getSocial(typeVar);
                if (social != null) {
                    Log.i("test", "added");
                    final RelativeLayout tableRow = (RelativeLayout) layoutInflater.inflate(R.layout.contact_view_row2, null, false);
                    ImageView imageView = (ImageView) tableRow.findViewById(R.id.imageView);
                    Button signin = (Button) tableRow.findViewById(R.id.signIn);
                    if (typeVar.equalsIgnoreCase("Facebook")) {
                        signin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent facebookIntent = getOpenFacebookIntent(getApplicationContext());
                                startActivity(facebookIntent);
                            }
                        });
                    }
                    else if (typeVar.equalsIgnoreCase("Instagram"))  {
                        signin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent lit = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://www.Instagram.com/" + sharedPreferences.getString(MainActivity.keys[1], "")));
                                startActivity(lit);
                            }
                        });
                    }
                    else if (typeVar.equalsIgnoreCase("Twitter"))  {
                        signin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent lit1 = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://www.Twitter.com/" + sharedPreferences.getString(MainActivity.keys[2], "")));
                                startActivity(lit1);
                            }
                        });
                    }

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
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            for (int i = 0; i < MainActivity.keys.length; i++) {
                if (!sharedPreferences.getString(MainActivity.keys[i], "").equals("")) {
                    RelativeLayout tableRow = (RelativeLayout) layoutInflater.inflate(R.layout.contact_view_row, null, false);
                    ImageView imageView = (ImageView) tableRow.findViewById(R.id.imageView);
                    TextView handleTextView = (TextView) tableRow.findViewById(R.id.handleTextView);
                    TextView typeTextView = (TextView) tableRow.findViewById(R.id.typeTextView);
                    CheckBox checkBox = (CheckBox) tableRow.findViewById(R.id.checkBox);
                    imageView.setImageResource(SOCIAL_ICON_IDS[i]);
                    handleTextView.setText(sharedPreferences.getString(MainActivity.keys[i], ""));
                    typeTextView.setText(MainActivity.types[i]);
                    checkBox.setChecked(sharedPreferences.getBoolean(MainActivity.enabledKeys[i], false));
                    linearLayout.addView(tableRow);
                }
            }
            collapsingToolbarLayout.setTitle(sharedPreferences.getString("name", ""));
            fab.setImageResource(R.drawable.edit);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!intent.hasExtra(SendActivity.PEOPLE_KEY)) {
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                RelativeLayout view = (RelativeLayout) linearLayout.getChildAt(i);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                editor.putBoolean(MainActivity.enabledKeys[i], checkBox.isChecked());
            }
            editor.apply();
        }
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/app_scoped_user_id/" + sharedPreferences.getString(MainActivity.keys[0], ""))); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/app_scoped_user_id/" + sharedPreferences.getString(MainActivity.keys[0], ""))); //catches and opens a url to the desired page
        }
    }


}

