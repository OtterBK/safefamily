package hanium.oldercare.oldercareservice.utility;

import android.os.VibrationEffect;
import android.os.Vibrator;

public class VibrateUtility {

    public static void errorVibrate(Vibrator vibrator){
        vibrator.vibrate(VibrationEffect.createOneShot(250, 15));
    }

}
