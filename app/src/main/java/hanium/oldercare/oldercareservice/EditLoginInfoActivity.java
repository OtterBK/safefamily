package hanium.oldercare.oldercareservice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONArray;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import hanium.oldercare.oldercareservice.apinetwork.MyRequestUtility;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogAlert;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogLoading;
import hanium.oldercare.oldercareservice.handlermessage.AccountMessage;
import hanium.oldercare.oldercareservice.handlermessage.DeviceMessage;
import hanium.oldercare.oldercareservice.handlermessage.NetworkMessage;
import hanium.oldercare.oldercareservice.handlermessage.RegisterMessage;
import hanium.oldercare.oldercareservice.info.LoginInfo;
import hanium.oldercare.oldercareservice.inputfilter.PhoneFilter;
import hanium.oldercare.oldercareservice.utility.ScreenManager;
import hanium.oldercare.oldercareservice.utility.VibrateUtility;

public class EditLoginInfoActivity extends AppCompatActivity {

    //백그라운드 작업 응답 처리에 사용할 메시지 핸들러
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg == null) return;

            boolean isVibrate = false;
            if(msg.what == AccountMessage.EDIT_SUCCEED.ordinal()){

                LoginInfo.PW = (String)msg.obj; //로컬 로그인 정보 갱신

                CustomDialogAlert alert = new CustomDialogAlert(EditLoginInfoActivity.this);

                Runnable callback = ()->{
                    EditLoginInfoActivity.this.finish();
                };

                alert.callFunction("계정 변경 성공", "프로필 정보가 변경되었습니다.", callback);

            } else if(msg.what == AccountMessage.EDIT_FAIL.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(EditLoginInfoActivity.this);
                alert.callFunction("연결 실패", "요청에 실패하였습니다.\n\n네트워크를 확인하거나\n\n잠시 후 다시 시도해주세요.");
                isVibrate = true;
            }

        }
    };


    private Button btn_editUserInfo;
    private TextView user_ID; // = (LoginInfo.ID);
    private TextView user_email;
    private EditText input_pw;
    private EditText input_pwCheck;
    private EditText input_name;
    private EditText input_phoneNumber;

    private String name;
    private String phoneNumber;
    private String pw;
    private String email;

    private TextView name_warn;
    private TextView phoneNumber_warn;
    private TextView pw_warn;

    private boolean isCompSet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        ScreenManager.transparentStatusBar(this);

        loadComponents();
        refreshData();
        setFilters();
        setComponentsEvent();
    }


    private void loadComponents(){
        btn_editUserInfo = (Button) findViewById(R.id.account_btn_next);

        user_ID = (TextView) findViewById(R.id.user_ID);
        user_email = (TextView) findViewById(R.id.user_email);
        input_name = (EditText) findViewById(R.id.user_input_name);
        input_phoneNumber = (EditText) findViewById(R.id.user_input_phoneNumber);
        input_pw = (EditText) findViewById(R.id.user_input_pw);
        input_pwCheck = (EditText) findViewById(R.id.user_input_new_pwcheck);
        input_pwCheck.setVisibility(View.INVISIBLE);


        name_warn = (TextView) findViewById(R.id.account_name_warn);
        phoneNumber_warn = (TextView) findViewById(R.id.account_phoneNumber_warn);
        pw_warn = (TextView) findViewById(R.id.account_pw_warn);
        name_warn.setVisibility(View.INVISIBLE);
        phoneNumber_warn.setVisibility(View.INVISIBLE);
        pw_warn.setVisibility(View.INVISIBLE);

        isCompSet = true;
    }

    private void refreshData() {

        if(!isCompSet) return;


        new Thread(new Runnable() {
            public void run() {

                try {
                    pw = LoginInfo.PW;
                    JSONArray userInfo = MyRequestUtility.getUserInfo(LoginInfo.ID, pw);

                    if(userInfo != null){
                        email = (String)userInfo.get(0);
                        name = (String)userInfo.get(1);
                        phoneNumber = (String)userInfo.get(2);

                        user_email.setText(email);
                        input_name.setText(name);
                        input_phoneNumber.setText(phoneNumber);
                    }
                    user_ID.setText(LoginInfo.ID);
                    input_pw.setText(pw);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }

    private void setFilters(){
        InputFilter nickName_lengthFilter = new InputFilter.LengthFilter(15);
        InputFilter[] nickNameFilters = new InputFilter[] { nickName_lengthFilter}; //입력 제한 필터
        input_name.setFilters(nickNameFilters);

        InputFilter phone_lengthFilter = new InputFilter.LengthFilter(13);
        InputFilter[] phoneFilters = new InputFilter[] { new PhoneFilter(), phone_lengthFilter}; //입력 제한 필터
        input_phoneNumber.setFilters(phoneFilters);

        input_phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        InputFilter pw_lengthFilter = new InputFilter.LengthFilter(15);
        InputFilter[] pwFilters = new InputFilter[] { pw_lengthFilter}; //입력 제한 필터
        input_pw.setFilters(pwFilters);
        input_pwCheck.setFilters(pwFilters);
    }


    private void setComponentsEvent(){
        //버튼별 화면 이동 기능
        btn_editUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_tmp = input_name.getText().toString();
                String phoneNumber_tmp = input_phoneNumber.getText().toString();
                String pw_tmp = input_pw.getText().toString();


                if((name_warn.getText().toString()).equals("*") && (input_name.getText().toString()).equals("")){
                    CustomDialogAlert alert = new CustomDialogAlert(EditLoginInfoActivity.this);
                    alert.callFunction("경고", "이름을 입력하여 주세요.");
                }else if((phoneNumber_warn.getText().toString()).equals("*") && (input_phoneNumber.getText().toString()).equals("")){
                    CustomDialogAlert alert = new CustomDialogAlert(EditLoginInfoActivity.this);
                    alert.callFunction("경고", "연락처를 입력하여 주세요.");
                }else if(!((pw_warn.getText().toString()).equals("") || ((pw_warn.getText().toString()).equals("*")))){
                    CustomDialogAlert alert = new CustomDialogAlert(EditLoginInfoActivity.this);
                    alert.callFunction("경고", "비밀번호를 확인하여 주세요.");
                }else {

                    CustomDialogLoading loading = new CustomDialogLoading(EditLoginInfoActivity.this);
                    loading.callFunction();

                    Thread thread = new Thread(() -> {
                        Message message = null;

                        try {

                            // 비밀번호를 변경했다면
                            String new_pw = LoginInfo.PW;
                            if(!LoginInfo.PW.equals(pw_tmp)){
                                new_pw = pw_tmp;
                            }

                            boolean isSucceed = MyRequestUtility.editUserInfo(LoginInfo.ID, LoginInfo.PW, name_tmp, phoneNumber_tmp, new_pw);
                            loading.dismiss();
                            if(isSucceed){
                                message = handler.obtainMessage(AccountMessage.EDIT_SUCCEED.ordinal());
                                message.obj = new_pw;
                            } else {
                                message = handler.obtainMessage(AccountMessage.EDIT_FAIL.ordinal());
                            }



                        } catch (Exception e) {
                            message = handler.obtainMessage(AccountMessage.EDIT_FAIL.ordinal());
                            e.printStackTrace();
                        }
                        handler.sendMessage(message);
                    });
                    thread.start();

//
//
//                    new Thread(new Runnable() {
//                        public void run() {
//                            Message message = null;
//                            try {
//                                    MyRequestUtility.editUserInfo(LoginInfo.ID, name_tmp, phoneNumber_tmp, LoginInfo.PW);
//                                    finish();
//                            } catch (Exception e) {
//                                CustomDialogAlert alert = new CustomDialogAlert(EditLoginInfoActivity.this);
//                                alert.callFunction("연결 실패", "요청에 실패하였습니다.\n네트워크를 확인하거나\n잠시 후 다시 시도해주세요.");
//                                e.printStackTrace();
//                            }
//                            loading.dismiss();
//                        }
//                    }).start();
                }
            }
        });

        input_name.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력란에 변화가 있을 시 조치
                changed(name, input_name, name_warn);
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

        input_phoneNumber.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력란에 변화가 있을 시 조치
                changed(phoneNumber, input_phoneNumber, phoneNumber_warn);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때 조치
                changed(phoneNumber, input_phoneNumber, phoneNumber_warn);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에 조치
            }
        });

        input_pw.addTextChangedListener(new TextWatcher(){

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력란에 변화가 있을 시 조치
                changed(pw, input_pw, pw_warn);
                if(pw_warn.getVisibility() == View.VISIBLE){
                    if(TextUtils.isEmpty(input_pw.getText()) || input_pw.getText().length() < 8) {
                        pw_warn.setText("비밀번호는 8자 이상이어야합니다.");
                    }else
                        pw_warn.setText("*");
                    input_pwCheck.setVisibility(View.VISIBLE);
                }else
                    input_pwCheck.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때 조치
                changed(pw, input_pw, pw_warn);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에 조치
            }
        });

        input_pwCheck.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력란에 변화가 있을 시 조치
                if(input_pwCheck.getVisibility() == View.VISIBLE){
                    if(!TextUtils.isEmpty(input_pwCheck.getText())) {
                        if (!(input_pw.getText().toString().equals(input_pwCheck.getText().toString()))) {
                            pw_warn.setText("비밀번호가 일치하지 않습니다.");
                        } else {
                            pw_warn.setText("*");
                        }
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
    public void changed(String item, EditText changed_item, TextView symbol){
        if(item.equals(changed_item.getText().toString())){
            symbol.setVisibility(View.INVISIBLE);
        }
        else {
            symbol.setVisibility(View.VISIBLE);
        }
    }
}
