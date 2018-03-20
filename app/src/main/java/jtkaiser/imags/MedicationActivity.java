package jtkaiser.imags;
//add to current session med status
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import jtkaiser.imags.database.DataManager;
import jtkaiser.imags.database.DatabaseHelper;

import static jtkaiser.imags.PresessionActivity.EXTRA_SID;

public class MedicationActivity extends AppCompatActivity {

    private Button mYesButton;
    private Button mNoButton;
    private Button mSubmitButton;
    private TextView mText;
    private EditText mEditText;
    private String mSID;
    private boolean mTookMed;
    private String mName;
    private String mDosage;
    private int mQuestionNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);

        mQuestionNum = 1;

        mSID = getIntent().getStringExtra(EXTRA_SID);

        mText = (TextView) findViewById(R.id.medication_text);

        mEditText = (EditText) findViewById(R.id.answer_field);

        mSubmitButton = (Button) findViewById(R.id.answer_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mQuestionNum == 2){
                    mName = mEditText.getText().toString();
                    mEditText.setText("");
                    mText.setText(getString(R.string.medication_text3));
                    mQuestionNum++;
                }
                else if(mQuestionNum == 3){
                    mDosage = mEditText.getText().toString();
                    nextActivity();
                }
            }
        });

        mYesButton = (Button) findViewById(R.id.yes_button);
        mYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTookMed = true;
                mText.setText(getString(R.string.medication_text2));
                mYesButton.setVisibility(View.INVISIBLE);
                mNoButton.setVisibility(View.INVISIBLE);
                mEditText.setVisibility(View.VISIBLE);
                mSubmitButton.setVisibility(View.VISIBLE);
                mQuestionNum++;
            }
        });

        mNoButton = (Button) findViewById(R.id.no_button);
        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTookMed = false;
                nextActivity();
            }
        });
    }

    private void nextActivity(){
        DataManager.get(this).createMedicationEntry(mTookMed, mName, mDosage);
        Intent i = PostsessionActivity.newIntent(MedicationActivity.this, mSID);
        startActivity(i);
    }

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, MedicationActivity.class);
        return i;
    }
}
