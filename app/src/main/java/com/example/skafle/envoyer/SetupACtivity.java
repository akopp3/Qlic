package com.example.skafle.envoyer;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import com.redbooth.WelcomeCoordinatorLayout;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.redbooth.WelcomeCoordinatorLayout;

import java.util.zip.Inflater;

public class SetupACtivity extends AppCompatActivity {
    public static final String[] keys = {"name_name", "fb_name", "ig_name", "twit_name", "phone_name", "contact_name", "link_name"};
    public static final String[] types = {"Facebook", "Instagram", "Twitter", "Phone", "Contact Info", "LinkedIn"};
    private boolean animationReady = false;
    private ValueAnimator backgroundAnimator;
    private WelcomeCoordinatorLayout coordinatorLayout;
    private Button skip;
    private EditText fbEdit;
    private EditText instaEdit;
    private EditText twitEdit;
    private EditText phoneEdit;
    private EditText contactEdit;
    private EditText linkEdit;
    private EditText nameText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_activity);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.setup_page_2, null);

        nameText = (EditText) view.findViewById(R.id.name_txt);
        fbEdit = (EditText) view.findViewById(R.id.fb_input);
        instaEdit = (EditText) view.findViewById(R.id.inst_input);
        twitEdit = (EditText) view.findViewById(R.id.twit_input);
        phoneEdit = (EditText) view.findViewById(R.id.phone_input);
        contactEdit = (EditText) view.findViewById(R.id.cont_input);
        linkEdit = (EditText) view.findViewById(R.id.link_input);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.contains("tutorial")){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        } else{
            editor.putBoolean("tutorial", false);
        }
        editor.apply();
        coordinatorLayout = (WelcomeCoordinatorLayout) findViewById(R.id.coordinator);
        skip = (Button) findViewById(R.id.skip);
        coordinatorLayout.setOnPageScrollListener(new WelcomeCoordinatorLayout.OnPageScrollListener() {
            @Override
            public void onScrollPage(View v, float progress, float maximum) {
                if (!animationReady) {
                    animationReady = true;
                    backgroundAnimator.setDuration((long) maximum);
                }
                backgroundAnimator.setCurrentPlayTime((long) progress);
            }

            @Override
            public void onPageSelected(View v, int pageSelected) {
                switch (pageSelected) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coordinatorLayout.getPageSelected() == coordinatorLayout.getNumOfPages() - 1) {
                    editor.putBoolean("tutorial", true);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                } else {
                    coordinatorLayout.setCurrentPage(coordinatorLayout.getPageSelected() + 1, true);
                }
                if(coordinatorLayout.getPageSelected() == 2) {
                    final EditText[] editViews = {nameText, fbEdit, instaEdit, twitEdit, phoneEdit, contactEdit, linkEdit};

                    for (int i = 0; i < editViews.length; i++) {
                        String key = keys[i];
                        final int j = i;
                        editViews[j].setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                                if (id == EditorInfo.IME_ACTION_DONE) {
                                    return true;
                                }
                                return false;
                            }
                        });
                        editor.putString(key, editViews[i].getText().toString());
                    }

                    editor.apply();
                    Snackbar.make(findViewById(android.R.id.content), "Saved", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
        coordinatorLayout.addPage(R.layout.setup_page_1, R.layout.setup_page_2, R.layout.setup_page_3);
        initializeBackgroundTransitions();
    }

    @Override
    public void onBackPressed() {
        if(coordinatorLayout.getPageSelected()!=0){
            coordinatorLayout.setCurrentPage(coordinatorLayout.getPageSelected() - 1, true);
        }
    }
    private void initializeBackgroundTransitions() {
        final Resources resources = getResources();

        final int colorPage1 = ResourcesCompat.getColor(resources, R.color.setup1, getTheme());
        final int colorPage2 = ResourcesCompat.getColor(resources, R.color.setup2, getTheme());
        final int colorPage3 = ResourcesCompat.getColor(resources, R.color.setup3, getTheme());

        backgroundAnimator = ValueAnimator
                .ofObject(new ArgbEvaluator(), colorPage1, colorPage2, colorPage3);
        backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                coordinatorLayout.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });


    }


}
