package com.example.skafle.envoyer;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.redbooth.WelcomeCoordinatorLayout;
import com.facebook.FacebookSdk;

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
import android.widget.Toast;

import com.redbooth.WelcomeCoordinatorLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

public class SetupACtivity extends AppCompatActivity {
    public static final String[] keys = {"name", "fb_name", "ig_name", "twit_name", "phone_name", "contact_name", "link_name"};
    public static final String[] types = {"Name", "Facebook", "Instagram", "Twitter", "Phone", "Contact Info", "LinkedIn"};
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
    private CallbackManager callbackManager;
    public static String name, id;
    private LoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_setup_activity);
//        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.setup_page_2, null);

//        nameText = (EditText) view.findViewById(R.id.name_txt);
//        fbEdit = (EditText) view.findViewById(R.id.fb_input);
//        instaEdit = (EditText) view.findViewById(R.id.inst_input);
//        twitEdit = (EditText) view.findViewById(R.id.twit_input);
//        phoneEdit = (EditText) view.findViewById(R.id.phone_input);
//        contactEdit = (EditText) view.findViewById(R.id.cont_input);
//        linkEdit = (EditText) view.findViewById(R.id.link_input);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.contains("tutorial")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
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
                        nameText = (EditText) v.findViewById(R.id.name_txt);
                        //fbEdit = (EditText) v.findViewById(R.id.fb_input);
                        instaEdit = (EditText) v.findViewById(R.id.inst_input);
                        //twitEdit = (EditText) v.findViewById(R.id.twit_input);
                        phoneEdit = (EditText) v.findViewById(R.id.phone_input);
                        contactEdit = (EditText) v.findViewById(R.id.cont_input);
                        linkEdit = (EditText) v.findViewById(R.id.link_input);

                        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
                        loginButton = (LoginButton) findViewById(R.id.fb_login);
                        List<String> permissionList = Arrays.asList("user_friends", "public_profile");
                        loginButton.setReadPermissions(permissionList);
                        callbackManager = CallbackManager.Factory.create();

                        loginButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                                    @Override
                                    public void onSuccess(LoginResult loginResult) {
                                        Log.v("FacebookLogin", "SUCCESS");
                                        GraphRequest request = GraphRequest.newMeRequest(
                                                loginResult.getAccessToken(),
                                                new GraphRequest.GraphJSONObjectCallback() {
                                                    @Override
                                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                                        Log.v("LoginActivity", response.toString());
                                                        try {
                                                            name = object.getString("name");
                                                            id = object.getString("id");
                                                            //List <String> = object.getJSONArray()
                                                            Toast.makeText(getApplicationContext(), "Facebook Login Successful", Toast.LENGTH_LONG).show();
                                                            Log.i("NAME", name);

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                        Bundle parameters = new Bundle();
                                        parameters.putString("fields", "id,name,email,gender,birthday");
                                        request.setParameters(parameters);
                                        request.executeAsync();
                                    }

                                    @Override
                                    public void onCancel() {
                                        Log.v("FacebookLogin", "LOGIN CANCEL");
                                    }

                                    @Override
                                    public void onError(FacebookException exception) {
                                        Log.v("FacebookLoginError", exception.getCause().toString());
                                    }
                                });
                            }
                        });
                        break;
                    case 2:
                        if (nameText.getText().toString().isEmpty()) {
                            coordinatorLayout.setCurrentPage(coordinatorLayout.getPageSelected() - 1, true);
                            AlertDialog.Builder alert = new AlertDialog.Builder(SetupACtivity.this);
                            alert.setTitle(R.string.name_error_title);
                            alert.setMessage(R.string.name_error_txt);

                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            });

                            alert.show();
                        }
                        break;
                    case 3:
                        break;
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numPage = coordinatorLayout.getPageSelected();
                int totalPages = coordinatorLayout.getNumOfPages() - 1;
                if (numPage == totalPages) {
                    editor.putBoolean("tutorial", true);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    coordinatorLayout.setCurrentPage(coordinatorLayout.getPageSelected() + 1, true);
                }

                //coordinatorLayout.setCurrentPage(coordinatorLayout.getPageSelected() + 2, true);
                if (coordinatorLayout.getPageSelected() == 2) {
                    final EditText[] editViews = {nameText, instaEdit, phoneEdit, contactEdit, linkEdit};
                    String fbKey = keys[1];
                    String twitKey = keys[3];
                    editor.putString(fbKey, id);
                    System.out.println(id);
                    editor.putString(twitKey, id);
                    System.out.println(id);

                    nameText.setError("You must put a Name in");

                    int j = 0;
                    for (int i = 0; i < keys.length; i++) {

                        if (i != 1 && i != 3) {
                            String key = keys[i];
                            editViews[j].setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                @Override
                                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                                    if (id == EditorInfo.IME_ACTION_DONE) {
                                        return true;
                                    }
                                    return false;
                                }
                            });

                            String text = editViews[j].getText().toString();

                            editor.putString(key, text);
                            System.out.println(editViews[j].getText().toString());
                            j++;
                        }
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
        if (coordinatorLayout.getPageSelected() != 0) {
            coordinatorLayout.setCurrentPage(coordinatorLayout.getPageSelected() - 1, true);
        } else {
            coordinatorLayout.setCurrentPage(0, true);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
