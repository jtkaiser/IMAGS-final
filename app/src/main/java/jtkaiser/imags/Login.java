package jtkaiser.imags;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.LoginActivity;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class Login extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{

    private static final String CLIENT_ID = "0e496f3bf31344c0aaf87a89ea883e0d";
    private static final String REDIRECT_URI = "unique://callback";

    private Player mPlayer;
    private TextView mTitle;
    private TextView mText;
    private Button mContinueButton;
    private Button mHelpButton;
    private Button mPlayToggle;
    private Button mYesbutton;
    private Button mNoButton;
    private Button mLogoutButton;
    private String userEmail;
    private Boolean isPlaying = true;
    private Integer stage;
    private Boolean medStatus;

    // Request code that will be used to verify if the result comes from correct activity
// Can be any integer
    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        stage = 0;
        mTitle = (TextView) findViewById(R.id.login_title);
        mText = (TextView) findViewById(R.id.login_text);

        mContinueButton = (Button) findViewById(R.id.login_continue);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceStage();
                }
            });

        mHelpButton = (Button) findViewById(R.id.login_help);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mPlayToggle = (Button) findViewById(R.id.play_toggle);
        mPlayToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    pausePlayer();
                }
                else{
                    resumePlayer();
                }
            }
        });

        mYesbutton = (Button) findViewById(R.id.yes_button);
        mYesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medStatus = true;
                advanceStage();
            }
        });

        mNoButton = (Button) findViewById(R.id.no_button);
        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medStatus = false;
                advanceStage();
            }
        });

        mLogoutButton = (Button) findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(Login.this);
                        mPlayer.addNotificationCallback(Login.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("Login", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("Login", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("Login", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("Login", "User logged in");
        userEmail = "test";
        if(whiteListCheck()){
            mTitle.setText(R.string.login_success);
            mText.setText(R.string.login_success_text);
            mContinueButton.setVisibility(View.VISIBLE);
            Toast.makeText(Login.this, "Logged in as " + userEmail, Toast.LENGTH_LONG).show();
        }
        else{
            mTitle.setText(R.string.login_bad);
            mTitle.setText(R.string.login_bad_text);
        }
        mTitle.setVisibility(View.VISIBLE);
        mText.setVisibility(View.VISIBLE);
        mHelpButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoggedOut() {
        Log.d("Login", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("Login", "Login failed");
    }

    public void onLoginFailed(int i) {
        Log.d("Login", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("Login", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("Login", "Received connection message: " + message);
    }

    private boolean whiteListCheck(){
        return true;
    }

    private void pausePlayer(){
        isPlaying = false;
        mPlayToggle.setText(R.string.play_button);
    }

    private void resumePlayer(){
        isPlaying = true;
        mPlayToggle.setText(R.string.pause_button);
    }

    private void advanceStage() {
        // logged in
        if (stage == 0) {
            mTitle.setText(R.string.presession_title);
            mText.setText(R.string.presession_text);
        }

        // presession
        if (stage == 1) {
            mTitle.setText(R.string.search_title);
            mText.setVisibility(View.INVISIBLE);
        }

        // search screen
        if (stage == 2){
            mTitle.setText(R.string.session_title);
            mPlayToggle.setVisibility(View.VISIBLE);
        }

        // session screen
        if (stage == 3) {
            mPlayToggle.setVisibility(View.INVISIBLE);
            mContinueButton.setVisibility(View.INVISIBLE);
            mTitle.setText(R.string.medication_title);
            mText.setVisibility(View.VISIBLE);
            mText.setText(R.string.medication_text);
            mYesbutton.setVisibility(View.VISIBLE);
            mNoButton.setVisibility(View.VISIBLE);
        }

        //medication screen
        if (stage == 4) {
            mTitle.setText(R.string.postsession_title);
            mText.setText(R.string.postsession_text);
            mYesbutton.setVisibility(View.INVISIBLE);
            mNoButton.setVisibility(View.INVISIBLE);
            mLogoutButton.setVisibility(View.VISIBLE);
        }
        stage++;
    }
}
