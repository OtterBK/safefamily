package hanium.oldercare.oldercareservice.cardutility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hanium.oldercare.oldercareservice.R;


import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class DeviceViewAdapter extends RecyclerView.Adapter<DeviceViewAdapter.ViewHolder> {
    private ArrayList<DeviceModel> deviceList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView statusIcon;
        public TextView wardName;
        public TextView doorCount;
        public TextView speakerCount;

        public ViewHolder(View view) {
            super(view);
//            statusIcon = (ImageView)view.findViewById(R.id.device_item_status_image);
//            wardName = (TextView)view.findViewById(R.id.device_item_ward_name);
//            doorCount = (TextView)view.findViewById(R.id.device_item_door_count);
//            speakerCount = (TextView)view.findViewById(R.id.device_item_speaker_count);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DeviceViewAdapter(ArrayList<DeviceModel> deviceList) {
        this.deviceList = deviceList;
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
        //holder.statusIcon.setImageResource();
//        holder.wardName.setText(deviceList.get(position).getWard_name());
//        holder.doorCount.setText(deviceList.get(position).getDoorLogs().size());
//        holder.speakerCount.setText(deviceList.get(position).getSpeakerLogs().size());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return deviceList.size();
    }
}

