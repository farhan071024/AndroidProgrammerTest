package com.apppartner.androidprogrammertest;

import android.animation.ObjectAnimator;
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
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class AnimationActivity extends ActionBarActivity implements Animation.AnimationListener
{
    ImageView imageView;
    private android.widget.RelativeLayout.LayoutParams layoutParams;
    private Toolbar toolBar;
    final static int FLIP_VERTICAL = 1;
    final static int FLIP_HORIZONTAL = 2;
    Bitmap bitmap;
    AnimationDrawable rocketAnimation;
    int angle=0;

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

        //providing drag functionality
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                ClipData dragData = new ClipData(v.getTag().toString(), mimeTypes, item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(imageView);

                v.startDrag(dragData, myShadow, null, 0);
                return true;
            }
        });

        imageView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {

                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        v.setLayoutParams(layoutParams);
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        break;

                    case DragEvent.ACTION_DROP:
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

    // fading the image 100% alpha by pressing "fade" button
    public void fade(View v){
        // fading the image to 0% alpha
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(5000);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);

        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(5000);
        animation1.setFillAfter(true);
        imageView.startAnimation(animation1);
    }

    //Horizontal flip
    public void flipHorizontal(View v){
        imageView .setImageBitmap(flip(bitmap ,FLIP_HORIZONTAL));
    }

    //Vertical flip
    public void flipVertical(View v){
        imageView .setImageBitmap(flip(bitmap ,FLIP_VERTICAL));
    }

    // Making the image gray
    public void gray(View v){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }

    //Making the image colored
    public void color(View v){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(1);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);

    }

    // Performing frame animation
    public void frameAnim(View v){
        imageView.setBackgroundResource(R.drawable.frame);
        rocketAnimation = (AnimationDrawable) imageView.getBackground();
        rocketAnimation.start();
    }

    //Stopping frame animation
    public void stopAnim(View v){
        if(rocketAnimation != null) {
            if (rocketAnimation.isRunning()) {
                rocketAnimation.stop();
            } else {
            }
        }else{
            Toast.makeText(AnimationActivity.this,"Frame animation is not active",Toast.LENGTH_SHORT).show();
        }
    }

    //Rotating the image by 90 degree
    public void rotation(View v){
        angle=angle + 90;
        imageView.setRotation(angle);
    }

    //Performing bounce animation
    public void bounce(View v){
        ObjectAnimator animY = ObjectAnimator.ofFloat(imageView, "translationY", -100f, 0f);
        animY.setDuration(1000);//1sec
        animY.setInterpolator(new BounceInterpolator());
        animY.setRepeatCount(5);
        animY.start();
    }

    //Performing move animation
    public void move(View v){
        TranslateAnimation animation = new TranslateAnimation(0.0f, 400.0f, 0.0f, 0.0f);
        animation.setDuration(3000);
        animation.setRepeatCount(3);
        animation.setRepeatMode(2);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);
    }

    //Performing Zoom In
    public void zoomIn(View v){
       Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        // set animation listener
        animZoomIn.setAnimationListener(this);
        imageView.startAnimation(animZoomIn);
    }

    //Performing Zoom Out
    public void zoomOut(View v){
        Animation animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
        // set animation listener
        animZoomOut.setAnimationListener(this);
        imageView.startAnimation(animZoomOut);
    }

    //Callbacks of AnimationListener
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
