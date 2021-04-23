package com.example.mpf;

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

    //INSTANTIATE VARIABLES
    
    //THREAD HANDLERS
    private Thread calculateThread;
    private Thread doTimer;

    //LAYOUT AND IMAGE VIEWS
    private RelativeLayout mainLayout;
    private ImageView fishImageView;
    private ImageView chestImageView;
    private TextView countTextView;
    private TextView timeTextView;
    private TextView scoreTextView;

    //CLASSES
    private Fish mFish;
    private Treasure mChest;
    private Counter count;

    //SUBELEMENTS
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

        //CREATE FISH AND CHEST AT STARTING LOCATIONS
        xLocation = 270;
        yLocation = 480;
        buildFish();
        addChest();

        //CREATE COUNTER
        count = new Counter();

        //CREATE TEXT VIEWS
        countTextView = (TextView)findViewById(R.id.pointCounter);
        timeTextView = (TextView)findViewById(R.id.timer);
        scoreTextView = (TextView)findViewById((R.id.score));

        //CREATE THREADS
        calculateThread = new Thread(calculateAction);
        doTimer = new Thread(trunnable);

        //START TIMER THREAD
        doTimer.start();
    }

    private void buildFish() {
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //SET INITIAL POSITION AND VELOCITY FOR FISH
        int initialXPosition = xLocation;
        int intitialYPosition = yLocation;
        int proptionalVelocity = 20;

        //CREATE FISH AT LOCATION WITH VELOCITY
        mFish = new Fish();
        mFish.setX(initialXPosition);
        mFish.setY(intitialYPosition);
        mFish.setVelocity(proptionalVelocity);

        //ADD FISH TO LAYOUT
        fishImageView = (ImageView) layoutInflater.inflate(R.layout.fish_image, null);
        fishImageView.setX((float) mFish.getX());
        fishImageView.setY((float) mFish.getY());
        mainLayout.addView(fishImageView);
    }

    private void addChest() {
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //SET RANDOM STARTING POSITION FOR CHEST
        int initialXPosition = (int) (Math.random()*((210 - (- 180)))) - 180;
        int initialYPosition = (int) (Math.random()*((470 - (-150)))) - 150;

        //CREATE CHEST AT STARTING POSITION
        mChest = new Treasure();
        mChest.setX(initialXPosition);
        mChest.setY(initialYPosition);

        //ADD CHEST TO LAYOUT
        chestImageView = (ImageView) layoutInflater.inflate(R.layout.treasure, null);
        chestImageView.setX((float) mChest.getX());
        chestImageView.setY((float) mChest.getY());
        mainLayout.addView(chestImageView);
    }

    @Override
    protected void onResume() {

        //START MOVE TO FINGER THREAD
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

        //SET DELAY FOR FINDER PRESS
        private static final int DELAY = 20;
        public void run() {
            try {
                while(true) {

                    //MOVE FISH TO FINDER LOCATION
                    mFish.move(xLocation, yLocation);

                    //WAIT DELAY AMOUNT
                    Thread.sleep(DELAY);
                    threadHandler.sendEmptyMessage(0);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    //CREATE TIMER HANDLER
    Handler timer = new Handler();
    private Runnable trunnable = new Runnable() {
        @Override
        public void run() {

            //REMOVE TIME FROM TIMER
            count.settCount(count.getTime() - 0.02);

            //UPDATE TIMER TO 3 DECIMAL POINTS IN LAYOUT
            DecimalFormat df = new DecimalFormat("###.###");
            timeTextView.setText(df.format(count.getTime()).toString());

            //RESET SCORE WHEN TIMER RUNS OUT
            if (count.getTime() <= 0){

                //UPDATE HIGH SCORE
                if(count.getCount() > highScore){
                    highScore = count.getCount();
                    scoreTextView.setText("High Score: " + highScore);
                }

                countTextView.setText("0");
                count.setCount(0);
                timeTextView.setText("10");
                count.settCount(10.000);
            }

            //WAIT TO REMOVE TIME FROM TIMER
            timer.postDelayed(this, 2);
        }
    };

    //CREATE MOVE TO FINGER HANDLER
    public Handler threadHandler = new Handler() {
        public void handleMessage (android.os.Message msg) {

            //UPDATE FISH POSITION IN LAYOUT
            fishImageView.setX((float) mFish.getX());
            fishImageView.setY((float) mFish.getY());
        }
    };

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        int touchAction = event.getActionMasked();
        switch (touchAction) {

            //UPDATE FINGER LOCATION ON FINGER PRESS
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

                //ADD TO TIMER AND SCORE WHEN FISH GETS TO CHEST
                if (Math.abs(fishImageView.getX()-chestImageView.getX()) <= 100 && Math.abs(fishImageView.getY()-chestImageView.getY()) <= 50){

                    //ADD TO COUNTER AND UPDATE IN LAYOUT
                    count.addCount();
                    countTextView.setText(count.getCount().toString());

                    //ADD TO TIMER AND UPDATE IN LAYOUT
                    timeTextView.setText(df.format(count.getTime()).toString());
                    count.addTime();
                    timeTextView.setText(df.format(count.getTime()).toString());

                    //MOVE CHEST TO NEW RANDOM POSITION
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
