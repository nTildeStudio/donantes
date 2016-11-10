package com.ntilde.utils.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.ntilde.app.R;

/**
 * Created by emanuel on 25/11/15.
 */
public class DialogUtils {

    public static void showNoNetDialog(Context context){
        showAlertError(context,
                context.getString(R.string.no_net_dialog_title),
                context.getString(R.string.no_net_dialog_message),
                new CloseActivityCallback(context));
    }

    public static void showNoDataRetrieved(Context context){
        showAlertError(context,
                context.getString(R.string.no_data_retrieved_dialog_title),
                context.getString(R.string.no_data_retrieved_dialog_message),
                new CloseActivityCallback(context));
    }

    private static void showAlertError(Context context, String title, String message,DialogInterface.OnClickListener callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.no_net_dialog_ok), callback);
        builder.create().show();
    }
}
