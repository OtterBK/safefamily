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
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import hanium.oldercare.oldercareservice.apinetwork.MyRequestUtility;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogAlert;
import hanium.oldercare.oldercareservice.handlermessage.NetworkMessage;
import hanium.oldercare.oldercareservice.handlermessage.RegisterMessage;
import hanium.oldercare.oldercareservice.inputfilter.EmailFilter;
import hanium.oldercare.oldercareservice.inputfilter.IDFilter;
import hanium.oldercare.oldercareservice.utility.VibrateUtility;

public class FindPWActivity extends AppCompatActivity {

    private TextView input_id;
    private TextView input_email;
    private Button btn_find_pw;

    private Vibrator vibrator;

    //백그라운드 작업 응답 처리에 사용할 메시지 핸들러
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg == null) return;

            boolean isVibrate = false;

            if(msg.what == RegisterMessage.NO_USER_INFO.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(FindPWActivity.this);
                alert.callFunction("비밀번호 찾기", "일치하는 가입정보가\n존재하지 않습니다.");
                isVibrate = true;
            } else if(msg.what == RegisterMessage.PASSWORD_RESET_SUCCEED.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(FindPWActivity.this);
                alert.callFunction("비밀번호 찾기", "입력하신 메일로 임시 비밀번호를 발송하였습니다.");
            } else if(msg.what == RegisterMessage.PASSWORD_RESET_FAIL.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(FindPWActivity.this);
                alert.callFunction("비밀번호 찾기", "비밀번호 초기화 요청에 실패했습니다.");
                isVibrate = true;
            } else if(msg.what == NetworkMessage.NETWORK_FAIL.ordinal()) {
                CustomDialogAlert alert = new CustomDialogAlert(FindPWActivity.this);
                alert.callFunction("연결 실패", "요청에 실패하였습니다.\n네트워크를 확인하거나\n잠시 후 다시 시도해주세요.");
                isVibrate = true;
            }

            if(isVibrate) VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
        }
    };


    private void loadComponents(){
        input_id = (EditText) findViewById(R.id.find_pw_input_id);
        input_email = (EditText) findViewById(R.id.find_pw_input_email);

        btn_find_pw = (Button) findViewById(R.id.find_pw_btn_find);
    }


    private void setFilters(){
        InputFilter id_lengthFilter = new InputFilter.LengthFilter(15);
        InputFilter[] idFilters = new InputFilter[] { new IDFilter(), id_lengthFilter}; //입력 제한 필터
        input_id.setFilters(idFilters);

        InputFilter[] emailFilters = new InputFilter[] { new EmailFilter()}; //입력 제한 필터
        input_email.setFilters(emailFilters);
    }

    private void setComponentsEvent(){
        btn_find_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(input_id.getText())){
                    CustomDialogAlert alert = new CustomDialogAlert(FindPWActivity.this);
                    alert.callFunction("경고", "아이디를 입력해주세요.");
                }else if(TextUtils.isEmpty(input_email.getText())){
                    CustomDialogAlert alert = new CustomDialogAlert(FindPWActivity.this);
                    alert.callFunction("경고", "이메일을 입력해주세요.");
                } else {

                    String id = input_id.getText().toString();
                    String email = input_email.getText().toString();
                    new Thread(new Runnable() {
                        public void run() {

                            Message message = null;

                            try{
                                String tmpID = MyRequestUtility.find_id(email); //이메일에 해당하는 id 값 받음
                                if(tmpID == null){
                                    message = handler.obtainMessage(RegisterMessage.NO_USER_INFO.ordinal());
                                } else {
                                    if(!tmpID.equals(id)){ //응답 id가 입력한 id와 동일하지 않다면
                                        message = handler.obtainMessage(RegisterMessage.NO_USER_INFO.ordinal());
                                    } else { //동일하다면
                                        if(MyRequestUtility.resetPassword(email)){ //해당 회원의 비밀번호 초기화 요청
                                            message = handler.obtainMessage(RegisterMessage.PASSWORD_RESET_SUCCEED.ordinal());
                                        } else {
                                            message = handler.obtainMessage(RegisterMessage.PASSWORD_RESET_FAIL.ordinal());
                                        }
                                    }
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
                VibrateUtility.errorVibrate(vibrator);
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };


        input_id.addTextChangedListener(inputEffectWatcher);
        input_email.addTextChangedListener(inputEffectWatcher);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        loadComponents();
        setFilters();
        setComponentsEvent();
        setEffectObject();
    }



}