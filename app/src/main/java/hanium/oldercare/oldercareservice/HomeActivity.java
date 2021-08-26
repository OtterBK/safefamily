package hanium.oldercare.oldercareservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import hanium.oldercare.oldercareservice.cardutility.DeviceModel;
import hanium.oldercare.oldercareservice.cardutility.DeviceViewAdapter;
import hanium.oldercare.oldercareservice.utility.ScreenManager;

public class HomeActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<DeviceModel> deviceList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ScreenManager.transparentStatusBar(this);



        mRecyclerView = (RecyclerView) findViewById(R.id.home_recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        deviceList.add(new DeviceModel("1","1234"));
        deviceList.add(new DeviceModel("1","1234"));
        deviceList.add(new DeviceModel("1","1234"));


        // specify an adapter (see also next example)
        mAdapter = new DeviceViewAdapter(deviceList);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void onBackPressed(){
        this.finish();
    }
}