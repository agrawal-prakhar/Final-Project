package Timers;

public class Timer {
private double startTime;
private int currentTime;
private int previousTime;

private int intervalFrames;

private int countIntervals;
private int previousInterval;
private int frameCount;
private double milliseconds;

public Timer() {
   this.startTime = start(); //sets start of timer to time when initialized
}

public double start() {
    startTime = System.currentTimeMillis();
    return startTime;
}
public int getElapsedTime() { //returns time in seconds
    currentTime = (int)(System.currentTimeMillis() - startTime) / 1000; //difference between current and start time
    return currentTime;
}

public double getElapsedDecimalSeconds() {
    milliseconds = (double)(System.currentTimeMillis() - startTime) / 1000;
    return milliseconds;
}
public void printTime() {
    if(currentTime!=previousTime) {
        System.out.println("current time:"+currentTime);
    }
    previousTime = currentTime;
}



public void frameInterval(int setIntervalFrames) { //checks if the current interval frame equals the desired interval frame
    intervalFrames++; //sets a number for the amount of frames ('intervalFrames') that occur between each interval count
    if(intervalFrames == setIntervalFrames) { //if the frames reaches the desired interval number, the count increases by one
        countIntervals++; //number of intervals reached
        intervalFrames = 0; //once the interval count increases by one, the frame number resets to zero and will start checking all over again
    }
    //previousInterval = countIntervals;
}

public int getIntervalTime() {
    return countIntervals;
}

public void clearIntervalTimer() { intervalFrames = 0; }
public int getPrevInterval() { return previousInterval;}
public void setPrevInterval(int xyz) { previousInterval = xyz; }

public void countFrames() {
    frameCount++;
}

public int getFrameCount() {
    return frameCount;
}

}
