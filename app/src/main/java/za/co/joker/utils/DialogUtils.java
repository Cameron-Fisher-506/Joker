package za.co.joker.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import za.co.joker.dialogs.PermissionCallback;


public class DialogUtils
{

    public static AlertDialog createAlertPermission(Context context, String title, String message, boolean isPrompt, final PermissionCallback permissionCallback)
    {
        AlertDialog toReturn = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        if(isPrompt)
        {
            builder.setCancelable(true);
            builder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            permissionCallback.checkPermission(true);
                            dialog.cancel();
                        }
                    });

            builder.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }else
        {
            builder.setCancelable(false);
            builder.setPositiveButton(
                    "Okay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }

        toReturn = builder.create();

        return toReturn;

    }
}
