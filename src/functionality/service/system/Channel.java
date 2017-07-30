package functionality.service.system;

public class Channel {
    private static int countOfBids;
    private int myCountOfBids;
    private Bid bid;

    private double busyFrom;
    private double busyTo;
    private double busyTime;


    public void addBid(Bid bid){
        this.bid = bid;
        busyFrom = bid.getStartTime();
        busyTo = bid.getEndTime();
        this.bid.serve();
        myCountOfBids++;
        countOfBids++;
        busyTime += busyTo - busyFrom;
    }

    public boolean isBusy(){
        if (this.bid == null)
            return false;
        return true;
    }

    public double getBusyTime(){
        return busyTime;
    }

    public double getBusyFrom(){
        return this.busyFrom;
    }

    public double getBusyTo(){
        return this.busyTo;
    }

    public int getMyCountOfBids() {
        return myCountOfBids;
    }

    public static int getCountOfBids(){
        return countOfBids;
    }

}
