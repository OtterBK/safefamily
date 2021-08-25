package hanium.oldercare.oldercareservice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import hanium.oldercare.oldercareservice.apinetwork.MyRequestUtility;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogAlert;
import hanium.oldercare.oldercareservice.handlermessage.LoginMessage;
import hanium.oldercare.oldercareservice.handlermessage.NetworkMessage;
import hanium.oldercare.oldercareservice.info.LoginInfo;
import hanium.oldercare.oldercareservice.inputfilter.IDFilter;
import hanium.oldercare.oldercareservice.inputfilter.PWFilter;
import hanium.oldercare.oldercareservice.utility.ScreenManager;
import hanium.oldercare.oldercareservice.utility.VibrateUtility;

public class LoginSelectActivity extends AppCompatActivity {

    private Button btn_register;
    private Button btn_login;

    private void loadComponents(){
        btn_register = (Button) findViewById(R.id.select_login_btn_register);
        btn_login = (Button) findViewById(R.id.select_login_btn_login);
    }


    private void setComponentsEvent(){

        //버튼별 화면 이동 기능
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        //버튼별 화면 이동 기능
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_login);

        ScreenManager.transparentStatusBar(this);

        loadComponents();

        setComponentsEvent();


    }

}