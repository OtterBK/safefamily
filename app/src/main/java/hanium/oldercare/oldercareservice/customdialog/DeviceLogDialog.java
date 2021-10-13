package hanium.oldercare.oldercareservice.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import hanium.oldercare.oldercareservice.R;
import hanium.oldercare.oldercareservice.deviceutility.DeviceModel;

public class DeviceLogDialog extends AppCompatActivity {

    private Context context;
    private DeviceModel device;
    private Dialog dialog;

    public DeviceLogDialog(Context context) {

        this.context = context;

    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(DeviceModel device) {

        this.device = device;

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        dialog = dlg;

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_device_log);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();



        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        refreshLog(); //데이터 표시

//        titleView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 커스텀 다이얼로그를 종료한다.
//                dlg.dismiss();
//            }
//        });
//        descView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 커스텀 다이얼로그를 종료한다.
//                dlg.dismiss();
//            }
//        });

    }

    public void refreshLog(){

        final TableLayout tableLayout = (TableLayout) dialog.findViewById(R.id.device_log_table);

        Typeface typeFace = Typeface.createFromAsset(context.getAssets(),"lotte_dream.ttf"); //asset > fonts 폴더 내 폰트파일 적용

        JSONArray doorLog = device.getDoorLogs();
        for(int li = 0; li < doorLog.size(); li++){
            try{
                JSONArray log = (JSONArray)doorLog.get(li);

                TableRow tableRow = new TableRow(context);
                tableRow.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                TextView textView[] = new TextView[3];
                for(int i = 0; i < 3; i++){
                    textView[i] = new TextView(context);
                    textView[i].setGravity(Gravity.CENTER);
                    textView[i].setTextSize(16);
                    textView[i].setTypeface(typeFace);
                    tableRow.addView(textView[i]);
                }
                tableRow.setPadding(0,5,0,5);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf1 = new SimpleDateFormat("YYYY년 MM월 dd일");
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh시 mm분 ss초");

                JSONObject dateMap = (JSONObject) log.get(1);
                String dateStr = String.valueOf(dateMap.get("$date"));
                long date = Long.parseLong(dateStr);


                textView[0].setText(sdf1.format(date));
                textView[1].setText(sdf2.format(date));
                textView[2].setText(String.valueOf(log.get(2)));

                tableLayout.addView(tableRow);
            }catch (Exception e){
                e.printStackTrace();
            }

        }


    }

}