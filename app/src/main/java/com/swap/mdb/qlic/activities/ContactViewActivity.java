package com.swap.mdb.qlic.activities;

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

import com.swap.mdb.qlic.R;
import com.swap.mdb.qlic.social.Social;
import com.swap.mdb.qlic.transfer.Receiver;

public class ContactViewActivity extends AppCompatActivity {

    public static final int[] SOCIAL_ICON_IDS = {R.drawable.facebook, R.drawable.instagram,
            R.drawable.twitter, R.drawable.phone, R.drawable.email, R.drawable.linkedin};
    public static SharedPreferences sharedPreferences;
    LinearLayout linearLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab;
    String info;
    Receiver receiver;
    Intent intent;

    public static Intent getOpenFacebookIntent(Context context, Social soc) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://facewebmodal/f?href=" + soc.keyInfo())); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse(soc.keyInfo())); //catches and opens a url to the desired page
        }
    }

    public static Intent getOpenIGIntent(Context context, Social soc) {
        Intent intent;
        try {
            context.getPackageManager().getPackageInfo("com.instagram.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("instagram://user?username=" + soc.keyInfo()));

        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/" + soc.keyInfo()));
        }

        return intent;
    }

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
            for (int i = 0; i < SendActivity.types.length; i++) {
                String typeVar = SendActivity.types[i];
                final Social social = receiver.getSocial(typeVar);
                if (social != null) {
                    Log.i("test", "added");
                    //final RelativeLayout tableRow = (RelativeLayout) layoutInflater.inflate(R.layout.contact_view_row2, null, false);
                    //ImageView imageView = (ImageView) tableRow.findViewById(R.id.imageView);
                    //Button signin = (Button) tableRow.findViewById(R.id.signIn);
                    if (typeVar.equalsIgnoreCase("Facebook")) {
                        final LinearLayout tableRow = (LinearLayout) layoutInflater.inflate(R.layout.contact_view_row2, null, false);
                        ImageView imageView = (ImageView) tableRow.findViewById(R.id.imageView);
                        Button signin = (Button) tableRow.findViewById(R.id.signIn);
                        signin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent facebookIntent = getOpenFacebookIntent(getApplicationContext(), social);
                                    startActivity(facebookIntent);
                                } catch (Exception e) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW,
                                            Uri.parse(social.keyInfo()));
                                    startActivity(intent);
                                }
                            }
                        });
                        imageView.setImageResource(SOCIAL_ICON_IDS[i]);
                        linearLayout.addView(tableRow);
                    } else if (typeVar.equalsIgnoreCase("Instagram")) {
                        final LinearLayout tableRow = (LinearLayout) layoutInflater.inflate(R.layout.contact_view_row2, null, false);
                        ImageView imageView = (ImageView) tableRow.findViewById(R.id.imageView);
                        Button signin = (Button) tableRow.findViewById(R.id.signIn);
                        signin.setText("Open Instagram Profile");
                        signin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent lit = getOpenIGIntent(getApplicationContext(), social);
                                    startActivity(lit);
                                } catch (Exception e) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/" + social.keyInfo()));
                                    startActivity(intent);
                                }

                            }
                        });
                        imageView.setImageResource(SOCIAL_ICON_IDS[i]);
                        linearLayout.addView(tableRow);
                    } else if (typeVar.equalsIgnoreCase("Twitter")) {
                        final LinearLayout tableRow = (LinearLayout) layoutInflater.inflate(R.layout.contact_view_row2, null, false);
                        ImageView imageView = (ImageView) tableRow.findViewById(R.id.imageView);
                        Button signin = (Button) tableRow.findViewById(R.id.signIn);
                        signin.setText("Follow on Twitter");
                        signin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent lit1 = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(social.keyInfo()));
                                startActivity(lit1);
                            }
                        });
                        imageView.setImageResource(SOCIAL_ICON_IDS[i]);
                        linearLayout.addView(tableRow);
                    } else {
                        final RelativeLayout tableRow = (RelativeLayout) layoutInflater.inflate(R.layout.contact_view_row, null, false);
                        ImageView imageView = (ImageView) tableRow.findViewById(R.id.imageView);
                        TextView textView = (TextView) tableRow.findViewById(R.id.handleTextView);
                        TextView textView2 = (TextView) tableRow.findViewById(R.id.typeTextView);
                        textView2.setVisibility(View.GONE);
                        CheckBox checkBox = (CheckBox) tableRow.findViewById(R.id.checkBox);
                        checkBox.setVisibility(View.GONE);

                        textView.setText(social.keyInfo());
                        imageView.setImageResource(SOCIAL_ICON_IDS[i]);
                        linearLayout.addView(tableRow);

                    }


                    //imageView.setImageResource(SOCIAL_ICON_IDS[i]);
                    //linearLayout.addView(tableRow);
                }
            }

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent saveContactsIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    saveContactsIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                    saveContactsIntent.putExtra(ContactsContract.Intents.Insert.NAME, receiver.getName());

                    Social email = receiver.getSocial(SendActivity.types[4]);
                    if (email != null) {
                        saveContactsIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, email.keyInfo());
                    }

                    Social phoneNumber = receiver.getSocial(SendActivity.types[3]);
                    if (email != null) {
                        saveContactsIntent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber.keyInfo());
                    }

                    startActivity(saveContactsIntent);
                }
            });
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            for (int i = 0; i < SendActivity.keys.length; i++) {
                if (!sharedPreferences.getString(SendActivity.keys[i], "").equals("")) {
                    RelativeLayout tableRow = (RelativeLayout) layoutInflater.inflate(R.layout.contact_view_row, null, false);
                    ImageView imageView = (ImageView) tableRow.findViewById(R.id.imageView);
                    TextView handleTextView = (TextView) tableRow.findViewById(R.id.handleTextView);
                    TextView typeTextView = (TextView) tableRow.findViewById(R.id.typeTextView);
                    final CheckBox checkBox = (CheckBox) tableRow.findViewById(R.id.checkBox);
                    imageView.setImageResource(SOCIAL_ICON_IDS[i]);
                    if (SendActivity.types[i].equalsIgnoreCase("Facebook")) {
                        handleTextView.setText(sharedPreferences.getString(LoaderActivity.FB_NAME, ""));
                    } else if (SendActivity.types[i].equalsIgnoreCase("Twitter")) {
                        handleTextView.setText(sharedPreferences.getString(LoaderActivity.TWIT_NAME, ""));
                    } else {
                        handleTextView.setText(sharedPreferences.getString(SendActivity.keys[i], ""));
                    }
                    typeTextView.setText(SendActivity.types[i]);
                    checkBox.setChecked(sharedPreferences.getBoolean(SendActivity.enabledKeys[i], false));
                    tableRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkBox.setChecked(!checkBox.isChecked());
                        }
                    });
                    tableRow.setTag(SendActivity.enabledKeys[i]);
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
                editor.putBoolean((String) view.getTag(), checkBox.isChecked());
            }
            editor.apply();
        }
    }


}

