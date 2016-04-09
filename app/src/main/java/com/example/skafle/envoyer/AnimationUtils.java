package com.example.skafle.envoyer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by aneeshjindal on 4/8/16.
 */
public class AnimationUtils {

    public static void circularReveal(final View view) {
        if (Build.VERSION.SDK_INT >= 21) {

            int cx = view.getWidth() / 2;
            int cy = view.getHeight() / 2;

            int finalRadius = Math.max(view.getWidth(), view.getHeight());

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

            view.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void circularReveal(final View view, int X, int Y, AnimatorListenerAdapter listener) {
        if (Build.VERSION.SDK_INT >= 21) {

//            int cx = view.getWidth() / 2;
//            int cy = view.getHeight() / 2;

            int finalRadius = Math.max(view.getWidth(), view.getHeight());

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, X, Y, 0, finalRadius);

            view.setVisibility(View.VISIBLE);
            if (listener != null) {
                anim.addListener(listener);
            }
            anim.start();
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }
}
