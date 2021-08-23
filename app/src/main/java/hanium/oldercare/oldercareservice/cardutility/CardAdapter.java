package hanium.oldercare.oldercareservice.cardutility;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import hanium.oldercare.oldercareservice.HomeActivity;
import hanium.oldercare.oldercareservice.R;

public class CardAdapter extends PagerAdapter {

    private List<CardModel> models;
    private LayoutInflater layoutInflater;
    private Context context;


    public CardAdapter(List<CardModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_device_item, container, false);

        ImageView imageView;
        TextView title, desc;

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);

        imageView.setImageResource(models.get(position).getImage());
        title.setText(models.get(position).getTitle());
        desc.setText(models.get(position).getDesc());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(position==0) {
//                    Intent intent = new Intent(context, TargetActivity.class);
//                    //intent.putExtra("param", models.get(position).getTitle());
//                    context.startActivity(intent);
//
//                }
//                else if(position==1){
//                    Intent intent = new Intent(context, HomeActivity.class);
//                    //intent.putExtra("param", models.get(position).getTitle());
//                    context.startActivity(intent);
//                }
//                else if(position==2){
//                    Intent intent = new Intent(context, MartActivity.class);
//                    //intent.putExtra("param", models.get(position).getTitle());
//                    context.startActivity(intent);
//                }
//                else if(position==3){
//                    Intent intent = new Intent(context, CarActivity.class);
//                    //intent.putExtra("param", models.get(position).getTitle());
//                    context.startActivity(intent);
//                }
            }

        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}