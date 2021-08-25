package hanium.oldercare.oldercareservice;

import android.content.Context;
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
import hanium.oldercare.oldercareservice.handlermessage.NetworkMessage;
import hanium.oldercare.oldercareservice.handlermessage.RegisterMessage;
import hanium.oldercare.oldercareservice.inputfilter.EmailFilter;
import hanium.oldercare.oldercareservice.utility.ScreenManager;
import hanium.oldercare.oldercareservice.utility.VibrateUtility;

public class FindIDActivity extends AppCompatActivity {

    private TextView input_email;
    private Button btn_find_id;

    private String responseID;

    private Vibrator vibrator;

    //백그라운드 작업 응답 처리에 사용할 메시지 핸들러
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg == null) return;

            boolean isVibrate = false;

            if(msg.what == RegisterMessage.FIND_ID_SUCCEED.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(FindIDActivity.this);
                alert.callFunction("아이디 찾기", "가입하신 아이디는\n"+responseID+"\n입니다.");
            } else if(msg.what == RegisterMessage.FIND_ID_FAIL.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(FindIDActivity.this);
                alert.callFunction("아이디 찾기", "일치하는 가입 정보가\n존재하지 않습니다.");
                isVibrate = true;
            } else if(msg.what == NetworkMessage.NETWORK_FAIL.ordinal()) {
                CustomDialogAlert alert = new CustomDialogAlert(FindIDActivity.this);
                alert.callFunction("연결 실패", "요청에 실패하였습니다.\n네트워크를 확인하거나\n잠시 후 다시 시도해주세요.");
                isVibrate = true;
            }

            if(isVibrate) VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
        }
    };


    private void loadComponents(){
        input_email = (TextView) findViewById(R.id.find_id_input_email);
        btn_find_id = (Button) findViewById(R.id.find_id_btn_find);
    }


    private void setFilters(){
        InputFilter[] emailFilters = new InputFilter[] { new EmailFilter()}; //입력 제한 필터
        input_email.setFilters(emailFilters);
    }

    private void setComponentsEvent(){
        btn_find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(input_email.getText())){
                    CustomDialogAlert alert = new CustomDialogAlert(FindIDActivity.this);
                    alert.callFunction("경고", "이메일을 입력해주세요.");
                    VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
                } else {

                    String email = input_email.getText().toString();
                    new Thread(new Runnable() {
                        public void run() {

                            Message message = null;

                            try{
                                String tmpID = MyRequestUtility.find_id(email);
                                if(tmpID == null){
                                    message = handler.obtainMessage(RegisterMessage.FIND_ID_FAIL.ordinal());
                                } else {
                                    FindIDActivity.this.responseID = tmpID;
                                    message = handler.obtainMessage(RegisterMessage.FIND_ID_SUCCEED.ordinal());
                                }
                            } catch (Exception e){
                                message = handler.obtainMessage(NetworkMessage.NETWORK_FAIL.ordinal());
                                e.printStackTrace();
                            }
                            handler.sendMessage(message);

                        }
                    }).start();

                }
            }
        });
    }

    private void setEffectObject(){
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        TextWatcher inputEffectWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력란에 변화가 있을 시 조치

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };


        input_email.addTextChangedListener(inputEffectWatcher);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        ScreenManager.transparentStatusBar(this);

        loadComponents();

        setFilters();

        setComponentsEvent();

        setEffectObject();

    }



}