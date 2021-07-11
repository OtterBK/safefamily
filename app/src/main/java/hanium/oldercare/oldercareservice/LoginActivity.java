package hanium.oldercare.oldercareservice;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    private TextView lbl_today;
    private static final String TYPEFACE_NAME = "NanumPen.otf";
    private Typeface typeface = null;
    private Button btn_login;
    private TextView btn_find_id;
    private TextView btn_find_pw;
    private TextView btn_register;

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

        btn_login = (Button) findViewById(R.id.find_id_btn_find);

        //버튼별 화면 이동 기능
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
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
                finish();
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
                finish();
            }
        });
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