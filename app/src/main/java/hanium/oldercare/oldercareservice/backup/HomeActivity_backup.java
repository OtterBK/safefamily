package hanium.oldercare.oldercareservice.backup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import hanium.oldercare.oldercareservice.R;

public class HomeActivity_backup extends AppCompatActivity {

    private ConstraintLayout deviceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    public void onBackPressed(){
        this.finish();
    }
}