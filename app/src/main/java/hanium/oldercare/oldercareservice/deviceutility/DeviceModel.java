package hanium.oldercare.oldercareservice.deviceutility;

import android.widget.ImageView;
import android.widget.TextView;

import org.json.simple.JSONArray;


import hanium.oldercare.oldercareservice.R;
import hanium.oldercare.oldercareservice.apinetwork.MyRequestUtility;
import hanium.oldercare.oldercareservice.info.ActivityInfo;
import hanium.oldercare.oldercareservice.utility.MyNotificationManager;


public class DeviceModel {

    private String device_id;
    private String device_pw;
    private DangerLevel dangerLevel = DangerLevel.UNKNOWN;
    private int device_status_icon = R.drawable.status_unknown;
    private String ward_name;
    private String ward_age;
    private String ward_address;
    private String ward_description;
    private String door_count;
    private String speaker_count;

    private JSONArray doorLogs;
    private JSONArray speakerLogs;



    private ImageView comp_status_image;
    private TextView comp_ward_name;
    private TextView comp_door_count;
    private TextView comp_speaker_count;

    private boolean isCompSet = false;


    private int status = 1; //0, 1, 2 각각 연결실패, 안전, 주의, 위험

    public DeviceModel(String device_id, String device_pw) {
        this.device_id = device_id;
        this.device_pw = device_pw;
    }


    public void setComponent(ImageView comp_status_image, TextView comp_ward_name, TextView comp_door_count, TextView comp_speaker_count){
        this.comp_status_image = comp_status_image;
        this.comp_ward_name = comp_ward_name;
        this.comp_door_count = comp_door_count;
        this.comp_speaker_count = comp_speaker_count;
        isCompSet = true;

    }

    //데이터 새로 받아오기
    public void refreshData(){

        try {
            JSONArray deviceInfoArray = MyRequestUtility.getDeviceInfo(device_id,device_pw);
            if(deviceInfoArray != null){
                JSONArray deviceInfo = (JSONArray)deviceInfoArray.get(0);
                ward_name = String.valueOf(deviceInfo.get(0));
                ward_age = String.valueOf(deviceInfo.get(1));
                ward_address = String.valueOf(deviceInfo.get(2));
                ward_description = String.valueOf(deviceInfo.get(3));
            }

            JSONArray tmpDoorLogs = MyRequestUtility.getDoorLogs(device_id, device_pw);
            if(tmpDoorLogs != null){
                doorLogs = tmpDoorLogs;
                door_count = String.valueOf(doorLogs.size());

            }

            JSONArray tmpSpeakerLogs = MyRequestUtility.getSpeakerLogs(device_id, device_pw);
            if(tmpSpeakerLogs != null) {
                speakerLogs = tmpSpeakerLogs;
                speaker_count = String.valueOf(speakerLogs.size());
            }

            dangerLevel = DetectDanger.getDangerLevel(this);
            if(dangerLevel == DangerLevel.DANGER){
                MyNotificationManager.sendDangerNotification(ActivityInfo.homeActivity, ward_name); //위험 레벨일 시 알람
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    //컴포넌트에 값 재설정
    public void refreshComp(){

        if(!isCompSet) return;

        if(dangerLevel == DangerLevel.SAFE){
            device_status_icon = R.drawable.status_safe;
        } else if(dangerLevel == DangerLevel.WARN){
            device_status_icon = R.drawable.status_warn;
        } else if(dangerLevel == DangerLevel.DANGER){
            device_status_icon = R.drawable.status_danger;
        } else {
            device_status_icon = R.drawable.status_unknown;
        }

        comp_status_image.setImageResource(device_status_icon);
        comp_ward_name.setText(ward_name);
        comp_door_count.setText(door_count);
        comp_speaker_count.setText(speaker_count);
    }


    public String getDevice_id() {
        return device_id;
    }

    public String getDevice_pw() {
        return device_pw;
    }


    public int getDevice_status_icon() {
        return device_status_icon;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getWard_name() {
        return ward_name;
    }

    public void setWard_name(String ward_name) {
        this.ward_name = ward_name;
    }

    public String getWard_age() {
        return ward_age;
    }

    public void setWard_age(String ward_age) {
        this.ward_age = ward_age;
    }

    public String getWard_address() {
        return ward_address;
    }

    public void setWard_address(String ward_address) {
        this.ward_address = ward_address;
    }

    public String getWard_description() {
        return ward_description;
    }

    public void setWard_description(String ward_description) {
        this.ward_description = ward_description;
    }

    public JSONArray getDoorLogs() {
        return doorLogs;
    }

    public void setDoorLogs(JSONArray doorLogs) {
        this.doorLogs = doorLogs;
    }

    public JSONArray getSpeakerLogs() {
        return speakerLogs;
    }

    public void setSpeakerLogs(JSONArray speakerLogs) {
        this.speakerLogs = speakerLogs;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
