package com.example.skafle.envoyer;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

import com.redbooth.WelcomeCoordinatorLayout;

public class SetupActivity extends AppCompatActivity {
    private boolean animationReady = false;
    private ValueAnimator backgroundAnimator;
    private WelcomeCoordinatorLayout coordinatorLayout;
    private Button skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_activity);

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
                if(coordinatorLayout.getPageSelected()== coordinatorLayout.getNumOfPages()-1){
                    finish();
                }else{
                coordinatorLayout.setCurrentPage(coordinatorLayout.getPageSelected()+1, true);}
            }
        });
        coordinatorLayout.addPage(R.layout.setup_page_1, R.layout.setup_page_2, R.layout.setup_page_3);
        initializeBackgroundTransitions();
    }



    private void initializeBackgroundTransitions() {
        final Resources resources = getResources();
        ResourcesCompat compat = new ResourcesCompat();
        final int colorPage1 = compat.getColor(resources, R.color.setup1, getTheme());
        final int colorPage2 = compat.getColor(resources, R.color.setup2, getTheme());
        final int colorPage3 = compat.getColor(resources, R.color.setup3, getTheme());

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
