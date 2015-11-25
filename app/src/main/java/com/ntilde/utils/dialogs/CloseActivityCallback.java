package com.ntilde.utils.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by emanuel on 25/11/15.
 */
public class CloseActivityCallback implements DialogInterface.OnClickListener {

    private Context context;

    public CloseActivityCallback(Context context){
        this.context = context;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        ((Activity)context).finish();
        dialog.dismiss();
    }
}
