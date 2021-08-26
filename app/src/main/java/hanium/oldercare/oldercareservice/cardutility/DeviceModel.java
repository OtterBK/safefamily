package hanium.oldercare.oldercareservice.cardutility;

import android.os.Message;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hanium.oldercare.oldercareservice.apinetwork.MyRequestUtility;
import hanium.oldercare.oldercareservice.handlermessage.RegisterMessage;

public class DeviceModel {

    private String device_id;
    private String device_pw;
    private String ward_name;
    private String ward_age;
    private String ward_address;
    private String ward_description;

    private ArrayList<HashMap<String, String>> doorLogs = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> speakerLogs = new ArrayList<HashMap<String, String>>();



    private int status = 1; //0, 1, 2 각각 연결실패, 안전, 주의, 위험

    public DeviceModel(String device_id, String device_pw) {
        this.device_id = device_id;
        this.device_pw = device_pw;

        refreshData();
    }

    //데이터 새로 받아오기
    public void refreshData(){

        new Thread(new Runnable() {
            public void run() {

                try {
                    JSONObject deviceInfo = MyRequestUtility.getDeviceInfo(device_id,device_pw);
                    if(deviceInfo != null){
                        ward_name = (String)deviceInfo.get("ward_name");
                        ward_age = (String)deviceInfo.get("ward_age");
                        ward_address = (String)deviceInfo.get("ward_address");
                        ward_description = (String)deviceInfo.get("ward_description");
                    }

                    ArrayList<HashMap<String, String>> tmpDoorLogs = MyRequestUtility.getDoorLogs(device_id, device_pw);
                    if(tmpDoorLogs != null){
                        doorLogs = tmpDoorLogs;
                    }

                    ArrayList<HashMap<String, String>> tmpSpeakerLogs = MyRequestUtility.getSpeakerLogs(device_id, device_pw);
                    if(tmpSpeakerLogs != null){
                        speakerLogs = tmpSpeakerLogs;
                    }


                } catch (Exception e) {
                    return;
                }
            }
        }).start();


    }


    public String getDevice_id() {
        return device_id;
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

    public ArrayList<HashMap<String, String>> getDoorLogs() {
        return doorLogs;
    }

    public void setDoorLogs(ArrayList<HashMap<String, String>> doorLogs) {
        this.doorLogs = doorLogs;
    }

    public ArrayList<HashMap<String, String>> getSpeakerLogs() {
        return speakerLogs;
    }

    public void setSpeakerLogs(ArrayList<HashMap<String, String>> speakerLogs) {
        this.speakerLogs = speakerLogs;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
