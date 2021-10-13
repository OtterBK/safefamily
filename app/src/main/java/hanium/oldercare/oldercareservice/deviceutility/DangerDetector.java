package hanium.oldercare.oldercareservice.deviceutility;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Callable;

import hanium.oldercare.oldercareservice.utility.MyNotificationManager;

public class DangerDetector {

    final public static int DANGER_HOUR = 24;

    public static DangerLevel getDangerLevel(DeviceModel device){

        DangerLevel returnLevel = DangerLevel.UNKNOWN;

        JSONArray doorLog = device.getDoorLogs();

        if(doorLog.size() != 0){
            JSONArray lastLog = (JSONArray)doorLog.get(doorLog.size()-1);


            Calendar calendar = Calendar.getInstance();


            JSONObject dateMap = (JSONObject) lastLog.get(1);
            String dateStr = String.valueOf(dateMap.get("$date"));
            long utcDate = Long.parseLong(dateStr);

            Calendar tmpCalendar = Calendar.getInstance();
            tmpCalendar.setTimeInMillis(utcDate);
            tmpCalendar.add(Calendar.HOUR, -9);
            long lastDate = tmpCalendar.getTimeInMillis();

            long now = calendar.getTimeInMillis();

            long duration_l = now - lastDate;

            Calendar duration = Calendar.getInstance();
            duration.setTimeInMillis(duration_l);

            int duration_h = (duration.get(Calendar.DAY_OF_YEAR)-1) * 24 + duration.get(Calendar.HOUR_OF_DAY);
            Logger.i(duration_h+"");

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
