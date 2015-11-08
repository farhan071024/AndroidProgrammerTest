package com.apppartner.androidprogrammertest;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class AnimationActivity extends ActionBarActivity
{
    ImageView imageView;
    private android.widget.RelativeLayout.LayoutParams layoutParams;
    private Toolbar toolBar;
    final static int FLIP_VERTICAL = 1;
    final static int FLIP_HORIZONTAL = 2;
    Bitmap bitmap;
    AnimationDrawable rocketAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);


        imageView= (ImageView) findViewById(R.id.imageView2);
        bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap(); // get bitmap associated with your imageview



        //setting up the toolbar
        toolBar= (Toolbar) findViewById(R.id.include);
        toolBar.setTitle("Animation");
        toolBar.setBackgroundColor(Color.BLACK);
        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable backArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        backArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // fading the image to 0% alpha
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(3000);
        animation.setStartOffset(1000);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                ClipData dragData = new ClipData(v.getTag().toString(), mimeTypes, item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(imageView);

                v.startDrag(dragData, myShadow, null, 0);
                //    imageView.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        imageView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                //   imageView.setVisibility(View.INVISIBLE);
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        //     Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        //    Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        //    Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        v.setLayoutParams(layoutParams);
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        //       Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        //      Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");

                        // Do nothing
                        break;

                    case DragEvent.ACTION_DROP:
                        //         Log.d(msg, "ACTION_DROP event");

                        // Do nothing
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(imageView);

                    imageView.startDrag(data, shadowBuilder, imageView, 0);
                    imageView.setVisibility(View.INVISIBLE);
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    public static Bitmap flip(Bitmap src, int type) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if(type == FLIP_VERTICAL) {
            matrix.preScale(1.0f, -1.0f);
        }
        // if horizonal
        else if(type == FLIP_HORIZONTAL) {
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Making the image 100% alpha by pressing "fade" button
    public void fade(View v){
        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(3000);
      //  animation1.setStartOffset(5000);
        animation1.setFillAfter(true);
        imageView.startAnimation(animation1);
    }
    public void flipHorizontal(View v){
        imageView .setImageBitmap(flip(bitmap ,FLIP_HORIZONTAL));
    }
    public void flipVertical(View v){
        imageView .setImageBitmap(flip(bitmap ,FLIP_VERTICAL));
    }
    public void gray(View v){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }
    public void color(View v){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(1);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);

    }
    public void frameAnim(View v){
        imageView.setBackgroundResource(R.drawable.rocket_thrust);
        rocketAnimation = (AnimationDrawable) imageView.getBackground();
        rocketAnimation.start();
    }
    public void stopAnim(View v){
        if(rocketAnimation.isRunning()){
        rocketAnimation.stop();
        }
    }
}
