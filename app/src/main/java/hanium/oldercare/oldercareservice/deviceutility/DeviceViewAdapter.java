package hanium.oldercare.oldercareservice.deviceutility;

/*
그냥... JSONARRAY 가져다 쓰죠 나중에 리팩토링 할 때 고치겟습니다.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hanium.oldercare.oldercareservice.DeviceInfoActivity;
import hanium.oldercare.oldercareservice.R;
import hanium.oldercare.oldercareservice.handlermessage.DeviceMessage;
import hanium.oldercare.oldercareservice.info.DeviceInfo;


import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class DeviceViewAdapter extends RecyclerView.Adapter<DeviceViewAdapter.ViewHolder> {


    private ArrayList<DeviceModel> deviceList;
    private Context context;
    private Handler callbackHandler; //아이템 바인드할 때 마다 실행

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView statusIcon;
        public TextView wardName;
        public TextView doorCount;
        public TextView speakerCount;
        public View mainView;

        public ViewHolder(View view) {
            super(view);

            statusIcon = (ImageView)view.findViewById(R.id.device_item_status_image);
            wardName = (TextView)view.findViewById(R.id.device_item_ward_name);
            doorCount = (TextView)view.findViewById(R.id.device_item_door_count);
            speakerCount = (TextView)view.findViewById(R.id.device_item_speaker_count);
            mainView = view;

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DeviceViewAdapter(ArrayList<DeviceModel> deviceList, Context context, Handler callbackHandler) {
        this.deviceList = deviceList;
        this.context = context;
        this.callbackHandler = callbackHandler;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DeviceViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_device_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        DeviceModel device = deviceList.get(position);

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DeviceInfo.infoDevice = device;

                Intent intent = new Intent(context.getApplicationContext(), DeviceInfoActivity.class);
                context.startActivity(intent);
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //context.overridePendingTransition(R.anim.slide_in_right, R.anim.none);
            }
        });

        device.setComponent(holder.statusIcon, holder.wardName, holder.doorCount, holder.speakerCount);

        Thread refreshThread = new Thread(()->{
            device.refreshData();
            Message handleMsg = callbackHandler.obtainMessage(DeviceMessage.REFRESH_DEVICE_COMP.ordinal());
            handleMsg.arg1 = position;
            callbackHandler.sendMessage(handleMsg);
        });
        refreshThread.start();



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return deviceList.size();
    }
}

