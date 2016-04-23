package com.example.skafle.envoyer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        fab = (FloatingActionButton) findViewById(R.id.fab);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < SetupACtivity.keys.length; i++) {
            RelativeLayout tableRow = (RelativeLayout) layoutInflater.inflate(R.layout.contact_row_edit, null, false);
            ImageView imageView = (ImageView) tableRow.findViewById(R.id.imageView);
            EditText handleEditText = (EditText) tableRow.findViewById(R.id.handleEditText);
            TextView typeTextView = (TextView) tableRow.findViewById(R.id.typeTextView);
            imageView.setImageResource(ContactViewActivity.SOCIAL_ICON_IDS[i]);
            handleEditText.setText(sharedPreferences.getString(SetupACtivity.keys[i], ""));
            typeTextView.setText(SetupACtivity.types[i]);
            linearLayout.addView(tableRow);
        }

        fab.setImageResource(R.drawable.save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < MainActivity.keys.length; i++) {
                    RelativeLayout relativeLayout = (RelativeLayout) linearLayout.getChildAt(i);
                    EditText editText = (EditText) relativeLayout.findViewById(R.id.handleEditText);
                    editor.putString(MainActivity.keys[i], editText.getText().toString());
                }
                editor.commit();
                finish();
            }
        });

    }
}
