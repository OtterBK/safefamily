package hanium.oldercare.oldercareservice.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import hanium.oldercare.oldercareservice.R;

/**
 * Created by Administrator on 2017-08-07.
 */

public class CustomDialogConfirm {

    private Context context;
    private Dialog dialog;
    private TextView titleView;
    private TextView descView;
    private Button btn_ok;


    public CustomDialogConfirm(Context context) {

        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(String title, String desc, Runnable okFunction) {

        defineComponent(title, desc, okFunction);

    }

    public void callFunction(String title, String desc, Runnable okFunction, Runnable callback) {
        defineComponent(title, desc, okFunction);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                callback.run();
            }
        });
    }

    private void defineComponent(String title, String desc, Runnable okFunction){
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        dialog= new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dialog.setContentView(R.layout.activity_dialog_confirm);

        // 커스텀 다이얼로그를 노출한다.
        dialog.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        titleView = (TextView) dialog.findViewById(R.id.dialog_confirm_title);
        descView = (TextView) dialog.findViewById(R.id.dialog_confirm_desc);
        btn_ok = (Button) dialog.findViewById(R.id.dialog_confirm_ok);

        titleView.setText(title);
        descView.setText(desc);

        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 종료한다.
                dialog.dismiss();
            }
        });
        descView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 종료한다.
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 종료한다.
                dialog.dismiss();
                okFunction.run();
            }
        });
    }


}