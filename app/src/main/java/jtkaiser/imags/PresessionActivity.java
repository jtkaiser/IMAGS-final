package jtkaiser.imags;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.UUID;

public class PresessionActivity extends AppCompatActivity {

    private static final String DIALOG_HELP = "HelpDialog";

    static final String EXTRA_TOKEN = "imags.token";
    static final String EXTRA_SID = "imags.sid";
    private Button mContinueButton;
    private Button mHelpButton;
    private String mToken;
    private String mSID;
    private SeekBar mSeekBar;
    private PainTracker mPainTracker;
    private ImageView mFacesScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presession);

        mPainTracker = PainTracker.get(this);

        mSID = getIntent().getStringExtra(EXTRA_SID);

        mToken = getIntent().getStringExtra(EXTRA_TOKEN);

        mContinueButton = (Button) findViewById(R.id.presession_continue);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = SearchActivity.newIntent(PresessionActivity.this, mSID);
                i.putExtra(SearchActivity.EXTRA_TOKEN, mToken);
                startActivity(i);
            }
        });

        mSeekBar = (SeekBar) findViewById(R.id.presession_seekbar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar mSeekBar, int progress, boolean fromUser) {
                mPainTracker.setValue(progress);
                mContinueButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mContinueButton.setVisibility(View.VISIBLE);
            }
        });

        mFacesScale = (ImageView) findViewById(R.id.presession_faces);
        mFacesScale.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    processTouch(event.getX());
                }
                return true;
            }
        });

        mHelpButton = (Button) findViewById(R.id.presession_help);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                HelpDialogFragment dialog = new HelpDialogFragment();
                dialog.show(manager, DIALOG_HELP);
            }
        });
    }

    public static Intent newIntent(Context context, String SID) {
        Intent i = new Intent(context, PresessionActivity.class);
        i.putExtra(EXTRA_SID, SID);
        return i;
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
}
