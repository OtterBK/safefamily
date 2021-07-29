package hanium.oldercare.oldercareservice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hanium.oldercare.oldercareservice.apinetwork.MyRequestUtility;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogAlert;
import hanium.oldercare.oldercareservice.handlermessage.RegisterMessage;
import hanium.oldercare.oldercareservice.info.RegisterInfo;
import hanium.oldercare.oldercareservice.inputfilter.EmailFilter;
import hanium.oldercare.oldercareservice.inputfilter.IDFilter;
import hanium.oldercare.oldercareservice.inputfilter.NumberFilter;

public class EmailVerifyActivity extends AppCompatActivity {

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg == null) return;

            if(msg.what == RegisterMessage.ALREADY_USE_EMAIL.ordinal()){
                canSendEmail = false;
                lbl_warn_email_verify.setTextColor(Color.RED);
                lbl_warn_email_verify.setText("이미 사용중인 이메일입니다");
            } else if(msg.what == RegisterMessage.CAN_USE_EMAIL.ordinal()) {
                canSendEmail = true;
                lbl_warn_email_verify.setTextColor(Color.BLUE);
                lbl_warn_email_verify.setText("");
            } else if(msg.what == RegisterMessage.CANNOT_USE_EMAIL.ordinal()) {
                canSendEmail = false;
                lbl_warn_email_verify.setTextColor(Color.RED);
                lbl_warn_email_verify.setText("이메일을 입력해주세요.");
            } else if(msg.what == RegisterMessage.SEND_CODE_SUCCESS.ordinal()) {
                finalEmail = input_email.getText().toString();
                lbl_warn_email_verify.setTextColor(Color.BLUE);
                lbl_warn_email_verify.setText("인증코드를 전송하였습니다.");
            } else if(msg.what == RegisterMessage.SEND_CODE_FAIL.ordinal()) {
                lbl_warn_email_verify.setTextColor(Color.RED);
                lbl_warn_email_verify.setText("인증코드 전송에 실패했습니다.");
            } else if(msg.what == RegisterMessage.CODE_NULL.ordinal()) {
                lbl_warn_code_check.setTextColor(Color.RED);
                lbl_warn_code_check.setText("인증코드를 입력해주세요.");
            } else if(msg.what == RegisterMessage.CODE_CONSISTENT.ordinal()) {
                isCodeChecked = true;
                lbl_warn_code_check.setTextColor(Color.BLUE);
                btn_email_verify.setEnabled(false);
                btn_code_check.setEnabled(false);
                lbl_warn_email_verify.setText("이메일 인증을 완료하였습니다.");
                lbl_warn_code_check.setText("이메일 인증을 완료하였습니다.");
                CustomDialogAlert alert = new CustomDialogAlert(EmailVerifyActivity.this);
                alert.callFunction("완료", "인증이 완료되었습니다.");
            } else if(msg.what == RegisterMessage.CODE_INCONSISTENT.ordinal()) {
                lbl_warn_code_check.setTextColor(Color.RED);
                lbl_warn_code_check.setText("인증코드가 일치하지 않습니다.");
            } else if(msg.what == RegisterMessage.REGISTER_FAIL.ordinal()) {
                Toast.makeText(EmailVerifyActivity.this, "회원가입에 실패했습니다.", Toast.LENGTH_LONG).show();
            } else if(msg.what == RegisterMessage.REGISTER_DONE.ordinal()) {
                Toast.makeText(EmailVerifyActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
            }
        }
    };

    private Button btn_done;
    private Button btn_email_verify;
    private Button btn_code_check;
    private TextView lbl_warn_email_verify;
    private TextView lbl_warn_code_check;
    private TextView input_email;
    private TextView input_code;

    private boolean canSendEmail= false;
    private String finalEmail = "";
    private boolean isCodeChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        btn_done = (Button) findViewById(R.id.email_verify_btn_done);
        btn_email_verify = (Button) findViewById(R.id.email_verify_btn_sendCode);
        btn_code_check = (Button) findViewById(R.id.email_verify_btn_codeCheck);
        lbl_warn_email_verify = (TextView) findViewById(R.id.email_verify_sendCode_warn);
        lbl_warn_code_check = (TextView) findViewById(R.id.email_verify_codeCheck_warn);
        input_email = (TextView) findViewById(R.id.email_verify_input_email);
        input_code = (TextView) findViewById(R.id.email_verify_input_code);

        InputFilter[] emailFilters = new InputFilter[] { new EmailFilter()}; //입력 제한 필터
        input_email.setFilters(emailFilters);

        InputFilter[] codeFilters = new InputFilter[] { new NumberFilter()}; //입력 제한 필터
        input_code.setFilters(codeFilters);

        //인증 코드 이메일 전송
        btn_email_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!canSendEmail) return;

                String email = input_email.getText().toString();

                btn_email_verify.setEnabled(false);

                new Thread(new Runnable() {
                    public void run() {

                        Message message = null;
                        try {
                            isCodeChecked = false;
                            int cnt = MyRequestUtility.getCompareEmailAmount(email); //이미 존재하는 아이디인지 확인
                            if(cnt > 0){ //이미 존재하면
                                message = handler.obtainMessage(RegisterMessage.ALREADY_USE_EMAIL.ordinal());
                            } else {
                                MyRequestUtility.requestEmailVerify(email);
                                message = handler.obtainMessage(RegisterMessage.SEND_CODE_SUCCESS.ordinal());
                            }
                        } catch (Exception e) {
                            message = handler.obtainMessage(RegisterMessage.SEND_CODE_FAIL.ordinal());
                            e.printStackTrace();
                        }
                        handler.sendMessage(message);
                    }
                }).start();

                btn_email_verify.setEnabled(true);
            }
        });
        input_email.addTextChangedListener(new TextWatcher(){

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력란에 변화가 있을 시 조치
                if(TextUtils.isEmpty(input_email.getText()) || !input_email.getText().toString().contains("@")) {
                    Message message = handler.obtainMessage(RegisterMessage.CANNOT_USE_EMAIL.ordinal());
                    handler.sendMessage(message);
                } else {
                    Message message = handler.obtainMessage(RegisterMessage.CAN_USE_EMAIL.ordinal());
                    handler.sendMessage(message);
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

        //인증 코드 확인
        btn_code_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(finalEmail.equals("")) return;

                String email = input_email.getText().toString();

                Message message = null;

                if(TextUtils.isEmpty(input_code.getText())){
                    message = handler.obtainMessage(RegisterMessage.ALREADY_USE_EMAIL.ordinal());
                    handler.sendMessage(message);
                    return;
                }
                String code = input_code.getText().toString();

                btn_code_check.setEnabled(false);

                new Thread(new Runnable() {
                    public void run() {
                        Message message = null;
                        try {
                            boolean isConsistent = MyRequestUtility.verifyCodeComapre(email, code); //코드 일치 확인
                            if(isConsistent){ //일치하면
                                message = handler.obtainMessage(RegisterMessage.CODE_CONSISTENT.ordinal());
                            } else {
                                message = handler.obtainMessage(RegisterMessage.CODE_INCONSISTENT.ordinal());
                            }
                        } catch (Exception e) {
                            message = handler.obtainMessage(RegisterMessage.CODE_INCONSISTENT.ordinal());
                            e.printStackTrace();
                        }
                        handler.sendMessage(message);
                    }
                }).start();

                btn_code_check.setEnabled(true);
            }
        });

        //회원가입 완료
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isCodeChecked){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();

                    
                    new Thread(new Runnable() {
                        public void run() {
                            Message message = null;
                            try {
                                MyRequestUtility.register(RegisterInfo.tmpId, RegisterInfo.tmpPw, finalEmail, "", "", ""); //코드 일치 확인
                                message = handler.obtainMessage(RegisterMessage.REGISTER_DONE.ordinal());
                            } catch (Exception e) {
                                message = handler.obtainMessage(RegisterMessage.REGISTER_FAIL.ordinal());
                                e.printStackTrace();
                            }
                            handler.sendMessage(message);
                        }
                    }).start();

                } else {
                    CustomDialogAlert alert = new CustomDialogAlert(EmailVerifyActivity.this);
                    alert.callFunction("경고", "이메일 인증을 완료해주세요.");
                }
            }
        });
    }



}
