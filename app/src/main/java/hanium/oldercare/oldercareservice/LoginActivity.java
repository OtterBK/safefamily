package hanium.oldercare.oldercareservice;

import android.content.Intent;
import android.graphics.Color;
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
import hanium.oldercare.oldercareservice.inputfilter.IDFilter;
import hanium.oldercare.oldercareservice.inputfilter.PWFilter;

public class LoginActivity extends AppCompatActivity {

    private TextView lbl_today;
    private static final String TYPEFACE_NAME = "NanumPen.otf";
    private Typeface typeface = null;
    private Button btn_login;
    private TextView btn_find_id;
    private TextView btn_find_pw;
    private TextView btn_register;
    private TextView input_id;
    private TextView input_pw;


    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg == null) return;

            if(msg.what == LoginMessage.LOGIN_FAIL.ordinal()){
                CustomDialogAlert alert = new CustomDialogAlert(LoginActivity.this);
                alert.callFunction("로그인 실패", "존재하지 않는 아이디이거나,\n 잘못된 비밀번호입니다.");
            } else if(msg.what == LoginMessage.LOGIN_SUCCEED.ordinal()){
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            } else if(msg.what == NetworkMessage.NETWORK_FAIL.ordinal()) {
                CustomDialogAlert alert = new CustomDialogAlert(LoginActivity.this);
                alert.callFunction("연결 실패", "요청에 실패하였습니다.\n네트워크를 확인하거나\n잠시 후 다시 시도해주세요.");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTypeface();
        setContentView(R.layout.activity_login);

        lbl_today = (TextView) findViewById(R.id.login_lbl_today); //오늘 날짜 설정

        String dayString = "";

        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY) < 10) calendar.add(Calendar.DATE, -1);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int dow = calendar.get(Calendar.DAY_OF_WEEK);


        if(month < 10){ //한자리 수라면
            dayString += "0";
        }
        dayString += month + ". ";

        if(day < 10){ //한자리 수라면
            dayString += "0";
        }
        dayString += day + ". ";

        String dayName = "일";
        switch(dow){
            case 1: dayName = "일"; break;
            case 2: dayName = "월"; break;
            case 3: dayName = "화"; break;
            case 4: dayName = "수"; break;
            case 5: dayName = "목"; break;
            case 6: dayName = "금"; break;
            case 7: dayName = "토"; break;

            default: dayName = "일";
        }

        dayString += "("+dayName+")";

        lbl_today.setText(dayString);

        btn_login = (Button) findViewById(R.id.login_btn_login);

        //버튼별 화면 이동 기능
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(input_id.getText())){
                    CustomDialogAlert alert = new CustomDialogAlert(LoginActivity.this);
                    alert.callFunction("경고", "아이디를 입력해주세요.");
                } else if(TextUtils.isEmpty(input_pw.getText())){
                    CustomDialogAlert alert = new CustomDialogAlert(LoginActivity.this);
                    alert.callFunction("경고", "비밀번호를 입력해주세요.");
                } else {
                    String id = input_id.getText().toString();
                    String pw = input_pw.getText().toString();


                    new Thread(new Runnable() {
                        public void run() {
                            Message message = null;
                            try {
                                if(MyRequestUtility.instantLogin(id,pw)){
                                    message = handler.obtainMessage(LoginMessage.LOGIN_SUCCEED.ordinal());
                                } else {
                                    message = handler.obtainMessage(LoginMessage.LOGIN_FAIL.ordinal());
                                }
                            } catch (Exception e) {
                                message = handler.obtainMessage(NetworkMessage.NETWORK_FAIL.ordinal());
                                e.printStackTrace();
                            }
                            handler.sendMessage(message);
                        }
                    }).start();
                }


            }
        });

        btn_find_id = (TextView) findViewById(R.id.login_lbl_find_id);

        //버튼별 화면 이동 기능
        btn_find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindIDActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        btn_find_pw = (TextView) findViewById(R.id.login_lbl_find_pw);

        //버튼별 화면 이동 기능
        btn_find_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindPWActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        btn_register = (TextView) findViewById(R.id.login_lbl_register);

        //버튼별 화면 이동 기능
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        // 입력필터 적용
        input_id = (TextView) findViewById(R.id.login_input_id);

        InputFilter id_lengthFilter = new InputFilter.LengthFilter(15);
        InputFilter[] idFilters = new InputFilter[] { new IDFilter(), id_lengthFilter}; //입력 제한 필터
        input_id.setFilters(idFilters);

        input_pw = (TextView) findViewById(R.id.login_input_pw);

        InputFilter pw_lengthFilter = new InputFilter.LengthFilter(15);
        InputFilter[] pwFilters = new InputFilter[] { new PWFilter(), pw_lengthFilter}; //입력 제한 필터
        input_pw.setFilters(pwFilters);


    }

    private void loadTypeface(){
        if(typeface==null)
            typeface = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME);
    }

    @Override
    public void setContentView(int viewId) {
        View view = LayoutInflater.from(this).inflate(viewId, null);
        ViewGroup group = (ViewGroup)view;
        int childCnt = group.getChildCount();
        for(int i=0; i<childCnt; i++){
            View v = group.getChildAt(i);
            if(v instanceof TextView){
                ((TextView)v).setTypeface(typeface);
            }
        }
        super.setContentView(view);
    }


}