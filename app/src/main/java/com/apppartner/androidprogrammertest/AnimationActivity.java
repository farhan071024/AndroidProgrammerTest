package com.apppartner.androidprogrammertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class AnimationActivity extends ActionBarActivity
{
    ImageView imageView;
    int windowwidth;
    int windowheight;
    private RelativeLayout.LayoutParams layoutParams ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        windowwidth = getWindowManager().getDefaultDisplay().getWidth();
        windowheight = getWindowManager().getDefaultDisplay().getHeight();

        imageView= (ImageView) findViewById(R.id.imageView2);
        imageView.setImageResource(R.drawable.ic_apppartner);

        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(3000);
        animation.setStartOffset(1000);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);
        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x_cord = (int) event.getRawX();
                        int y_cord = (int) event.getRawY();

                        if (x_cord > windowwidth) {
                            x_cord = windowwidth;
                        }
                        if (y_cord > windowheight) {
                            y_cord = windowheight;
                        }

                        layoutParams.leftMargin = x_cord - 25;
                        layoutParams.topMargin = y_cord - 75;

                        imageView.setLayoutParams(layoutParams);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void fade(View v){
        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(3000);
      //  animation1.setStartOffset(5000);
        animation1.setFillAfter(true);
        imageView.startAnimation(animation1);
    }
}
