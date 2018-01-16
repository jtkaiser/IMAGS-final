package jtkaiser.imags;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MedicationActivity extends AppCompatActivity {

    private Button mYesbutton;
    private Button mNoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);

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
        Intent i = new Intent(MedicationActivity.this, PostsessionActivity.class);
        startActivity(i);
    }
}
