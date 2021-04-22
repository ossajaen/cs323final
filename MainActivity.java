package com.example.deadfishapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private Thread calculateThread;

    private RelativeLayout mainLayout;
    private ImageView fishImageView;

    private Fish mFish;
    private Home mHome;

    private int xLocation;
    private int yLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mainLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        xLocation = 200;
        yLocation = 200;
        addHome();
        buildFish();

        calculateThread = new Thread(calculateAction);
    }

    private void addHome() {
        int initialXPosition = xLocation;
        int initialYPosition = yLocation;
        mHome = new Home();
        mHome.setX(initialXPosition);
        mHome.setY(initialYPosition);
    }

    private void buildFish() {
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int initialXPosition = xLocation;
        int intitialYPosition = yLocation;

        int proptionalVelocity = 10;
        mFish = new Fish();
        mFish.setX(initialXPosition);
        mFish.setY(intitialYPosition);
        mFish.setVelocity(proptionalVelocity);

        fishImageView = (ImageView) layoutInflater.inflate(R.layout.fish_image, null);
        fishImageView.setX((float) mFish.getX());
        fishImageView.setY((float) mFish.getY());
        mainLayout.addView(fishImageView);
    }

    @Override
    protected void onResume() {
        calculateThread.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    private Runnable calculateAction = new Runnable() {
        private static final int DELAY = 200;
        public void run() {
            try {
                while(true) {
                    mFish.move(xLocation, yLocation);
                    Thread.sleep(DELAY);
                    threadHandler.sendEmptyMessage(0);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public Handler threadHandler = new Handler() {
        public void handleMessage (android.os.Message msg) {
            fishImageView.setX((float) mFish.getX());
            fishImageView.setY((float) mFish.getY());
        }
    };

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        int touchAction = event.getActionMasked();
        switch (touchAction) {
            case MotionEvent.ACTION_DOWN:
                xLocation = (int) event.getX();
                yLocation = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                xLocation = (int) event.getX();
                yLocation = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                xLocation = (int) event.getX();
                yLocation = (int) event.getY();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
