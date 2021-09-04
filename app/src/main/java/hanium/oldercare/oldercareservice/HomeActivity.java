package hanium.oldercare.oldercareservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hanium.oldercare.oldercareservice.cardutility.DeviceModel;
import hanium.oldercare.oldercareservice.cardutility.DeviceViewAdapter;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogAlert;
import hanium.oldercare.oldercareservice.info.RegisterInfo;
import hanium.oldercare.oldercareservice.inputfilter.PhoneFilter;
import hanium.oldercare.oldercareservice.utility.MyNotificationManager;
import hanium.oldercare.oldercareservice.utility.ScreenManager;
import hanium.oldercare.oldercareservice.utility.VibrateUtility;

public class HomeActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<DeviceModel> deviceList = new ArrayList<>();


    private Button btn_account_page;


    private Vibrator vibrator;

    private void loadComponents(){
        btn_account_page = (Button) findViewById(R.id.home_setting_account);

    }


    private void setEffectObject(){
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

    }

    private void setComponentsEvent(){
        //버튼별 화면 이동 기능
        btn_account_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyNotificationManager.createNotification(HomeActivity.this);
                //MyNotificationManager.sendAlarm(HomeActivity.this, "홍길동님의 이상 징후가 파악되었습니다.");

            }
        });
    }

    private void setFilters(){


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ScreenManager.transparentStatusBar(this);

        loadComponents();
        setFilters();
        setComponentsEvent();
        setEffectObject();



        mRecyclerView = (RecyclerView) findViewById(R.id.home_recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        deviceList.add(new DeviceModel("1","1234"));
        deviceList.add(new DeviceModel("1","1234"));
        deviceList.add(new DeviceModel("1","1234"));


        // specify an adapter (see also next example)
        mAdapter = new DeviceViewAdapter(deviceList);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void onBackPressed(){
        this.finish();
    }
}