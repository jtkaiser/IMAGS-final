package jtkaiser.imags;
//add to current session med status
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.UUID;

import static jtkaiser.imags.PresessionActivity.EXTRA_SID;

public class MedicationActivity extends AppCompatActivity {

    private Button mYesbutton;
    private Button mNoButton;
    private DatabaseHelper mDBHelper;
    private UUID mSID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);

        mSID = (UUID) getIntent().getSerializableExtra(EXTRA_SID);

        mYesbutton = (Button) findViewById(R.id.yes_button);
        mYesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(true);
            }
        });

        mNoButton = (Button) findViewById(R.id.no_button);
        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(false);
            }
        });
    }

    private void submit(Boolean medStatus){
        Intent i = PostsessionActivity.newIntent(MedicationActivity.this, mSID);
        startActivity(i);
    }

    public static Intent newIntent(Context context, UUID SID) {
        Intent i = new Intent(context, MedicationActivity.class);
        i.putExtra(EXTRA_SID, SID);
        return i;
    }
}
