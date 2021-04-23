package com.example.deadfishapp;

public class Counter {
    //DATA MEMBERS
    private Integer mCount;
    private Double tCount;

    //CONSTRUCTOR
    public Counter() {
        mCount = 0;
        tCount = 10.000;
    }

    //SETTER AND GETTERs

    public void setCount(int x){
        mCount = x;
    }

    public void settCount(double x){
        tCount = x;
    }

    public Integer getCount() {
        return mCount;
    }

    public Double getTime(){
        return tCount;
    }
    
    //modify timer directly
    public void addCount() { //found treasure means a point!
        mCount++;
    }

    public void addTime(){ //foound treasure means more time!
        tCount += .75;
    }

    public void loseTime(){ //this code is now useless as we now use a proper handler that uses actual time
        tCount -= 0.0175;
    }
}
