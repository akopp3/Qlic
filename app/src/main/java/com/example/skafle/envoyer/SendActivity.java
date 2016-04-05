package com.example.skafle.envoyer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import java.util.ArrayList;
import java.util.List;

public class SendActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {
    public static final String PEOPLE_KEY = "people_key";

    private SharedPreferences pref;
    private Button sendBtn;
    private GoogleApiClient mGoogleApiClient;
    private Message mDeviceInfoMessage;
    private MessageListener messageListener;
    private boolean mResolvingError = false;
    private View parentLayout;
    private Carrier carrier;
    private List<Receiver> receivers;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("ONCREATE", "EXISTS");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        parentLayout = findViewById(R.id.root_view);
        layout = (LinearLayout) findViewById(R.id.peopleHolder);
        sendBtn = (Button) findViewById(R.id.send_btn);
        carrier = new Carrier(pref.getString("name", "default"));
        receivers = new ArrayList<>();
        setCarrier();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SendActivity.this);
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Snackbar.make(parentLayout, "Sent", Snackbar.LENGTH_SHORT).show();
                                publish();
                                populateMessageListener();
                                subscribe();
                            }
                        }).setNegativeButton("Not Ready", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(parentLayout, "Not Ready", Snackbar.LENGTH_SHORT);
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_RESOLVE_ERROR) {
            if (resultCode == RESULT_OK) {
                publish();
                populateMessageListener();
                subscribe();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        unpublish();
        unsubscribe();
        mGoogleApiClient.disconnect();
    }

    private void publish() {
        Log.i("TAG", "Trying to publish.");
        // Set a simple message payload.
        Log.i("codyisdumb", carrier.toString());
        mDeviceInfoMessage = new Message(carrier.toString().getBytes());
        // Cannot proceed without a connected GoogleApiClient.
        // Reconnect and execute the pending task in onConnected().
        if (!mGoogleApiClient.isConnected()) {
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else {
            PublishOptions options = new PublishOptions.Builder()
                    .setCallback(new PublishCallback() {
                        @Override
                        public void onExpired() {
                            Log.i("TAG", "No longer publishing.");
                        }
                    }).build();

            Nearby.Messages.publish(mGoogleApiClient, mDeviceInfoMessage, options)
                    .setResultCallback(new ResultCallback<Status>() {

                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                Log.i("TAG", "Published successfully.");
                            } else {
                                Log.i("TAG", "Could not publish.");
                                // Check whether consent was given;
                                // if not, prompt the user for consent.
                                handleUnsuccessfulNearbyResult(status);
                            }
                        }
                    });
        }
    }

    private void populateMessageListener() {
        messageListener = new MessageListener() {
            @Override
            public void onFound(final Message message) {
                final String nearbyMessageString = new String(message.getContent());
                final Receiver newReceiver = new Receiver(nearbyMessageString);
                receivers.add(newReceiver);
                String name = newReceiver.getName();

                Button button = new Button(SendActivity.this);
                button.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                button.setText(name);
                layout.addView(button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Pretty inefficient, need to figure out a better way
                        Intent intent = new Intent(getApplicationContext(), ContactViewActivity.class);
                        intent.putExtra(PEOPLE_KEY, nearbyMessageString);
                        startActivity(intent);
                    }
                });

                Log.i("FOUND", nearbyMessageString);
            }

            // Called when a message is no longer detectable nearby.
            public void onLost(final Message message) {
                final String nearbyMessageString = new String(message.getContent());
                // Take appropriate action here (update UI, etc.)
            }
        };
    }

    private void unsubscribe() {
        Log.i("TAG", "Trying to unsubscribe.");
        if (messageListener != null) {
            if (mGoogleApiClient.isConnected()) {
                Nearby.Messages.unsubscribe(mGoogleApiClient, messageListener);
            }
        }
    }

    // Subscribe to receive messages.
    private void subscribe() {
        Log.i("TAG", "Trying to subscribe.");
        // Cannot proceed without a connected GoogleApiClient.
        // Reconnect and execute the pending task in onConnected().
        if (!mGoogleApiClient.isConnected()) {
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else {
            SubscribeOptions options = new SubscribeOptions.Builder()
                    .setCallback(new SubscribeCallback() {
                        @Override
                        public void onExpired() {
                            Log.i("TAG", "No longer subscribing.");
                        }
                    }).build();

            Nearby.Messages.subscribe(mGoogleApiClient, messageListener, options)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                Log.i("TAG", "Subscribed successfully.");
                            } else {
                                Log.i("TAG", "Could not subscribe.");
                                // Check whether consent was given;
                                // if not, prompt the user for consent.
                                handleUnsuccessfulNearbyResult(status);
                            }
                        }
                    });
        }
    }

    private void unpublish() {
        Log.i("TAG", "Trying to unpublish.");
        if (mDeviceInfoMessage != null) {
            if (mGoogleApiClient.isConnected()) {
                Nearby.Messages.unpublish(mGoogleApiClient, mDeviceInfoMessage);
            }
        }
    }

    private void handleUnsuccessfulNearbyResult(Status status) {
        Log.i("TAG", "Processing error, status = " + status);
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (status.hasResolution()) {
            try {
                mResolvingError = true;
                status.startResolutionForResult(SendActivity.this,
                        Constants.REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                mResolvingError = false;
                Log.i("TAG", "Failed to resolve error status.", e);
            }
        } else {
            if (status.getStatusCode() == CommonStatusCodes.NETWORK_ERROR) {
                Toast.makeText(getApplicationContext(),
                        "No connectivity, cannot proceed. Fix in 'Settings' and try again.",
                        Toast.LENGTH_LONG).show();
            } else {
                // To keep things simple, pop a toast for all other error messages.
                Toast.makeText(getApplicationContext(), "Unsuccessful: " +
                        status.getStatusMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        publish();
        subscribe();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("ConSusp", "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("ConFailed", "Connection Failed");
    }

    private void setCarrier() {
        Log.i("CARRIER", "goes here");
        Log.i("CARRIER2", MainActivity.keys.length + "");
        for (String key : MainActivity.keys) {
            if (pref.contains(key)) {
                Social newSocial = getSocial(key);
                carrier.addSocial(newSocial);
                Log.i("SOCIAL", key);
            }
        }
    }

    private Social getSocial(String key) {
        // TODO: Add functionality for other Socials

        Social soc = null;
        String mess = pref.getString(key, "");
        switch (key) {
            case "fb_name":
                soc = new FacebookClass();
                soc.setKeyInfo(mess);
                soc.activate();
                break;
            case "ig_name":
                soc = new InstagramSocial();
                soc.setKeyInfo(mess);
                soc.activate();
                break;
            case "twit_name":
                soc = new TwitterSocial();
                soc.setKeyInfo(mess);
                soc.activate();
                break;
            case "phone_name":
                soc = new PhoneNumber();
                soc.setKeyInfo(mess);
                soc.activate();
                break;
            case "contact_name":
                soc = new ContactSocial();
                soc.setKeyInfo(mess);
                soc.activate();
                break;
            case "link_name":
                soc = new LinkedinSocial();
                soc.setKeyInfo(mess);
                soc.activate();
                break;
            default:
                break;
        }

        return soc;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mGoogleApiClient.disconnect();
        this.finish();
    }
}
