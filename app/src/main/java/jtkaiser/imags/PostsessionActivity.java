package jtkaiser.imags;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.UUID;

import static jtkaiser.imags.PresessionActivity.EXTRA_SID;

public class PostsessionActivity extends AppCompatActivity {

    private Button mLogoutButton;
    private UUID mSID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postsession);

        mSID = (UUID) getIntent().getSerializableExtra(EXTRA_SID);

        mLogoutButton = (Button) findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public static Intent newIntent(Context context, UUID SID) {
        Intent i = new Intent(context, PostsessionActivity.class);
        i.putExtra(EXTRA_SID, SID);
        return i;
    }
}
