package hanium.oldercare.oldercareservice.cardutility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeviceModel {

    private String device_id;
    private String ward_name;
    private String ward_age;
    private String ward_address;
    private String ward_description;

    private List<Map<String, String>> doorLogs = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> speakerLogs = new ArrayList<Map<String, String>>();



    private int status = 0; //0, 1, 2 각각 안전, 주의, 위험

    public DeviceModel(String device_id, String ward_name, String ward_age, String ward_address, String ward_description) {
        this.device_id = device_id;
        this.ward_name = ward_name;
        this.ward_age = ward_age;
        this.ward_address = ward_address;
        this.ward_description = ward_description;

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

    public List<Map<String, String>> getDoorLogs() {
        return doorLogs;
    }

    public void setDoorLogs(List<Map<String, String>> doorLogs) {
        this.doorLogs = doorLogs;
    }

    public List<Map<String, String>> getSpeakerLogs() {
        return speakerLogs;
    }

    public void setSpeakerLogs(List<Map<String, String>> speakerLogs) {
        this.speakerLogs = speakerLogs;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
