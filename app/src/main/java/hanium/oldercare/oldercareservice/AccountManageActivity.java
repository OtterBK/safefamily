package hanium.oldercare.oldercareservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import hanium.oldercare.oldercareservice.customdialog.CustomDialogAlert;
import hanium.oldercare.oldercareservice.info.LoginInfo;

public class AccountManageActivity extends AppCompatActivity {

    private Button btn_editLoginInfo_page;
    private Button btn_logout_page;
    private Button btn_deleteLoginInfo_page;
    final TextView tt_pw = (TextView) findViewById(R.id.check_pw);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manage);

        loadComponents();
        setComponentsEvent();
/*        if(LoginInfo.Login_check) {
            if (LoginInfo.PW.equals(tt_pw)) {
                Intent intent = new Intent(getApplicationContext(), EditLoginInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.none);
            } else {
                CustomDialogAlert alert = new CustomDialogAlert(AccountManageActivity.this);
                alert.callFunction("경고", "비밀번호가 일치하지 않습니다.");
            }
        }*/
    }
    /*if((tt_pw.getText().toString()).equals(LoginInfo.PW)){
        Intent intent = new Intent(getApplicationContext(), EditLoginInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.none);
    }else{
        CustomDialogAlert alert = new CustomDialogAlert(AccountManageActivity.this);
        alert.callFunction("경고", "비밀번호가 일치하지 않습니다.");
    }*/

    private void loadComponents(){
        btn_editLoginInfo_page = (Button) findViewById(R.id.account_manage_btn_changeInfo);
        btn_logout_page = (Button) findViewById(R.id.account_manage_btn_logout);
        btn_deleteLoginInfo_page = (Button) findViewById(R.id.account_manage_btn_unregister);
    }

    private void setComponentsEvent(){
        //버튼별 화면 이동 기능
        btn_editLoginInfo_page.setOnClickListener(new View.OnClickListener() { //계정 정보 관리
            @Override
            public void onClick(View view){
                AccountManageCheckActivity dlg = new AccountManageCheckActivity(AccountManageActivity.this);
                dlg.Access_check(tt_pw);
            }
        });

        btn_logout_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditLoginInfoActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                overridePendingTransition(R.anim.slide_in_right, R.anim.none);
            }
        });

        btn_deleteLoginInfo_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditLoginInfoActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                overridePendingTransition(R.anim.slide_in_right, R.anim.none);
            }
        });
    }
}
