package hanium.oldercare.oldercareservice;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import hanium.oldercare.oldercareservice.apinetwork.MyRequestUtility;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogAlert;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogLoading;
import hanium.oldercare.oldercareservice.info.LoginInfo;

public class EditLoginInfoActivity extends AppCompatActivity {

    private Button btn_editPassword;
    String id = LoginInfo.ID;
    private TextView input_pw;
    private TextView input_new_pw;
    private TextView input_new_pwCheck;
    private TextView account_pw_warn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        loadComponents();
        setComponentsEvent();
        LoginInfo.Login_check = false;
    }

    private void loadComponents(){
        btn_editPassword = (Button) findViewById(R.id.account_btn_next);
        input_pw = (TextView) findViewById(R.id.user_input_pw);
        input_new_pw = (TextView) findViewById(R.id.user_input_new_pw);
        input_new_pwCheck = (TextView) findViewById(R.id.user_input_new_pwcheck);
        account_pw_warn = (TextView) findViewById(R.id.account_pw_warn);
    }

    private void setComponentsEvent(){
        //버튼별 화면 이동 기능
        btn_editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pw = input_pw.getText().toString();
                String new_pw = input_new_pw.getText().toString();
                String new_pwcheck = input_new_pwCheck.getText().toString();


                CustomDialogLoading loading = new CustomDialogLoading(EditLoginInfoActivity.this);
                loading.callFunction();

                new Thread(new Runnable() {
                    public void run() {
                        Message message = null;
                        try {
                            CustomDialogAlert alert = new CustomDialogAlert(EditLoginInfoActivity.this);

                            if(MyRequestUtility.instantLogin(id,pw)){
                                MyRequestUtility.editPassword(id, new_pw);
                                finish();
                            } else {
                                alert.callFunction("비밀번호 오류", "잘못된 비밀번호입니다.");
                            }
                        } catch (Exception e) {
                            CustomDialogAlert alert = new CustomDialogAlert(EditLoginInfoActivity.this);
                            alert.callFunction("연결 실패", "요청에 실패하였습니다.\n네트워크를 확인하거나\n잠시 후 다시 시도해주세요.");
                            e.printStackTrace();
                        }
                        loading.dismiss();
                    }
                }).start();
            }
        });

        input_new_pw.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력란에 변화가 있을 시 조치
                if(TextUtils.isEmpty(input_new_pw.getText()) || input_new_pw.getText().length() < 8) {
                    account_pw_warn.setTextColor(Color.RED);
                    account_pw_warn.setText("비밀번호는 8자 이상이여야합니다.");
                } else if(!TextUtils.isEmpty(input_new_pwCheck.getText())){
                    if(!input_pw.getText().toString().equals(input_new_pwCheck.getText().toString())){
                        account_pw_warn.setTextColor(Color.RED);
                        account_pw_warn.setText("비밀번호가 일치하지 않습니다.");
                    } else {
                        account_pw_warn.setTextColor(Color.BLUE);
                        account_pw_warn.setText("비밀번호가 일치합니다.");
                        LoginInfo.PW = input_new_pw.getText().toString();
                    }
                } else {
                    account_pw_warn.setTextColor(Color.BLUE);
                    account_pw_warn.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때 조치
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에 조치
            }

        });

        input_new_pwCheck.addTextChangedListener(new TextWatcher(){

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력난에 변화가 있을 시 조치
                if(TextUtils.isEmpty(input_new_pw.getText()) || input_new_pw.getText().length() < 8) {
                    account_pw_warn.setTextColor(Color.RED);
                    account_pw_warn.setText("비밀번호는 8자 이상이여야합니다.");
                } else if(!TextUtils.isEmpty(input_new_pw.getText())){
                    if(!input_pw.getText().toString().equals(input_new_pwCheck.getText().toString())){
                        account_pw_warn.setTextColor(Color.RED);
                        account_pw_warn.setText("비밀번호가 일치하지 않습니다.");
                    } else {
                        account_pw_warn.setTextColor(Color.BLUE);
                        account_pw_warn.setText("비밀번호가 일치합니다.");
                        LoginInfo.PW = input_new_pw.getText().toString();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때 조치
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에 조치
            }
        });
    }

}
