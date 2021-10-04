package hanium.oldercare.oldercareservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import hanium.oldercare.oldercareservice.customdialog.AccountManageCheckDialog;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogInput;

public class AccountManageActivity extends AppCompatActivity {

    private Button btn_editLoginInfo_page;
    private Button btn_logout_page;
    private Button btn_deleteLoginInfo_page;
    private TextView tt_pw; //컴포넌트는 onCreate 함수에서 로드하는게 에러 안나요
    private TextView main_label;



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

    private void loadComponents(){
        btn_editLoginInfo_page = (Button) findViewById(R.id.account_manage_btn_changeInfo);
        btn_logout_page = (Button) findViewById(R.id.account_manage_btn_logout);
        btn_deleteLoginInfo_page = (Button) findViewById(R.id.account_manage_btn_unregister);
        tt_pw = (TextView) findViewById(R.id.check_pw);
    }

    private void setComponentsEvent() {
        //버튼별 화면 이동 기능
        btn_editLoginInfo_page.setOnClickListener(new View.OnClickListener() { //계정 정보 관리
            @Override
            public void onClick(View view) {
                AccountManageCheckDialog dlg = new AccountManageCheckDialog(AccountManageActivity.this);

                Runnable successCallback = new Runnable() {
                    public void run() {
                        Thread thread = new Thread() {
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), EditLoginInfoActivity.class); //이거를 띄울 액티비티로 변경
                                startActivity(intent);
                                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            }
                        };
                        thread.start();
                    }
                };

                dlg.Access_check(tt_pw, successCallback, 0);

            }
        });

        btn_logout_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogInput check = new CustomDialogInput(AccountManageActivity.this);
                Runnable successCallback = new Runnable(){
                    public void run(){
                        Intent i = new Intent(AccountManageActivity.this/*현재 액티비티 위치*/ , LoginSelectActivity.class/*이동 액티비티 위치*/);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                };
                check.callFunction("주의", "로그아웃",successCallback);
            }
        });

        btn_deleteLoginInfo_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountManageCheckDialog dlg = new AccountManageCheckDialog(AccountManageActivity.this);


                Runnable successCallback = new Runnable() {
                    public void run() {
                        CustomDialogInput check = new CustomDialogInput(AccountManageActivity.this);
                        Runnable successCallback = new Runnable(){
                            public void run(){
                                Intent i = new Intent(AccountManageActivity.this/*현재 액티비티 위치*/ , LoginSelectActivity.class/*이동 액티비티 위치*/);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }
                        };
                        check.callFunction("주의", "계정 삭제",successCallback);
                    }
                };
                dlg.Access_check(tt_pw, successCallback, 1);
            }
        });
    }
}