package functionality.service.system;

public class Bid {
    private double startTime;
    private double duration;
    private double waitTime;
    private boolean served;

    public boolean isServed() {
        return served;
    }

    public void serve(){
        this.served = true;
    }

    public void setStartTime(double startTime){
        this.startTime = startTime;
    }

    public double getStartTime(){
        return this.startTime;
    }

    public void setDuration(double duration){
        this.duration = duration;
    }

    public double getDuration(){
        return this.duration;
    }

    public void setWaitTime(double waitTime){
        this.waitTime = waitTime;
    }

    public double getWaitTime(){
        return this.waitTime;
    }

    public double getEndTime(){
        return this.startTime + this.duration;
    }

}
