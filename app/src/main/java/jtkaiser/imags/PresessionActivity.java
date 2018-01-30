package jtkaiser.imags;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class PresessionActivity extends AppCompatActivity {

    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private Button mContinueButton;
    private Button mHelpButton;
    private String mToken;
    private SeekBar mSeekBar;
    private PainTracker mPainTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presession);

        mPainTracker = PainTracker.get();

        mToken = getIntent().getStringExtra(EXTRA_TOKEN);

        mContinueButton = (Button) findViewById(R.id.presession_continue);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = SearchActivity.createIntent(PresessionActivity.this);
                i.putExtra(SearchActivity.EXTRA_TOKEN, mToken);
                startActivity(i);
            }
        });

        mSeekBar = (SeekBar) findViewById(R.id.presession_seekbar);
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
              mContinueButton.setVisibility(View.VISIBLE);
            }
        });
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, PresessionActivity.class);
    }
}
