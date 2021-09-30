package hanium.oldercare.oldercareservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import hanium.oldercare.oldercareservice.customdialog.CustomDialogAlert;
import hanium.oldercare.oldercareservice.info.RegisterInfo;
import hanium.oldercare.oldercareservice.inputfilter.PhoneFilter;
import hanium.oldercare.oldercareservice.utility.ScreenManager;
import hanium.oldercare.oldercareservice.utility.VibrateUtility;

public class ProfileInputActivity extends AppCompatActivity {

    private Button btn_next;
    private TextView nickName;
    private TextView address; //생각해보니 주소는 필요없을 것 같아서 뺌
    private TextView phone;

    private Vibrator vibrator;


    //백그라운드 작업 응답 처리에 사용할 메시지 핸들러
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg == null) return;

            boolean isVibrate = false;

            if(isVibrate) VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
        }
    };

    private void loadComponents(){
        btn_next = (Button) findViewById(R.id.profileInput_btn_do_crendent);

        nickName = (TextView) findViewById(R.id.profileInput_name);
//        address = (TextView) findViewById(R.id.profileInput_address);
        phone = (TextView) findViewById(R.id.profileInput_phone);
    }


    private void setEffectObject(){
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

    }

    private void setComponentsEvent(){
        //버튼별 화면 이동 기능
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(nickName.getText())){
                    CustomDialogAlert alert = new CustomDialogAlert(ProfileInputActivity.this);
                    alert.callFunction("경고", "이름을 입력해주세요.");
                    VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
                }  else if(TextUtils.isEmpty(phone.getText())){
                    CustomDialogAlert alert = new CustomDialogAlert(ProfileInputActivity.this);
                    alert.callFunction("경고", "전화번호를 입력해주세요.");
                    VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
                } else { //비번이랑 비번체크 일치 시 다음 단계로
                    RegisterInfo.nickName = nickName.getText().toString();
                    RegisterInfo.phone = phone.getText().toString();

                    Intent intent = new Intent(getApplicationContext(), EmailVerifyActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }

            }
        });
    }

    private void setFilters(){
        InputFilter nickName_lengthFilter = new InputFilter.LengthFilter(15);
        InputFilter[] nickNameFilters = new InputFilter[] { nickName_lengthFilter}; //입력 제한 필터
        nickName.setFilters(nickNameFilters);

        InputFilter phone_lengthFilter = new InputFilter.LengthFilter(13);
        InputFilter[] phoneFilters = new InputFilter[] { new PhoneFilter(), phone_lengthFilter}; //입력 제한 필터
        phone.setFilters(phoneFilters);

        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileinput);

        ScreenManager.transparentStatusBar(this);

        loadComponents();
        setFilters();
        setComponentsEvent();
        setEffectObject();

    }



}