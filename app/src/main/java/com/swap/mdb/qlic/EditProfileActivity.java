package com.swap.mdb.qlic;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.swap.mdb.qlic.SendActivity.types;

public class EditProfileActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        fab = (FloatingActionButton) findViewById(R.id.fab);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < SendActivity.keys.length; i++) {
            RelativeLayout tableRow = (RelativeLayout) layoutInflater.inflate(R.layout.contact_row_edit, null, false);
            ImageView imageView = (ImageView) tableRow.findViewById(R.id.imageView);
            TextView typeTextView = (TextView) tableRow.findViewById(R.id.typeTextView);
            imageView.setImageResource(ContactViewActivity.SOCIAL_ICON_IDS[i]);
            EditText handleEditText = (EditText) tableRow.findViewById(R.id.handleEditText);
            if (SendActivity.types[i].equals("Phone")) {
                handleEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            if (SendActivity.types[i].equals("Email")) {
                handleEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            }

            if (SendActivity.types[i].equalsIgnoreCase("Facebook")){
                handleEditText.setText(sharedPreferences.getString(SetupACtivity.FB_NAME,""));
            }
            else if (SendActivity.types[i].equalsIgnoreCase("Twitter")){
                handleEditText.setText(sharedPreferences.getString(SetupACtivity.TWIT_NAME, ""));
            }
            else {
                handleEditText.setText(sharedPreferences.getString(SendActivity.keys[i], ""));
            }


            typeTextView.setText(types[i]);
            linearLayout.addView(tableRow);
        }

        collapsingToolbarLayout.setTitle(sharedPreferences.getString("name", ""));
        fab.setImageResource(R.drawable.save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < SendActivity.keys.length; i++) {
                    RelativeLayout relativeLayout = (RelativeLayout) linearLayout.getChildAt(i);
                    EditText editText = (EditText) relativeLayout.findViewById(R.id.handleEditText);
                    editor.putString(SendActivity.keys[i], editText.getText().toString());
                }
                editor.apply();
                finish();
            }
        });

    }
}
