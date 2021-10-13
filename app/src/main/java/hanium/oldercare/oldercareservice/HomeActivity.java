package hanium.oldercare.oldercareservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import org.json.simple.JSONArray;

import java.util.ArrayList;

import hanium.oldercare.oldercareservice.apinetwork.MyRequestUtility;
import hanium.oldercare.oldercareservice.deviceutility.DeviceModel;
import hanium.oldercare.oldercareservice.deviceutility.DeviceViewAdapter;
import hanium.oldercare.oldercareservice.handlermessage.DeviceMessage;
import hanium.oldercare.oldercareservice.info.ActivityInfo;
import hanium.oldercare.oldercareservice.info.LoginInfo;
import hanium.oldercare.oldercareservice.utility.ScreenManager;
import hanium.oldercare.oldercareservice.utility.VibrateUtility;

public class HomeActivity extends AppCompatActivity {

    //백그라운드 작업 응답 처리에 사용할 메시지 핸들러
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg == null) return;

            boolean isVibrate = false;

            if(msg.what == DeviceMessage.REFRESH_DEVICE_READAPT.ordinal()){
                
                if(isActivityStopped){ //액티비티 비활성화 중이면
                    for(DeviceModel device : deviceList){ //이상 징후 파악을 위해 데이터만 재요청
                        Thread refreshThread = new Thread(()->{
                            device.refreshData();    
                        });
                        refreshThread.start();
                    }
                } else { //액티비티 활성화 중이면
                    mAdapter = new DeviceViewAdapter(deviceList, HomeActivity.this, handler);
                    mRecyclerView.setAdapter(mAdapter); //어댑터 재생성
                }
                
            } else if(msg.what == DeviceMessage.REFRESH_DEVICE_COMP.ordinal()){
                int deviceIndex = msg.arg1;
                DeviceModel deviceModel = deviceList.get(deviceIndex);

                deviceModel.refreshComp();
            }

            if(isVibrate) VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
        }
    };

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<DeviceModel> deviceList = new ArrayList<>();
    private int bindCheckCount = 0;


    private Button btn_account_page;
    private Button btn_device_add;

    private boolean alreadyRefreshing;
    private boolean isActivityStopped;

    private Vibrator vibrator;

    private Thread refreshSchedulerThread;

    private void loadComponents(){
        btn_account_page = (Button) findViewById(R.id.home_setting_account);
        btn_device_add = (Button) findViewById(R.id.home_device_add);
    }


    private void setEffectObject(){
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

    }

    private void setComponentsEvent(){
        //버튼별 화면 이동 기능
        btn_account_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AccountManageActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                overridePendingTransition(R.anim.slide_in_right, R.anim.none);

//                MyNotificationManager.createNotification(HomeActivity.this);
                //MyNotificationManager.sendAlarm(HomeActivity.this, "홍길동님의 이상 징후가 파악되었습니다.");

            }
        });

        btn_device_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DeviceAddActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                overridePendingTransition(R.anim.slide_in_right, R.anim.none);

//                MyNotificationManager.createNotification(HomeActivity.this);
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

        ActivityInfo.homeActivity = this; //홈 화면은 하나만 써서 계속 재활용

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

        refreshScheduler(); //디바이스 목록 갱신 스케줄러 실행

    }

    @Override
    public void onStop(){
        super.onStop();
        isActivityStopped = true;
    }

    @Override
    public void onResume(){
        super.onResume();
        isActivityStopped = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refreshSchedulerThread.interrupt();
    }

    public void refreshScheduler(){
        refreshSchedulerThread = new Thread(){
            public void run(){
                boolean stopFlag = false;

                try{

                    while(!stopFlag){

                        if(alreadyRefreshing) {
                            alreadyRefreshing = false;
                            continue;
                        }
                        refreshDeviceList();
                        Thread.sleep(30000); //30초마다 1번 수행
                    }

                } catch (InterruptedException exc){
                    stopFlag = true;
                } catch (Exception exc){
                    exc.printStackTrace();
                }
            }
        };
        refreshSchedulerThread.start();
    }

    public void refreshDevice(){

        Thread refreshThread = new Thread(){
            public void run(){
                try{

                    for(DeviceModel deviceModel : deviceList){
                        deviceModel.refreshData();
                    }

                    for(int i = 0; i < deviceList.size(); i++){
                        final int deviceIndex = i;

                        Message handleMsg = handler.obtainMessage(DeviceMessage.REFRESH_DEVICE_COMP.ordinal());
                        handleMsg.arg1 = deviceIndex;
                        handler.sendMessage(handleMsg);

                    }


                }catch (Exception exc){
                    exc.printStackTrace();
                }
            }
        };
        refreshThread.start();

    }

    public void refreshDeviceList(){

        //디바이스 목록 다시 불러와서 리스트 구성
        Thread refreshThread = new Thread(){
            public void run(){
                try{
                    alreadyRefreshing = true;

                    deviceList.clear();

                    JSONArray deviceArray = MyRequestUtility.getDeviceList(LoginInfo.ID, LoginInfo.PW);

                    for(int i = 0; i < deviceArray.size(); i++){
                        JSONArray tmpDeviceId = (JSONArray) deviceArray.get(i);
                        String deviceId = String.valueOf(tmpDeviceId.get(0));
                        String devicePw = String.valueOf(tmpDeviceId.get(1));
                        
                        if(MyRequestUtility.deviceCredential(deviceId, devicePw)){ //인증 성공 디바이스만 추가
                            deviceList.add(new DeviceModel(deviceId,devicePw));    
                        }
                    }


                    // specify an adapter (see also next example)
                    Message handleMsg = handler.obtainMessage(DeviceMessage.REFRESH_DEVICE_READAPT.ordinal());
                    handler.sendMessage(handleMsg);

                }catch (Exception exc){
                    exc.printStackTrace();
                }finally {
                    alreadyRefreshing = false;
                }
            }
        };
        refreshThread.start();

    }

    public void onBackPressed(){
        this.finish();
    }
}