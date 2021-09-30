package hanium.oldercare.oldercareservice;

import android.content.Context;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

import hanium.oldercare.oldercareservice.apinetwork.MyRequestUtility;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogAlert;
import hanium.oldercare.oldercareservice.handlermessage.DeviceMessage;
import hanium.oldercare.oldercareservice.handlermessage.NetworkMessage;
import hanium.oldercare.oldercareservice.handlermessage.RegisterMessage;
import hanium.oldercare.oldercareservice.info.RegisterInfo;
import hanium.oldercare.oldercareservice.inputfilter.IDFilter;
import hanium.oldercare.oldercareservice.inputfilter.NumberFilter;
import hanium.oldercare.oldercareservice.inputfilter.PWFilter;
import hanium.oldercare.oldercareservice.utility.ScreenManager;
import hanium.oldercare.oldercareservice.utility.VibrateUtility;

public class DeviceAddActivity extends AppCompatActivity {

    private Button btn_next;
    private TextView reason;
    private TextView input_id;
    private TextView input_pw;

    private Vibrator vibrator;


    //백그라운드 작업 응답 처리에 사용할 메시지 핸들러
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg == null) return;

            boolean isVibrate = false;

            if(msg.what == DeviceMessage.CREDENTIAL_SUCCEED.ordinal()){
//                lbl_warn_id.setTextColor(Color.BLUE);
//                lbl_warn_id.setText("사용 가능한 아이디입니다.");
//                id_final = input_id.getText().toString();
            } else if(msg.what == DeviceMessage.CREDENTIAL_FAIL.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(DeviceAddActivity.this);
                alert.callFunction("인증 실피", "인증에 실패하였습니다.\n디바이스 번호 또는 비밀번호를 확인해주세요.");
                isVibrate = true;
            } else if(msg.what == NetworkMessage.NETWORK_FAIL.ordinal()) {
                CustomDialogAlert alert = new CustomDialogAlert(DeviceAddActivity.this);
                alert.callFunction("연결 실패", "요청에 실패하였습니다.\n네트워크를 확인하거나\n잠시 후 다시 시도해주세요.");
                isVibrate = true;
            }

            if(isVibrate) VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
        }
    };

    private void loadComponents(){
        btn_next = (Button) findViewById(R.id.device_credential_btn_next);

        input_id = (TextView) findViewById(R.id.device_credential_input_id);
        input_pw = (TextView) findViewById(R.id.device_credential_input_id);

        reason = (TextView) findViewById(R.id.device_credential_reason);
    }


    private void setEffectObject(){
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

    }

    private void setComponentsEvent() {
        //버튼별 화면 이동 기능
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (input_id.getText().equals("")) {
                    CustomDialogAlert alert = new CustomDialogAlert(DeviceAddActivity.this);
                    alert.callFunction("경고", "디바이스의 번호(ID)를 입력해주세요.");
                    VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
                } else if (input_id.getText().equals("")) {
                    CustomDialogAlert alert = new CustomDialogAlert(DeviceAddActivity.this);
                    alert.callFunction("경고", "디바이스의 비밀번호를 입력해주세요.");
                    VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
                } else { //인증 진행

                    String id = input_id.getText().toString();
                    String pw = input_pw.getText().toString();

                    new Thread(()-> {
                        Message message = null;
                        try {
                            boolean isSucceed = MyRequestUtility.deviceCredential(id, pw);

                            if (isSucceed) {
                                message = handler.obtainMessage(DeviceMessage.CREDENTIAL_SUCCEED.ordinal());
                            } else {
                                message = handler.obtainMessage(DeviceMessage.CREDENTIAL_FAIL.ordinal());
                            }
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
        InputFilter id_lengthFilter = new InputFilter.LengthFilter(10);
        InputFilter[] idFilters = new InputFilter[] { new NumberFilter(), id_lengthFilter}; //입력 제한 필터
        input_id.setFilters(idFilters);


        InputFilter pw_lengthFilter = new InputFilter.LengthFilter(10);
        InputFilter[] pwFilters = new InputFilter[] { new NumberFilter(), pw_lengthFilter}; //입력 제한 필터
        input_pw.setFilters(pwFilters);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_credential);

        ScreenManager.transparentStatusBar(this);

        loadComponents();
        setFilters();
        setComponentsEvent();
        setEffectObject();

    }



}