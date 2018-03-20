package jtkaiser.imags;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;
import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

import static jtkaiser.imags.PresessionActivity.EXTRA_SID;

public class SessionActivity extends AppCompatActivity implements SpotifyPlayer.NotificationCallback, ConnectionStateCallback {

    private Button mPlayToggle;
    private Button mFinishButton;
    private Button mHelpButton;
    private Button mNewSongButton;
    private Player mPlayer;
    private SongDataManager mSongDataManager;

    private TextView mTrackTitle;
    private TextView mTrackArtist;
    private TextView mTrackAlbum;

    private ImageView mTrackImage;
    private SeekBar mSeekBar;
    private ImageView mFacesScale;
    private PainTracker mPainTracker;
    private String mToken;
    private SpotifyService mService;

    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "0e496f3bf31344c0aaf87a89ea883e0d";
    private static final String REDIRECT_URI = "unique://callback";
    private static final String DIALOG_HELP = "HelpDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        if(mPlayer != null){
            Spotify.destroyPlayer(mPlayer);
        }

        mService = new SpotifyApi().setAccessToken(mToken).getService();
        mSongDataManager = SongDataManager.get(this, mService);
        mPainTracker = PainTracker.get(this);

        mSeekBar = (SeekBar) findViewById(R.id.session_seekbar);
        mSeekBar.setProgress(mPainTracker.getLastValue());


        mPlayToggle = (Button) findViewById(R.id.play_toggle);
        mPlayToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer.getPlaybackState().isPlaying) {
                    pausePlayer();
                } else if (mPlayer.getPlaybackState().positionMs != 0) {
                    resumePlayer();
                } else {
                    startPlayer();
                }
            }
        });

        mFinishButton = (Button) findViewById(R.id.session_finish);
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausePlayer();
                Spotify.destroyPlayer(mPlayer);

                Intent i = MedicationActivity.newIntent(SessionActivity.this);
                startActivity(i);
            }
        });

        mNewSongButton = (Button) findViewById(R.id.new_song_button);
        mNewSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausePlayer();

                mSongDataManager.setNewTrack();

                Intent i = SearchActivity.newIntent(SessionActivity.this, mToken);
                startActivity(i);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar mSeekBar, int progress, boolean fromUser) {
                mPainTracker.setValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mFacesScale = (ImageView) findViewById(R.id.session_faces);
        mFacesScale.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    processTouch(event.getX());
                }
                return true;
            }
        });

        mHelpButton = (Button) findViewById(R.id.session_help);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                HelpDialogFragment dialog = new HelpDialogFragment();
                dialog.show(manager, DIALOG_HELP);
            }
        });

        setInfoDisplay();



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
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(SessionActivity.this);
                        mPlayer.addNotificationCallback(SessionActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("LoginActivity", "Could not initialize player: " + throwable.getMessage());
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
        Log.d("LoginActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("LoginActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("LoginActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("LoginActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
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

    private void pausePlayer() {
        mPlayToggle.setText(R.string.play_button);
        mPlayer.pause(null);
    }

    private void resumePlayer() {

        if(mSongDataManager.isNewTrack()) {
            startPlayer();
        }


        mPlayToggle.setText(R.string.pause_button);
        mPlayer.resume(null);
    }

    private void startPlayer() {
        mPlayToggle.setText(R.string.pause_button);
        mPlayer.playUri(null, mSongDataManager.getUri(), 0, 0);
    }

    private void setInfoDisplay() {
        mTrackTitle = (TextView) findViewById(R.id.track_title);
        mTrackTitle.setText(mSongDataManager.getName());

        mTrackArtist = (TextView) findViewById(R.id.track_artist);
        mTrackArtist.setText(mSongDataManager.getArtistNames());

        mTrackAlbum = (TextView) findViewById(R.id.track_album);
        mTrackAlbum.setText(mSongDataManager.getAlbumName());

        mTrackImage = (ImageView) findViewById(R.id.track_image);
        Picasso.with(this).load(mSongDataManager.getImageUrl()).into(mTrackImage);
    }

    private void processTouch(float xPos){
        double innerXPos = (xPos - mFacesScale.getLeft());
        Double relativePos = Math.floor((innerXPos * 6) / mFacesScale.getWidth());
        int progressVal = 2 * relativePos.intValue();
        if(progressVal > 10){
            progressVal = 10;
        }
        mSeekBar.setProgress(progressVal);
    }

    public static Intent newIntent(Context context, String SID) {
        Intent i = new Intent(context, SessionActivity.class);
        i.putExtra(EXTRA_SID, SID);
        return i;
    }

}
