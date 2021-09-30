package hanium.oldercare.oldercareservice.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import hanium.oldercare.oldercareservice.R;

public class CustomDialogLoading {

    private Context context;
    private Dialog dlg;

    public CustomDialogLoading(Context context) {
        this.context = context;


    }

    public void callFunction() {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);
        this.dlg = dlg;

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.setContentView(R.layout.activity_dialog_loading);

        dlg.show();
    }

    public void dismiss(){
        dlg.dismiss();
    }

}
