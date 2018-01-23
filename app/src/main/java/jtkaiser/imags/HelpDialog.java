package jtkaiser.imags;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by jtkai on 1/23/2018.
 */

public class HelpDialog {
    public HelpDialog(Context context){
        build(context);
    }

    private void build(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.help_message)
                .setTitle(R.string.help_title);
        builder.setPositiveButton(R.string.help_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog dialog = builder.create();
    }
}
