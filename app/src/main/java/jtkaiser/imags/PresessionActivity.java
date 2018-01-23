package jtkaiser.imags;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PresessionActivity extends AppCompatActivity {

    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private Button mContinue;
    private Button mHelp;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presession);

        Intent intent = getIntent();
        mToken = intent.getStringExtra(EXTRA_TOKEN);

        mContinue = (Button) findViewById(R.id.presession_continue);
        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = SearchActivity.createIntent(PresessionActivity.this);
                i.putExtra(SearchActivity.EXTRA_TOKEN, mToken);
                startActivity(i);
            }
        });
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, PresessionActivity.class);
    }
}
