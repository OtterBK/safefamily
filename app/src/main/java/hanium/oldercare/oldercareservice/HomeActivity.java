package hanium.oldercare.oldercareservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import hanium.oldercare.oldercareservice.cardutility.CardAdapter;
import hanium.oldercare.oldercareservice.cardutility.CardModel;

public class HomeActivity extends AppCompatActivity {

    ViewPager viewPager;
    CardAdapter adapter;
    List<CardModel> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        models = new ArrayList<>();
        models.add(new CardModel(R.drawable.door_back, "목적지", "어디로 여행을 갈지 골라보자"));
        models.add(new CardModel(R.drawable.logo_blue, "숙소", "근처 숙소들을 찾아보고 숙소 가격을 알아보자"));
        models.add(new CardModel(R.drawable.logo_white, "장보기", "무엇을 사고 대충 비용이 얼마나 드는지 알아보자"));
        models.add(new CardModel(R.drawable.icon, "이동수단", "어떤 이동수단을 사용하고 비용이 얼마나 드는지 알아보자"));

        adapter = new CardAdapter(models,this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 360, 130, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };
        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));

                }

                else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void onBackPressed(){
        this.finish();
    }
}