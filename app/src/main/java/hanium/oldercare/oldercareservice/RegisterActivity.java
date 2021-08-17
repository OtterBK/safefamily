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
import hanium.oldercare.oldercareservice.handlermessage.RegisterMessage;
import hanium.oldercare.oldercareservice.info.RegisterInfo;
import hanium.oldercare.oldercareservice.inputfilter.IDFilter;
import hanium.oldercare.oldercareservice.inputfilter.PWFilter;
import hanium.oldercare.oldercareservice.utility.VibrateUtility;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_next;
    private TextView input_id;
    private TextView input_pw;
    private TextView input_pwCheck;
    private TextView lbl_warn_id;
    private TextView lbl_warn_pw;

    private String id_final = ""; //허용된 아이디
    private String pw_final = ""; //허용된 비번

    private Vibrator vibrator;



    //백그라운드 작업 응답 처리에 사용할 메시지 핸들러
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg == null) return;

            boolean isVibrate = false;

            if(msg.what == RegisterMessage.CAN_USE_ID.ordinal()){
                lbl_warn_id.setTextColor(Color.BLUE);
                lbl_warn_id.setText("사용 가능한 아이디입니다.");
                id_final = input_id.getText().toString();
            } else if(msg.what == RegisterMessage.ALREADY_USE_ID.ordinal()) {
                lbl_warn_id.setTextColor(Color.RED);
                lbl_warn_id.setText("이미 존재하는 아이디입니다.");
            } else if(msg.what == RegisterMessage.CANNOT_USE_ID.ordinal()) {
                lbl_warn_id.setTextColor(Color.RED);
                lbl_warn_id.setText("사용할 수 없는 아이디입니다.");
            } else if(msg.what == RegisterMessage.ID_LENGTH_LOW.ordinal()) {
                lbl_warn_id.setTextColor(Color.RED);
                lbl_warn_id.setText("아이디는 5글자 이상 입력하세요.");
            } else if(msg.what == RegisterMessage.ID_INIT.ordinal()) {
                lbl_warn_id.setTextColor(Color.RED);
                lbl_warn_id.setText("");
            }

            if(isVibrate) VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
        }
    };

    private void loadComponents(){
        btn_next = (Button) findViewById(R.id.register_btn_next);

        input_id = (TextView) findViewById(R.id.register_input_id);
        input_pw = (TextView) findViewById(R.id.register_input_pw);
        input_pwCheck = (TextView) findViewById(R.id.register_input_pwcheck);
        lbl_warn_id = (TextView) findViewById(R.id.register_id_warn);
        lbl_warn_pw = (TextView) findViewById(R.id.register_pw_warn);
    }

    private void setEffectObject(){
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

    }

    private void setComponentsEvent(){
        //버튼별 화면 이동 기능
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(id_final.equals("")){
                    CustomDialogAlert alert = new CustomDialogAlert(RegisterActivity.this);
                    alert.callFunction("경고", "아이디를 확인해주세요.");
                    VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
                } else if(pw_final.equals("")){
                    CustomDialogAlert alert = new CustomDialogAlert(RegisterActivity.this);
                    alert.callFunction("경고", "비밀번호를 확인해주세요.");
                    VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
                }else { //비번이랑 비번체크 일치 시 다음 단계로
                    RegisterInfo.tmpId = id_final;
                    RegisterInfo.tmpPw = pw_final;

                    Intent intent = new Intent(getApplicationContext(), EmailVerifyActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                }

            }
        });

        input_id.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){ //포커스가 풀렸을 때

                    TextView myView = (TextView) v;

                    String id = myView.getText().toString();

                    if(id.length() < 5){ //5글자 미만일 시
                        Message message = handler.obtainMessage(RegisterMessage.ID_LENGTH_LOW.ordinal());
                        handler.sendMessage(message);
                    } else {
                        new Thread(new Runnable() {
                            public void run() {
                                Message message = null;
                                try {
                                    int idCnt = MyRequestUtility.getCompareIdAmount(id);

                                    if (idCnt > 0) {
                                        message = handler.obtainMessage(RegisterMessage.ALREADY_USE_ID.ordinal());
                                    } else {
                                        message = handler.obtainMessage(RegisterMessage.CAN_USE_ID.ordinal());
                                    }
                                } catch (Exception e) {
                                    message = handler.obtainMessage(RegisterMessage.CANNOT_USE_ID.ordinal());
                                    e.printStackTrace();
                                }
                                handler.sendMessage(message);
                            }
                        }).start();
                    }
                } else { //포커스 됐을 때
                    Message message = handler.obtainMessage(RegisterMessage.ID_INIT.ordinal());
                    handler.sendMessage(message);
                }
            }
        });

        input_pw.addTextChangedListener(new TextWatcher(){

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력란에 변화가 있을 시 조치
                if(TextUtils.isEmpty(input_pw.getText()) || input_pw.getText().length() < 8) {
                    lbl_warn_pw.setTextColor(Color.RED);
                    lbl_warn_pw.setText("비밀번호는 8자 이상이여야합니다.");
                } else if(!TextUtils.isEmpty(input_pwCheck.getText())){
                    if(!input_pw.getText().toString().equals(input_pwCheck.getText().toString())){
                        lbl_warn_pw.setTextColor(Color.RED);
                        lbl_warn_pw.setText("비밀번호가 일치하지 않습니다.");
                    } else {
                        lbl_warn_pw.setTextColor(Color.BLUE);
                        lbl_warn_pw.setText("비밀번호가 일치합니다.");
                        pw_final = input_pw.getText().toString();
                    }
                } else {
                    lbl_warn_pw.setTextColor(Color.BLUE);
                    lbl_warn_pw.setText("");
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

        input_pwCheck.addTextChangedListener(new TextWatcher(){

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력난에 변화가 있을 시 조치
                if(TextUtils.isEmpty(input_pw.getText()) || input_pw.getText().length() < 8) {
                    lbl_warn_pw.setTextColor(Color.RED);
                    lbl_warn_pw.setText("비밀번호는 8자 이상이여야합니다.");
                } else if(!TextUtils.isEmpty(input_pw.getText())){
                    if(!input_pw.getText().toString().equals(input_pwCheck.getText().toString())){
                        lbl_warn_pw.setTextColor(Color.RED);
                        lbl_warn_pw.setText("비밀번호가 일치하지 않습니다.");
                    } else {
                        lbl_warn_pw.setTextColor(Color.BLUE);
                        lbl_warn_pw.setText("비밀번호가 일치합니다.");
                        pw_final = input_pw.getText().toString();
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

    private void setFilters(){
        InputFilter id_lengthFilter = new InputFilter.LengthFilter(15);
        InputFilter[] idFilters = new InputFilter[] { new IDFilter(), id_lengthFilter}; //입력 제한 필터
        input_id.setFilters(idFilters);


        InputFilter pw_lengthFilter = new InputFilter.LengthFilter(15);
        InputFilter[] pwFilters = new InputFilter[] { new PWFilter(), pw_lengthFilter}; //입력 제한 필터
        input_pw.setFilters(pwFilters);
        input_pwCheck.setFilters(pwFilters);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        loadComponents();
        setFilters();
        setComponentsEvent();
        setEffectObject();

    }



}