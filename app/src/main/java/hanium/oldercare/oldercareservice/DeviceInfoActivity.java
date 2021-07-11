package hanium.oldercare.oldercareservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DeviceInfoActivity extends AppCompatActivity {

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        refreshLog();
    }

    public void refreshLog(){
        tableLayout = (TableLayout) findViewById(R.id.deviceInfo_table);
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                            ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView textView[] = new TextView[3];
        for(int i = 0; i < 3; i++){
            textView[i] = new TextView(this);
            textView[i].setGravity(Gravity.CENTER);
            textView[i].setTextSize(16);
            tableRow.addView(textView[i]);
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("YYYY년 MM월 dd일");
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh시 mm분 ss초");

        textView[0].setText(sdf1.format(calendar.getTime()));
        textView[1].setText(sdf2.format(calendar.getTime()));
        textView[2].setText("현관");

        tableLayout.addView(tableRow);
    }

    public void onBackPressed(){
        this.finish();
    }
}