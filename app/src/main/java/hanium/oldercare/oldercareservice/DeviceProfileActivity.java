package hanium.oldercare.oldercareservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.InputFilter;
import android.util.Log;
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
import hanium.oldercare.oldercareservice.info.ActivityInfo;
import hanium.oldercare.oldercareservice.info.DeviceInfo;
import hanium.oldercare.oldercareservice.info.LoginInfo;
import hanium.oldercare.oldercareservice.inputfilter.NumberFilter;
import hanium.oldercare.oldercareservice.utility.ScreenManager;
import hanium.oldercare.oldercareservice.utility.VibrateUtility;

public class DeviceProfileActivity extends AppCompatActivity {

    private Button btn_done;

    private TextView btn_edit_password;

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

            if(msg.what == DeviceMessage.EDIT_PROFILE_SUCCEED.ordinal()){
                Toast.makeText(DeviceProfileActivity.this, "디바이스를 등록하였습니다.", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                finish();
                ActivityInfo.homeActivity.refreshDeviceList();

            } else if(msg.what == DeviceMessage.EDIT_PROFILE_FAIL.ordinal() || msg.what == NetworkMessage.NETWORK_FAIL.ordinal()){

                CustomDialogAlert alert = new CustomDialogAlert(DeviceProfileActivity.this);
                alert.callFunction("변경 실패", "잠시 후 다시 시도해보세요.");

            }

            if(isVibrate) VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
        }
    };

    private void loadComponents(){
        btn_done = (Button) findViewById(R.id.device_profile_btn_done);
        btn_edit_password = (TextView) findViewById(R.id.device_profile_change_password);

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

                    String ward_name = input_name.getText().toString();
                    String ward_age = input_age.getText().toString();
                    String ward_address = input_address.getText().toString();
                    String ward_desc = input_desc.getText().toString();

                    CustomDialogLoading loading = new CustomDialogLoading(DeviceProfileActivity.this);
                    loading.callFunction();

                    new Thread(()->{

                        Message message = null;

                        try {
                            boolean isSucceed =
                                    MyRequestUtility.setDeviceProfile(final_id, final_pw, ward_name, ward_age, ward_address, ward_desc);

                            if (isSucceed) {
                                MyRequestUtility.deviceAdd(LoginInfo.ID, LoginInfo.PW, final_id, final_pw);
                                message = handler.obtainMessage(DeviceMessage.EDIT_PROFILE_SUCCEED.ordinal());
                            } else {
                                message = handler.obtainMessage(DeviceMessage.EDIT_PROFILE_FAIL.ordinal());
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

        btn_edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), DevicePasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

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