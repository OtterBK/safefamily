package hanium.oldercare.oldercareservice.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.text.format.Time;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import hanium.oldercare.oldercareservice.R;
import hanium.oldercare.oldercareservice.info.TimeInfo;

/**
 * Created by Administrator on 2017-08-07.
 */

public class TimeCheckDialog extends AppCompatActivity{

    private Context context;
//    private int retVal; // ok-success: 2, ok-fails: 1, cancel: 0
    private TextView titleTxt;

    public TimeCheckDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void editTime() { //val:0 로그아웃 :1 계정삭제

        final Dialog dlg = new Dialog(context);        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);          // 액티비티의 타이틀바를 숨긴다.
        dlg.setContentView(R.layout.activity_device_time_check);    // 커스텀 다이얼로그의 레이아웃을 설정한다.
        titleTxt = (TextView) dlg.findViewById(R.id.time_check_lbl1);

        dlg.show();                                                 // 커스텀 다이얼로그를 노출한다.

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText message = (EditText) dlg.findViewById(R.id.time_check_edit_time);
        final Button okButton = (Button) dlg.findViewById(R.id.time_check_ok);
        final Button cancelButton = (Button) dlg.findViewById(R.id.time_check_cancel);

        message.setText(TimeInfo.DANGER_HOUR+"");

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                String timeStr = message.getText().toString();

                // 커스텀 다이얼로그를 종료한다.

                TimeInfo.DANGER_HOUR = Integer.parseInt(timeStr);
                Toast.makeText(context, "이상 징후 판단 시간을 변경했습니다.", Toast.LENGTH_SHORT).show();

                Thread tmpThread = new Thread(()-> {
                        try ( //요기서 객체를 생성하면 try종료 후 자동으로 close처리됨
                              //true : 기존 파일에 이어서 작성 (default는 false임)

                              FileWriter fw = new FileWriter("TimeCheck.txt", false);
                              BufferedWriter bw = new BufferedWriter(fw);
                        ) {

                            bw.write(String.valueOf(TimeInfo.DANGER_HOUR)); //버퍼에 데이터 입력
                        } catch (Exception e) {
                            System.out.println(e);
                        } finally {
                            dlg.dismiss();
                        }

                    }
                );
                tmpThread.start();


            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소했습니다.", Toast.LENGTH_SHORT).show();
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}