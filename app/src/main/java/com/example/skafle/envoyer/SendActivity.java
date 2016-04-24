package com.example.skafle.envoyer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.jorgecastilloprz.FABProgressCircle;
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

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

public class SendActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {
    public static final String PEOPLE_KEY = "people_key";

    private SharedPreferences pref;
    private GoogleApiClient mGoogleApiClient;
    private Message mDeviceInfoMessage;
    private MessageListener messageListener;
    private boolean mResolvingError = false;
    private View parentLayout;
    private Carrier carrier;
    private List<Receiver> receivers;
    private RelativeLayout layout;
    private LinearLayout extraHolder;
    private String key;

    FloatingActionButton person1, person2, person3, person4, person5, person6, person7, person8;

    View bottomSheet;
    BottomSheetBehavior bottomSheetBehavior;
    FloatingActionButton selectAllFAB;
    ImageView arrowImageView;
    CheckBox facebookCheckBox;
    CheckBox instagramCheckBox;
    CheckBox twitterCheckBox;
    CheckBox phoneNumberCheckBox;
    CheckBox contactInfoCheckBox;
    CheckBox linkedInCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("ONCREATE", "EXISTS");

        bottomSheet = findViewById(R.id.bottom_sheet);
        selectAllFAB = (FloatingActionButton) findViewById(R.id.selectAllFab);
        arrowImageView = (ImageView) findViewById(R.id.arrow);
        facebookCheckBox = (CheckBox) findViewById(R.id.facebookCheckBox);
        instagramCheckBox = (CheckBox) findViewById(R.id.instagramCheckBox);
        twitterCheckBox = (CheckBox) findViewById(R.id.twitterCheckBox);
        phoneNumberCheckBox = (CheckBox) findViewById(R.id.phoneNumberCheckBox);
        contactInfoCheckBox = (CheckBox) findViewById(R.id.contactInfoCheckBox);
        linkedInCheckBox = (CheckBox) findViewById(R.id.linkedInCheckBox);
        Utils.initialize();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final FABProgressCircle fabProgressCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircle);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.big_fab);
        if (floatingActionButton != null) {
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fabProgressCircle != null) {
                        fabProgressCircle.show();
                    }
                    AlertDialog.Builder alert = new AlertDialog.Builder(SendActivity.this);
                    /* builder.setMessage(R.string.dialog_message)
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
                    builder.show(); */

                    final EditText edittext = new EditText(SendActivity.this);
                    alert.setMessage(R.string.dialog_message);
                    alert.setTitle(R.string.dialog_title);

                    alert.setView(edittext);

                    alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String password = edittext.getText().toString();

                            if (!password.isEmpty()) {
                                Snackbar.make(parentLayout, "Sent", Snackbar.LENGTH_SHORT).show();
                                try {
                                    key = Utils.generateKey(Utils.salt, password);
                                    publish();
                                    populateMessageListener();
                                    subscribe();
                                } catch (NoSuchAlgorithmException|InvalidKeySpecException e) {
                                    Log.i("Error", e.toString());
                                }
                            }
                        }
                    });

                    alert.setNegativeButton("Not Ready", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Snackbar.make(parentLayout, "Not Ready", Snackbar.LENGTH_SHORT);
                        }
                    });

                    alert.show();
                }
            });
        }

        parentLayout = findViewById(R.id.root_view);
        layout = (RelativeLayout) findViewById(R.id.peopleHolder);
        carrier = new Carrier(pref.getString("name", "default"));
        extraHolder = (LinearLayout) findViewById(R.id.extra_holder);
        receivers = new ArrayList<>();
        setCarrier();

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(300);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        arrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    arrowImageView.setImageResource(R.drawable.down_arrow);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    arrowImageView.setImageResource(R.drawable.up_arrow);
                }
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    arrowImageView.setImageResource(R.drawable.up_arrow);
                    AnimationUtils.circularHide(selectAllFAB);
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED){
                    arrowImageView.setImageResource(R.drawable.down_arrow);
                    AnimationUtils.circularReveal(selectAllFAB);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                ArgbEvaluator evaluator = new ArgbEvaluator();
//                Log.i("test", "" + (Integer) evaluator.evaluate(slideOffset, R.color.colorAccent, R.color.colorPrimary));
//                bottomSheet.setBackgroundColor((Integer) evaluator.evaluate(slideOffset, R.color.colorAccent, R.color.colorPrimary));
            }
        });

        person1 = (FloatingActionButton) findViewById(R.id.person1);
        person2 = (FloatingActionButton) findViewById(R.id.person2);
        person3 = (FloatingActionButton) findViewById(R.id.person3);
        person4 = (FloatingActionButton) findViewById(R.id.person4);
        person5 = (FloatingActionButton) findViewById(R.id.person5);
        person6 = (FloatingActionButton) findViewById(R.id.person6);
        person7 = (FloatingActionButton) findViewById(R.id.person7);
        person8 = (FloatingActionButton) findViewById(R.id.person8);
    }

    /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_RESOLVE_ERROR) {
            if (resultCode == RESULT_OK) {
                publish();
                populateMessageListener();
                subscribe();
            }
        }
    } */

    @Override
    public void onStop() {
        super.onStop();
        unpublish();
        unsubscribe();
        mGoogleApiClient.disconnect();
    }

    private void publish() {
        // Set a simple message payload.
        String messageString = carrier.toString();
        String encrypted = Utils.encrypt(key, "", messageString);
        mDeviceInfoMessage = new Message(encrypted.getBytes());
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
                String messageString = new String(message.getContent());
                final String nearbyMessageString;

                try {
                    nearbyMessageString = Utils.decrypt(key, "", messageString);
                } catch (IOException e) {
                    Toast.makeText(SendActivity.this, "Passwords Don't Match", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Receiver newReceiver = new Receiver(nearbyMessageString);
                receivers.add(newReceiver);
                String name = newReceiver.getName();

                String initial = Utils.getInitial(name);

                FloatingActionButton fab = showAndGetNextFAB();
                TextDrawable textDrawable = TextDrawable.builder().buildRect(initial, Color.TRANSPARENT);
                if (fab != null) {
                    fab.setImageDrawable(textDrawable);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Pretty inefficient, need to figure out a better way
                            Intent intent = new Intent(getApplicationContext(), ContactViewActivity.class);
                            intent.putExtra(PEOPLE_KEY, nearbyMessageString);
                            startActivity(intent);
                        }
                    });

                    AnimationUtils.circularReveal(fab);
                }

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

    int numPeople = 0;
    private FloatingActionButton showAndGetNextFAB() {
        if (numPeople == 0) {
            numPeople++;
            return person1;
        } else if (numPeople == 1) {
            numPeople++;
            return person2;
        } else if (numPeople == 2) {
            numPeople++;
            return person3;
        } else if (numPeople == 3) {
            numPeople++;
            return person4;
        } else if (numPeople == 4) {
            numPeople++;
            return person5;
        } else if (numPeople == 5) {
            numPeople++;
            return person6;
        } else if (numPeople == 6) {
            numPeople++;
            return person7;
        } else if (numPeople == 7) {
            numPeople++;
            return person8;
        } else if (numPeople > 7) {
            numPeople++;
            FloatingActionButton person = new FloatingActionButton(SendActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(0, 20, 0, 20);
            person.setLayoutParams(params);

            extraHolder.addView(person);
            return person;
        }
        return null;
    }
}
