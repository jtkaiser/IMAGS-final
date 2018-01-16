package jtkaiser.imags;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PresessionActivity extends AppCompatActivity {

    private Button mContinue;
    private Button mHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presession);

        mContinue = (Button) findViewById(R.id.presession_continue);
        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PresessionActivity.this, SearchActivity.class);
                startActivity(i);
            }
        });
    }
}
