package com.example.skafle.envoyer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;

public class SendActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {
    private Button sendBtn;
    private GoogleApiClient mGoogleApiClient;
    private Message mDeviceInfoMessage;
    private MessageListener messageListener;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        sendBtn = (Button) findViewById(R.id.send_btn);
        resultText = (TextView) findViewById(R.id.result_txt);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SendActivity.this);
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(SendActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                                publish();
                                populateMessageListener();
                                subscribe();
                            }
                        }).setNegativeButton("Not Ready", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SendActivity.this, "Not Ready", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        unpublish();
        unsubscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        unpublish();
        unsubscribe();
    }

    private void publish() {
        Log.i("TAG", "Trying to publish.");
        // Set a simple message payload.
        String strMsg = "HELLO!!!!";
        mDeviceInfoMessage = new Message(strMsg.getBytes());
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

                // Do something with the message string.
                Log.i("TAG", nearbyMessageString);
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
            Nearby.Messages.unsubscribe(mGoogleApiClient, messageListener);
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
            Nearby.Messages.unpublish(mGoogleApiClient, mDeviceInfoMessage);
        }
    }

    private void handleUnsuccessfulNearbyResult(Status status) {
        Log.i("TAG", "handleUnsuccessfulNearbyResult");
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("ConSusp", "Connection Suspended");
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("ConFailed", "Connection Failed");
    }
}
