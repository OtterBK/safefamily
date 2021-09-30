package hanium.oldercare.oldercareservice;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hanium.oldercare.oldercareservice.apinetwork.MyRequestUtility;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogAlert;
import hanium.oldercare.oldercareservice.handlermessage.LoginMessage;
import hanium.oldercare.oldercareservice.info.LoginInfo;

/**
 * Created by Administrator on 2017-08-07.
 */

public class AccountManageCheckActivity extends AppCompatActivity{

    private Context context;
//    private int retVal; // ok-success: 2, ok-fails: 1, cancel: 0
    private TextView titleTxt;


    public AccountManageCheckActivity(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void Access_check(TextView pw, Runnable successCallback, Integer val) {

        final Dialog dlg = new Dialog(context);        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);          // 액티비티의 타이틀바를 숨긴다.
        dlg.setContentView(R.layout.activity_change_info_check);    // 커스텀 다이얼로그의 레이아웃을 설정한다.
        titleTxt = (TextView) dlg.findViewById(R.id.account_usr_lbl1);

        if(val == 0){
            titleTxt.setText("계정정보 접근");
        }
        else if(val == 1){
            titleTxt.setText("회원탈퇴");
        }
        dlg.show();                                                 // 커스텀 다이얼로그를 노출한다.

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText message = (EditText) dlg.findViewById(R.id.edit_pw);
        final Button okButton = (Button) dlg.findViewById(R.id.account_btn_check);
        final Button cancelButton = (Button) dlg.findViewById(R.id.account_btn_cancel);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                if(!(LoginInfo.PW.equals(message.getText().toString()))){
                    Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                }else{dlg.dismiss();
                    successCallback.run();}
                // 커스텀 다이얼로그를 종료한다.

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소했습니다.", Toast.LENGTH_SHORT).show();
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}