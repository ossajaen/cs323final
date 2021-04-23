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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    //instantiate all variables used later
    
    //thread handlers
    private Thread calculateThread;
    private Thread doTimer;

    //layouts and image views
    private RelativeLayout mainLayout;
    private ImageView fishImageView;
    private ImageView chestImageView;
    private TextView countTextView;
    private TextView timeTextView;
    private TextView scoreTextView;

    //classes
    private Fish mFish;
    private Treasure mChest;
    private Counter count;

    //subelements
    private int xLocation;
    private int yLocation;
    private int highScore = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mainLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        xLocation = 270;
        yLocation = 480;
        buildFish();
        addChest();

        count = new Counter();
        countTextView = (TextView)findViewById(R.id.pointCounter);

        timeTextView = (TextView)findViewById(R.id.timer);

        scoreTextView = (TextView)findViewById((R.id.score));

        calculateThread = new Thread(calculateAction);

        doTimer = new Thread(trunnable);
        doTimer.start();
    }

    private void buildFish() {
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int initialXPosition = xLocation;
        int intitialYPosition = yLocation;

        int proptionalVelocity = 20;
        mFish = new Fish();
        mFish.setX(initialXPosition);
        mFish.setY(intitialYPosition);
        mFish.setVelocity(proptionalVelocity);

        fishImageView = (ImageView) layoutInflater.inflate(R.layout.fish_image, null);
        fishImageView.setX((float) mFish.getX());
        fishImageView.setY((float) mFish.getY());
        mainLayout.addView(fishImageView);
    }

    private void addChest() {
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int initialXPosition = (int) (Math.random()*((210 - (- 180)))) - 180;
        int initialYPosition = (int) (Math.random()*((470 - (-150)))) - 150;

        //int initialXPosition = 50;
        //int initialYPosition = 180;

        mChest = new Treasure();
        mChest.setX(initialXPosition);
        mChest.setY(initialYPosition);

        chestImageView = (ImageView) layoutInflater.inflate(R.layout.treasure, null);
        chestImageView.setX((float) mChest.getX());
        chestImageView.setY((float) mChest.getY());
        mainLayout.addView(chestImageView);
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
        private static final int DELAY = 20;
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

    // Create the Handler object (on the main thread by default)
    Handler timer = new Handler();
    // Define the code block to be executed
    private Runnable trunnable = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
            DecimalFormat df = new DecimalFormat("###.###");
            count.settCount(count.getTime() - 0.02);
            timeTextView.setText(df.format(count.getTime()).toString());
            if (count.getTime() <= 0){
                if(count.getCount() > highScore){
                    highScore = count.getCount();
                    scoreTextView.setText("High Score: " + highScore);
                }
                countTextView.setText("0");
                count.setCount(0);
                timeTextView.setText("10");
                count.settCount(10.000);
            }
            // Repeat this the same runnable code block again another 2 seconds
            // 'this' is referencing the Runnable object
            timer.postDelayed(this, 2);
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

                DecimalFormat df = new DecimalFormat("###.###");
                //timeTextView.setText(df.format(count.getTime()).toString());

                //count.loseTime();

                //timeTextView.setText(df.format(count.getTime()).toString());

                //double currenttime = count.getTime();

                //double total = currenttime-(currenttime % 0.001);
                //String total2 = String.valueOf(total);
                //timeTextView.setText((total2));

                if (Math.abs(fishImageView.getX()-chestImageView.getX()) <= 100 && Math.abs(fishImageView.getY()-chestImageView.getY()) <= 50){
                    count.addCount();
                    countTextView.setText(count.getCount().toString());

                    timeTextView.setText(df.format(count.getTime()).toString());
                    count.addTime();
                    timeTextView.setText(df.format(count.getTime()).toString());


                    chestImageView.setY((int) (Math.random()*((470 - (-150)))) - 150);
                    chestImageView.setX((int) (Math.random()*((210 - (-180)))) - 180);
                }
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
