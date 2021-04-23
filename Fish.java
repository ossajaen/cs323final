package com.example.deadfishapp;

public class Fish {
    //data members
    private int mX;
    private int mY;
    private int mVelocity; //used to determine rate of change of fish position
    
    
    //setters and getters for position and velocity
    public void setVelocity(int velocity) {
        mVelocity = velocity;
    }

    public int getVelocity() {
        return mVelocity;
    }

    public void setX(int x) {
        mX = x;
    }

    public void setY(int y) {
        mY = y;
    }

    public int getX() {
        return mX - 240; //used pixel counts to align edges of image to center of image
    }

    public int getY() {
        return mY - 300; //used pixel counts to align edges of image to center of image
    }

    //actions: move fish from one position closer to finger
    public void move(int destinationX, int destinationY) {
        int distX = destinationX - mX;
        int distY = destinationY - mY;
        mX += distX/mVelocity; //divisor helps slow down fish as it approaches finger
        mY += distY/mVelocity;
    }
}
