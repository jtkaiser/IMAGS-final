package jtkaiser.imags;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String DIALOG_HELP = "HelpDialog";

    private Button mProceedButton;
    private Button mHelpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProceedButton = (Button)findViewById(R.id.welcome_continue);
        mProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        mHelpButton = (Button)findViewById(R.id.welcome_help);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                HelpDialogFragment dialog = new HelpDialogFragment();
                dialog.show(manager, DIALOG_HELP);
            }
        });

    }


}
