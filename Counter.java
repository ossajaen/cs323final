package com.example.mpf;

public class Counter {

    //DATA MEMBERS
    private Integer mCount;
    private Double tCount;

    //CONSTRUCTOR
    public Counter() {
        mCount = 0;
        tCount = 10.000;
    }

    //SETTER AND GETTERS
    public void setCount(int x) {
        mCount = x;
    }

    public void settCount(double x) {
        tCount = x;
    }

    public Integer getCount() {
        return mCount;
    }

    public Double getTime() {
        return tCount;
    }

    //ADD TO COUNTER
    public void addCount() { //found treasure means a point!
        mCount++;
    }

    //ADD TO TIMER
    public void addTime() {
        tCount += .75;
    }

    //LOSE FROM TIMER
    public void loseTime() {
        tCount -= 0.0175;
    }
}
