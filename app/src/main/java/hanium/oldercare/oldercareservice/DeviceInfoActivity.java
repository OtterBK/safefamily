package hanium.oldercare.oldercareservice;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import hanium.oldercare.oldercareservice.apinetwork.MyRequestUtility;
import hanium.oldercare.oldercareservice.cardutility.DeviceModel;
import hanium.oldercare.oldercareservice.cardutility.DeviceViewAdapter;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogAlert;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogConfirm;
import hanium.oldercare.oldercareservice.customdialog.CustomDialogLoading;
import hanium.oldercare.oldercareservice.customdialog.DeviceInfoDialog;
import hanium.oldercare.oldercareservice.customdialog.DeviceLogDialog;
import hanium.oldercare.oldercareservice.handlermessage.DeviceMessage;
import hanium.oldercare.oldercareservice.handlermessage.NetworkMessage;
import hanium.oldercare.oldercareservice.info.ActivityInfo;
import hanium.oldercare.oldercareservice.info.DeviceInfo;
import hanium.oldercare.oldercareservice.info.LoginInfo;
import hanium.oldercare.oldercareservice.utility.ScreenManager;
import hanium.oldercare.oldercareservice.utility.VibrateUtility;

public class DeviceInfoActivity extends AppCompatActivity {

    //백그라운드 작업 응답 처리에 사용할 메시지 핸들러
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg == null) return;

            boolean isVibrate = false;

            if(msg.what == DeviceMessage.DELETE_DEVICE_SUCCEED.ordinal()){
                Toast.makeText(DeviceInfoActivity.this, "디바이스를 삭제하였습니다.", Toast.LENGTH_LONG).show();

                DeviceInfoActivity.this.finish();
                ActivityInfo.homeActivity.refreshDeviceList();

            } else if(msg.what == DeviceMessage.DELETE_DEVICE_FAIL.ordinal() || msg.what == NetworkMessage.NETWORK_FAIL.ordinal()){

                CustomDialogAlert alert = new CustomDialogAlert(DeviceInfoActivity.this);
                alert.callFunction("삭제 실패", "잠시 후 다시 시도해보세요.");

            }

            if(isVibrate) VibrateUtility.errorVibrate(vibrator); //오류시 진동효과
        }
    };

    private Button btn_back;
    private Button btn_log;
    private Button btn_device_edit;
    private Button btn_device_delete;

    private DeviceModel device;


    private Vibrator vibrator;

    private void loadComponents(){
        btn_back = (Button) findViewById(R.id.deviceInfo_btn_back);
        btn_log = (Button) findViewById(R.id.deviceInfo_btn_log);
        btn_device_edit = (Button) findViewById(R.id.deviceInfo_btn_device_edit);
        btn_device_delete = (Button) findViewById(R.id.deviceInfo_btn_device_delete);
    }


    private void setEffectObject(){
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
    }

    private void setComponentsEvent(){
        //버튼별 화면 이동 기능
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeviceInfoActivity.this.finish();
            }
        });

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DeviceLogDialog logDialog = new DeviceLogDialog(DeviceInfoActivity.this);
                logDialog.callFunction(device);

            }
        });

        btn_device_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), DeviceProfileActivity.class);
                intent.putExtra("isEditMode", true);

                DeviceInfo.infoDevice = device;
                DeviceInfo.infoActivity = DeviceInfoActivity.this;

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.none);

            }
        });

        btn_device_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogConfirm dialogConfirm = new CustomDialogConfirm(DeviceInfoActivity.this);

                Runnable okFunction = ()->{
                    CustomDialogLoading loading = new CustomDialogLoading(DeviceInfoActivity.this);
                    loading.callFunction();

                    Thread thread = new Thread(()->{

                        Message message = null;

                        try {

                            boolean isSucceed =
                                    MyRequestUtility.deviceDelete(LoginInfo.ID, LoginInfo.PW, device.getDevice_id());

                            if (isSucceed) {
                                message = handler.obtainMessage(DeviceMessage.DELETE_DEVICE_SUCCEED.ordinal());
                            } else {
                                message = handler.obtainMessage(DeviceMessage.DELETE_DEVICE_FAIL.ordinal());
                            }

                        } catch (Exception e) {
                            message = handler.obtainMessage(NetworkMessage.NETWORK_FAIL.ordinal());
                            e.printStackTrace();
                        } finally {
                            loading.dismiss();
                        }
                        handler.sendMessage(message);


                    });
                    thread.start();
                };

                dialogConfirm.callFunction("삭제 확인", "정말로 디바이스를 삭제하시겠습니까?",okFunction);
            }
        });
    }

    private void setFilters(){


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        ScreenManager.transparentStatusBar(this);

        loadComponents();
        setFilters();
        setComponentsEvent();
        setEffectObject();

        device = DeviceInfo.infoDevice;


        refreshInfo(); //디바이스 목록 갱신

    }

    public void refreshInfo(){
        Activity context = DeviceInfoActivity.this;

        final TextView deviceNum = (TextView) context.findViewById(R.id.deviceInfo_text_deviceNum);
        deviceNum.setText(device.getDevice_id()+"번 디바이스");

        final TextView wardName = (TextView) context.findViewById(R.id.deviceInfo_text_targetName);
        wardName.setText(device.getWard_name());

        final TextView wardAge = (TextView) context.findViewById(R.id.deviceInfo_text_targetAge);
        wardAge.setText(device.getWard_age() + "세");

        final TextView wardAddress = (TextView) context.findViewById(R.id.deviceInfo_text_targetAddress);
        wardAddress.setText(device.getWard_address());

        final TextView desc = (TextView) context.findViewById(R.id.deviceInfo_text_targetDesc);
        desc.setText(device.getWard_description());



    }

    public void onBackPressed(){
        this.finish();
    }
}