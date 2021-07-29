package hanium.oldercare.oldercareservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

public class MyAlertDialog {

    public static void showInfoDialog(View view, Activity activity, String title, String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title).setMessage(text);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

}
