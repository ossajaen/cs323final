package com.example.mpf;

public class Fish {

    //DATA MEMBERS
    private int mX;
    private int mY;
    private int mVelocity; //FISH VELOCITY

    //SETTERS AND GETTERS
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

    //MOVE FISH TO DESTINATION
    public void move(int destinationX, int destinationY) {
        int distX = destinationX - mX;
        int distY = destinationY - mY;
        mX += distX/mVelocity; //divisor helps slow down fish as it approaches finger
        mY += distY/mVelocity;
    }
}
