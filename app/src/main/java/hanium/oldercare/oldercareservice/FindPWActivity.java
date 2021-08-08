package hanium.oldercare.oldercareservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
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

public class FindPWActivity extends AppCompatActivity {

    private TextView input_id;
    private TextView input_email;
    private Button btn_find_pw;


    //백그라운드 작업 응답 처리에 사용할 메시지 핸들러
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg == null) return;

            if(msg.what == RegisterMessage.NO_USER_INFO.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(FindPWActivity.this);
                alert.callFunction("비밀번호 찾기", "일치하는 가입정보가\n존재하지 않습니다.");
            } else if(msg.what == RegisterMessage.PASSWORD_RESET_SUCCEED.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(FindPWActivity.this);
                alert.callFunction("비밀번호 찾기", "입력하신 메일로 임시 비밀번호를 발송하였습니다.");
            } else if(msg.what == RegisterMessage.PASSWORD_RESET_FAIL.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(FindPWActivity.this);
                alert.callFunction("비밀번호 찾기", "비밀번호 초기화 요청에 실패했습니다.");
            } else if(msg.what == NetworkMessage.NETWORK_FAIL.ordinal()) {
                CustomDialogAlert alert = new CustomDialogAlert(FindPWActivity.this);
                alert.callFunction("연결 실패", "요청에 실패하였습니다.\n네트워크를 확인하거나\n잠시 후 다시 시도해주세요.");
            }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        loadComponents();
        setFilters();
        setComponentsEvent();
    }



}