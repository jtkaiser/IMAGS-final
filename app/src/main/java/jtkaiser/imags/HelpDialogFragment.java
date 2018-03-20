package jtkaiser.imags;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by jtkai on 2/6/2018.
 */

public class HelpDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.help_title)
                .setMessage(R.string.help_message)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
