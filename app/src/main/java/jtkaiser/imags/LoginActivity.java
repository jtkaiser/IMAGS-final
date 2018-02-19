package jtkaiser.imags;

//create paitent
//create session

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//import android.R;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;

import java.util.UUID;

public class LoginActivity extends AppCompatActivity implements ConnectionStateCallback, Player.NotificationCallback {

    private static final String CLIENT_ID = "0e496f3bf31344c0aaf87a89ea883e0d";
    private static final String REDIRECT_URI = "unique://callback";
    private static final String DIALOG_HELP = "HelpDialog";

    private TextView mTitle;
    private TextView mText;
    private Button mContinueButton;
    private Button mHelpButton;
    private String userEmail;
    private Player mPlayer;
    private String mToken;
    private DatabaseHelper mDBHelper;
    private UUID mSID;
//    String start;

    // Request code that will be used to verify if the result comes from correct activity
// Can be any integer
    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTitle = (TextView) findViewById(R.id.login_title);
        mText = (TextView) findViewById(R.id.login_text);

        mContinueButton = (Button) findViewById(R.id.login_continue);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spotify.destroyPlayer(this);
                Intent i = PresessionActivity.newIntent(LoginActivity.this, mSID);
                i.putExtra(PresessionActivity.EXTRA_TOKEN, mToken);
                startActivity(i);
            }
        });

        mHelpButton = (Button) findViewById(R.id.login_help);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                HelpDialogFragment dialog = new HelpDialogFragment();
                dialog.show(manager, DIALOG_HELP);
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
                    mToken = response.getAccessToken();
            }
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("LoginActivity", "User logged in");
        userEmail = "test";
        if(whiteListCheck()){
            //database stuff
            SessionData s = SessionData.get();
            mSID = UUID.randomUUID();
            s.setID(mSID);
            s.setPID(userEmail);
            mDBHelper = new DatabaseHelper(this);
            mDBHelper.createSession(s);
            s.setStartTime(mDBHelper.getDateTime());
            Log.d("Time(startof session): ", mDBHelper.getDateTime());
            mDBHelper.closeDatabase();
            Log.d("Session: ", s.getSID().toString());
            Log.d("Session: ", s.getPID());
            //app stuff
            mTitle.setText(R.string.login_success);
            mText.setText(R.string.login_success_text);
            Toast.makeText(LoginActivity.this, "Logged in as " + userEmail, Toast.LENGTH_LONG).show();
        }
        else{
            mTitle.setText(R.string.login_bad);
            mTitle.setText(R.string.login_bad_text);
        }
    }

    @Override
    public void onLoggedOut() {
        Log.d("LoginActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("LoginActivity", "LoginActivity failed");
    }

    public void onLoginFailed(int i) {
        Log.d("LoginActivity", "LoginActivity failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("LoginActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("LoginActivity", "Received connection message: " + message);
    }

    private boolean whiteListCheck(){
        return true;
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {

    }

    @Override
    public void onPlaybackError(Error error) {

    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }
}
