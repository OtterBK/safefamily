package hanium.oldercare.oldercareservice.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import hanium.oldercare.oldercareservice.EditLoginInfoActivity;
import hanium.oldercare.oldercareservice.R;
import hanium.oldercare.oldercareservice.apinetwork.MyRequestUtility;
import hanium.oldercare.oldercareservice.handlermessage.AccountMessage;
import hanium.oldercare.oldercareservice.info.LoginInfo;

/**
 * Created by Administrator on 2017-08-07.
 */

public class CustomDialogInput {

    private Context context;

    public CustomDialogInput(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(String title, TextView main_label) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_dialog_input);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final TextView titleView = (TextView) dlg.findViewById(R.id.dialog_input_title);
        final EditText message = (EditText) dlg.findViewById(R.id.dialog_input_input);
        final Button okButton = (Button) dlg.findViewById(R.id.dialog_input_ok);
        final Button cancelButton = (Button) dlg.findViewById(R.id.dialog_input_cancel);

        titleView.setText(title);

        message.setEnabled(true);


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                main_label.setText(message.getText().toString());
                Toast.makeText(context, "\"" +  message.getText().toString() + "\" 을 입력하였습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }

    public void callFunction(String title, String label, Runnable s, Integer val) { //val:0 로그아웃 :1 계정삭제

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_dialog_input);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final TextView titleView = (TextView) dlg.findViewById(R.id.dialog_input_title);
        final EditText message = (EditText) dlg.findViewById(R.id.dialog_input_input);
        final Button okButton = (Button) dlg.findViewById(R.id.dialog_input_ok);
        final Button cancelButton = (Button) dlg.findViewById(R.id.dialog_input_cancel);

        titleView.setText(title);

        message.setText(label + "하시겠습니까?");
        message.setEnabled(false);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(val == 1){
                        Thread thread = new Thread(() -> {

                            try {
                                boolean isSucceed = MyRequestUtility.deleteUserInfo(LoginInfo.ID, LoginInfo.PW);
                                    Toast.makeText(context, label+"되었습니다.", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(context, "네트워크 통신에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        });
                        thread.start();
                }else{
                    Toast.makeText(context, label+"되었습니다.", Toast.LENGTH_SHORT).show();
                }
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
//                s.run();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}