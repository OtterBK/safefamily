package hanium.oldercare.oldercareservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);

        Handler handler = new Handler(){
            public void handleMessage (Message msg){
                super.handleMessage(msg);
                startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0,2000);
    }
}
