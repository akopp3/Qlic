package com.swap.mdb.qlic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;



public class FacebookTesting extends Activity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    public static String name;
    public static String id;
    private Button profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_facebook_testing);
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
                                            Log.i("ID",id);

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

        profile = (Button) findViewById(R.id.button);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent facebookIntent = getOpenFacebookIntent(getApplicationContext());
                startActivity(facebookIntent);
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/app_scoped_user_id/"+ id)); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/app_scoped_user_id/"+ id)); //catches and opens a url to the desired page
        }
    }

}
