package hanium.oldercare.oldercareservice;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hanium.oldercare.oldercareservice.apinetwork.MyRequestUtility;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogAlert;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogLoading;
import hanium.oldercare.oldercareservice.handlermessage.DeviceMessage;
import hanium.oldercare.oldercareservice.handlermessage.NetworkMessage;
import hanium.oldercare.oldercareservice.info.DeviceInfo;
import hanium.oldercare.oldercareservice.inputfilter.NumberFilter;
import hanium.oldercare.oldercareservice.utility.ScreenManager;
import hanium.oldercare.oldercareservice.utility.VibrateUtility;

public class DevicePasswordActivity extends AppCompatActivity {

    private Button btn_done;

    private TextView input_current;
    private TextView input_new_pw;
    private TextView input_new_pw_check;

    private TextView pw_warn;
    private TextView pw_check_warn;


    private String device_id;
    private String device_pw;

    private String final_pw;
    private String final_new_pw;

    private Vibrator vibrator;


    //백그라운드 작업 응답 처리에 사용할 메시지 핸들러
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg == null) return;

            boolean isVibrate = false;

            if(msg.what == DeviceMessage.EDIT_PASSWORD_SUCCEED.ordinal()){
                Toast.makeText(DevicePasswordActivity.this, "비밀번호를 변경하였습니다.", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            } else if(msg.what == DeviceMessage.EDIT_PASSWORD_FAIL.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(DevicePasswordActivity.this);
                alert.callFunction("변경 실패", "비밀번호 변경에 실패했습니다.\n나중에 다시 시도해보세요.");
            } else if(msg.what == DeviceMessage.EDIT_PASSWORD_NOT_MATCH.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(DevicePasswordActivity.this);
                alert.callFunction("인증 실패", "디바이스의 현재 비빌번호가 일치하지 않습니다.\n");
            } else if(msg.what == DeviceMessage.EDIT_PASSWORD_NOT_MATCH.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(DevicePasswordActivity.this);
                alert.callFunction("연결 실패", "요청에 실패하였습니다.\n네트워크를 확인하거나\n잠시 후 다시 시도해주세요.");
            }

            if(isVibrate) VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
        }
    };

    private void loadComponents(){
        btn_done = (Button) findViewById(R.id.device_password_btn_done);

        input_current = (TextView) findViewById(R.id.device_password_input_current);
        input_new_pw = (TextView) findViewById(R.id.device_password_input_new);
        input_new_pw_check = (TextView) findViewById(R.id.device_password_input_new_check);

        pw_warn = (TextView) findViewById(R.id.device_password_warn);
        pw_check_warn = (TextView) findViewById(R.id.device_password_check_warn);
    }


    private void setEffectObject(){
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

    }

    private void setComponentsEvent() {
        //버튼별 화면 이동 기능

        input_new_pw.addTextChangedListener(new TextWatcher(){

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력란에 변화가 있을 시 조치
                if(TextUtils.isEmpty(input_new_pw.getText()) || input_new_pw.getText().length() < 4) {
                    pw_warn.setTextColor(Color.RED);
                    pw_warn.setText("비밀번호는 4자 이상이여야합니다.");
                } else if(!TextUtils.isEmpty(input_new_pw_check.getText())){
                    if(!input_new_pw.getText().toString().equals(input_new_pw_check.getText().toString())){
                        pw_warn.setTextColor(Color.RED);
                        pw_warn.setText("비밀번호가 일치하지 않습니다.");
                    } else {
                        pw_warn.setTextColor(Color.BLUE);
                        pw_warn.setText("비밀번호가 일치합니다.");
                        final_new_pw = input_new_pw.getText().toString();
                    }
                } else {
                    pw_warn.setTextColor(Color.BLUE);
                    pw_warn.setText("");
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

        input_new_pw_check.addTextChangedListener(new TextWatcher(){

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력난에 변화가 있을 시 조치
                if(TextUtils.isEmpty(input_new_pw.getText()) || input_new_pw.getText().length() < 4) {
                    pw_warn.setTextColor(Color.RED);
                    pw_warn.setText("비밀번호는 8자 이상이여야합니다.");
                } else if(!TextUtils.isEmpty(input_new_pw.getText())){
                    if(!input_new_pw.getText().toString().equals(input_new_pw_check.getText().toString())){
                        pw_warn.setTextColor(Color.RED);
                        pw_warn.setText("비밀번호가 일치하지 않습니다.");
                    } else {
                        pw_warn.setTextColor(Color.BLUE);
                        pw_warn.setText("비밀번호가 일치합니다.");
                        final_new_pw = input_new_pw.getText().toString();
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

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (input_current.getText().toString().equals("")) {
                    CustomDialogAlert alert = new CustomDialogAlert(DevicePasswordActivity.this);
                    alert.callFunction("경고", "현재 비밀번호를 입력해주세요.");
                    VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
                } else if (input_new_pw.getText().toString().equals("")) {
                    CustomDialogAlert alert = new CustomDialogAlert(DevicePasswordActivity.this);
                    alert.callFunction("경고", "새로운 비밀번호를 입력해주세요.");
                    VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
                } else { //인증 진행

                    final_pw = input_current.getText().toString();

                    CustomDialogLoading loading = new CustomDialogLoading(DevicePasswordActivity.this);
                    loading.callFunction();

                    new Thread(()->{

                        Message message = null;

                        try {

                            boolean isCredentialSucceed =
                                    MyRequestUtility.deviceCredential(device_id, final_pw);

                            if (isCredentialSucceed) {

                                boolean isEditSucceed = MyRequestUtility.setDevicePassword(device_id, final_pw, final_new_pw);

                                if(isEditSucceed){
                                    message = handler.obtainMessage(DeviceMessage.EDIT_PASSWORD_SUCCEED.ordinal());
                                } else {
                                    message = handler.obtainMessage(DeviceMessage.EDIT_PASSWORD_FAIL.ordinal());
                                }

                            } else {
                                message = handler.obtainMessage(DeviceMessage.EDIT_PASSWORD_NOT_MATCH.ordinal());
                            }
                            loading.dismiss();
                        } catch (Exception e) {
                            message = handler.obtainMessage(NetworkMessage.NETWORK_FAIL.ordinal());
                            e.printStackTrace();
                        }
                        handler.sendMessage(message);



                    }).start();

                }
            }
        });

    }

    private void setFilters(){

        InputFilter pw_lengthFilter = new InputFilter.LengthFilter(8);
        InputFilter[] pwFilters = new InputFilter[] { new NumberFilter(), pw_lengthFilter}; //입력 제한 필터
        input_current.setFilters(pwFilters);

        input_new_pw.setFilters(pwFilters);

        input_new_pw_check.setFilters(pwFilters);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_edit_password);

        device_id = DeviceInfo.tmpId;
        device_pw = DeviceInfo.tmpPw;

        ScreenManager.transparentStatusBar(this);

        loadComponents();
        setFilters();
        setComponentsEvent();
        setEffectObject();

    }



}