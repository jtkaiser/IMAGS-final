package jtkaiser.imags;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import jtkaiser.imags.database.DataManager;

import static jtkaiser.imags.PresessionActivity.EXTRA_SID;

public class PostsessionActivity extends AppCompatActivity {

    private static final String TAG = "PostSession";
    private Button mLogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postsession);

        mLogoutButton = (Button) findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostsessionActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Exit me", true);
                startActivity(intent);
                finish();
            }
        });

        DataManager.get(PostsessionActivity.this).endSession();
    }

    public static Intent newIntent(Context context, String SID) {
        Intent i = new Intent(context, PostsessionActivity.class);
        i.putExtra(EXTRA_SID, SID);
        return i;
    }

}
