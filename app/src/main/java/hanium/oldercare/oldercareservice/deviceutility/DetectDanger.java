package hanium.oldercare.oldercareservice.deviceutility;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;

import hanium.oldercare.oldercareservice.utility.MyNotificationManager;

public class DetectDanger {

    final public static int DANGER_HOUR = 24;

    public static DangerLevel getDangerLevel(DeviceModel device){

        DangerLevel returnLevel = DangerLevel.UNKNOWN;

        JSONArray doorLog = device.getDoorLogs();

        if(doorLog.size() != 0){
            JSONArray lastLog = (JSONArray)doorLog.get(doorLog.size()-1);


            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("YYYY년 MM월 dd일");
            SimpleDateFormat sdf2 = new SimpleDateFormat("hh시 mm분 ss초");

            JSONObject dateMap = (JSONObject) lastLog.get(1);
            String dateStr = String.valueOf(dateMap.get("$date"));
            long lastDate = Long.parseLong(dateStr);

            long now = calendar.getTimeInMillis();

            long duration_l = now - lastDate;

            Calendar duration = Calendar.getInstance();
            duration.setTimeInMillis(duration_l);

            int duration_h = duration.get(Calendar.DAY_OF_YEAR) * 24 + duration.get(Calendar.HOUR_OF_DAY);

            if(duration_h >= DANGER_HOUR){
                returnLevel = DangerLevel.DANGER;
            } else if(duration_h >= DANGER_HOUR / 2) {
                returnLevel = DangerLevel.WARN;
            } else {
                returnLevel = DangerLevel.SAFE;
            }

        }

        return  returnLevel;

    }

}
