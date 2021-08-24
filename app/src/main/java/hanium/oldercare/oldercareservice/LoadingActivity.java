package hanium.oldercare.oldercareservice;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import hanium.oldercare.oldercareservice.utility.ScreenManager;


public class LoadingActivity extends AppCompatActivity {

    private ImageView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);


        Logger.addLogAdapter(new AndroidLogAdapter()); //로거 로드

        ScreenManager.transparentAll(this);

        loadComponents();

        title.setVisibility(View.INVISIBLE);

        Handler fadeInHandler = new Handler(){
            public void handleMessage (Message msg){
                super.handleMessage(msg);
                titleFadeIn();
            }
        };
        fadeInHandler.sendEmptyMessageDelayed(0,1000);



        Handler handler = new Handler(){
            public void handleMessage (Message msg){
                super.handleMessage(msg);
                startActivity(new Intent(LoadingActivity.this, LoginSelectActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0,3500);
    }

    private void loadComponents(){
        title = (ImageView) findViewById(R.id.loading_title);
    }


    private void titleFadeOut(){
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(1000);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                title.setVisibility(View.GONE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        title.startAnimation(fadeOut);

    }

    private void titleFadeIn(){
        title.setVisibility(View.VISIBLE);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator()); // add this
        fadeIn.setDuration(1400);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                title.setVisibility(View.VISIBLE);
            }
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }
        });

        title.setAnimation(fadeIn);

    }

    private void animate(final ImageView imageView, final int images[], final int imageIndex, final boolean forever) {

        //imageView <-- The View which displays the images
        //images[] <-- Holds R references to the images to display
        //imageIndex <-- index of the first image to show in images[]
        //forever <-- If equals true then after the last image it starts all over again with the first image resulting in an infinite loop. You have been warned.

        int fadeInDuration = 500; // Configure time values here
        int timeBetween = 3000;
        int fadeOutDuration = 1000;

        imageView.setVisibility(View.INVISIBLE);    //Visible or invisible by default - this will apply when the animation ends
        imageView.setImageResource(images[imageIndex]);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(fadeInDuration);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
        fadeOut.setStartOffset(fadeInDuration + timeBetween);
        fadeOut.setDuration(fadeOutDuration);

        AnimationSet animation = new AnimationSet(false); // change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        animation.setRepeatCount(1);
        imageView.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                if (images.length - 1 > imageIndex) {
                    animate(imageView, images, imageIndex + 1,forever); //Calls itself until it gets to the end of the array
                }
                else {
                    if (forever){
                        animate(imageView, images, 0,forever);  //Calls itself to start the animation all over again in a loop if forever = true
                    }
                }
            }
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }
        });
    }
}
