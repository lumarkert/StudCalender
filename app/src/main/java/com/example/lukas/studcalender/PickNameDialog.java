package com.example.lukas.studcalender;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class PickNameDialog extends DialogFragment {

    PickNameDialog.PickNameDialogListener mListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_pick_name, null);
        final EditText profileName = (EditText) mView.findViewById(R.id.editText);


        builder.setView(mView)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PickNameDialog.PickNameDialogListener activity = (PickNameDialog.PickNameDialogListener) getActivity();
                        activity.onDialogPositiveClick(profileName.getText().toString());
                        PickNameDialog.this.getDialog().dismiss();
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PickNameDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public interface PickNameDialogListener {
        public void onDialogPositiveClick(String profileName);
    }
}
