package hanium.oldercare.oldercareservice;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import hanium.oldercare.oldercareservice.apinetwork.MyRequestUtility;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogAlert;
import hanium.oldercare.oldercareservice.handlermessage.LoginMessage;
import hanium.oldercare.oldercareservice.handlermessage.NetworkMessage;
import hanium.oldercare.oldercareservice.handlermessage.RegisterMessage;
import hanium.oldercare.oldercareservice.inputfilter.EmailFilter;

public class FindIDActivity extends AppCompatActivity {

    private TextView input_email;
    private Button btn_find_id;

    private String responseID;


    //백그라운드 작업 응답 처리에 사용할 메시지 핸들러
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg == null) return;

            if(msg.what == RegisterMessage.FIND_ID_SUCCEED.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(FindIDActivity.this);
                alert.callFunction("아이디 찾기", "가입하신 아이디는\n"+responseID+"\n입니다.");
            } else if(msg.what == RegisterMessage.FIND_ID_FAIL.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(FindIDActivity.this);
                alert.callFunction("아이디 찾기", "일치하는 가입 정보가\n존재하지 않습니다.");
            } else if(msg.what == NetworkMessage.NETWORK_FAIL.ordinal()) {
                CustomDialogAlert alert = new CustomDialogAlert(FindIDActivity.this);
                alert.callFunction("연결 실패", "요청에 실패하였습니다.\n네트워크를 확인하거나\n잠시 후 다시 시도해주세요.");
            }
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);



        loadComponents();

        setFilters();

        setComponentsEvent();



    }



}