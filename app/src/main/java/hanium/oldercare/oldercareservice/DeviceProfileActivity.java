package hanium.oldercare.oldercareservice;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import hanium.oldercare.oldercareservice.apinetwork.MyRequestUtility;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogAlert;
import hanium.oldercare.oldercareservice.handlermessage.DeviceMessage;
import hanium.oldercare.oldercareservice.handlermessage.NetworkMessage;
import hanium.oldercare.oldercareservice.info.DeviceInfo;
import hanium.oldercare.oldercareservice.inputfilter.NumberFilter;
import hanium.oldercare.oldercareservice.utility.ScreenManager;
import hanium.oldercare.oldercareservice.utility.VibrateUtility;

public class DeviceProfileActivity extends AppCompatActivity {

    private Button btn_done;
    private TextView reason;
    private TextView input_name;
    private TextView input_age;
    private TextView input_address;
    private TextView input_desc;
    private String final_id;
    private String final_pw;

    private Vibrator vibrator;


    //백그라운드 작업 응답 처리에 사용할 메시지 핸들러
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg == null) return;

            boolean isVibrate = false;

            if(msg.what == DeviceMessage.CREDENTIAL_SUCCEED.ordinal()){

                Runnable callback = () -> {

                };

                CustomDialogAlert alert = new CustomDialogAlert(DeviceProfileActivity.this);
                alert.callFunction("인증 성공", "디바이스 인증에 성공하였습니다.\n디바이스의 정보를 입력해주세요.");

                DeviceInfo.tmpId = final_id;
                DeviceInfo.tmpPw = final_pw;

            }

            if(isVibrate) VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
        }
    };

    private void loadComponents(){
        btn_done = (Button) findViewById(R.id.device_profile_btn_done);

        input_name = (TextView) findViewById(R.id.device_profile_ward_name);
        input_age = (TextView) findViewById(R.id.device_profile_ward_age);
        input_address = (TextView) findViewById(R.id.device_profile_ward_address);
        input_desc = (TextView) findViewById(R.id.device_profile_ward_desc);

        reason = (TextView) findViewById(R.id.device_credential_reason);
    }


    private void setEffectObject(){
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

    }

    private void setComponentsEvent() {
        //버튼별 화면 이동 기능
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (input_name.getText().toString().equals("")) {
                    CustomDialogAlert alert = new CustomDialogAlert(DeviceProfileActivity.this);
                    alert.callFunction("경고", "이름을 입력해주세요..");
                    VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
                } else if (input_age.getText().toString().equals("")) {
                    CustomDialogAlert alert = new CustomDialogAlert(DeviceProfileActivity.this);
                    alert.callFunction("경고", "연령을 입력해주세요..");
                    VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
                } else if (input_address.getText().toString().equals("")) {
                    CustomDialogAlert alert = new CustomDialogAlert(DeviceProfileActivity.this);
                    alert.callFunction("경고", "주소를 입력해주세요..");
                    VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
                } else { //인증 진행

//                    new Thread(()-> {
//                        Message message = null;
//                        try {
//                            boolean isSucceed = MyRequestUtility.deviceCredential(id, pw);
//
//                            if (isSucceed) {
//                                message = handler.obtainMessage(DeviceMessage.CREDENTIAL_SUCCEED.ordinal());
//                            } else {
//                                message = handler.obtainMessage(DeviceMessage.CREDENTIAL_FAIL.ordinal());
//                            }
//                        } catch (Exception e) {
//                            message = handler.obtainMessage(NetworkMessage.NETWORK_FAIL.ordinal());
//                            e.printStackTrace();
//                        }
//                        handler.sendMessage(message);
//                    }).start();
                }
            }
        });

    }

    private void setFilters(){

        InputFilter age_lengthFilter = new InputFilter.LengthFilter(3);
        InputFilter[] ageFilters = new InputFilter[] { new NumberFilter(), age_lengthFilter}; //입력 제한 필터
        input_age.setFilters(ageFilters);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_profile);

        final_id = DeviceInfo.tmpId;
        final_pw = DeviceInfo.tmpPw;

        ScreenManager.transparentStatusBar(this);

        loadComponents();
        setFilters();
        setComponentsEvent();
        setEffectObject();

    }



}