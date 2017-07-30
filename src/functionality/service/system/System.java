package functionality.service.system;

import functionality.Distributions;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class System {
    private final int countOfChannels;
    private final double maxTime;
    private ArrayList<Channel> channels;

    private int count = 0;

    private List<Double> averageStartTime;
    private List<Double> averageWaitTime;
    private List<Double> averageDuration;

    private List<Double> pForEach;
    double epsilon;

    private final int MAX_X = 1366 - 16;
    private final int MIN_X = 10;
    private final int MAX_Y = 768 - 18;
    private final int MIN_Y = 10;

    private final int ZOOM = 10;

    private final int SERVED = MAX_Y - 200;
    private final int NOT_SERVED = MAX_Y - 150;
    private int[] gChanel;
    final int BID_Y = 75;

    public System(int countOfChannels, double maxTime){
        this.countOfChannels = countOfChannels;
        this.maxTime = maxTime;
        this.gChanel = new int[countOfChannels + 1];

        channels = new ArrayList<>(this.countOfChannels);

        for (int i = 0; i < this.countOfChannels; i++) {
            this.channels.add(i, new Channel());
        }

        pForEach = new ArrayList<>();
    }

    private void clearChannels(){
        for (int i = 0; i < this.countOfChannels; i++) {
            this.channels.add(i, new Channel());
        }
    }

    private void start(Group root){

        Line lineX = new Line(MIN_X, MAX_Y - 100, MAX_X, MAX_Y - 100);
        lineX.setStyle("-fx-stroke: green; -fx-stroke-width: 2px");
        root.getChildren().add(lineX);

        Line lineY = new Line(MIN_X, MAX_Y - 100, MIN_X, MIN_Y);
        lineY.setStyle("-fx-stroke: green; -fx-stroke-width: 2px");
        root.getChildren().add(lineY);

        Polygon arrowX = new Polygon(MAX_X, MAX_Y - 100,
                MAX_X - 15, MAX_Y - 100 + 7.5,
                MAX_X - 15, MAX_Y - 100 - 7.5);
        arrowX.setStyle("-fx-stroke: green;" +
                "-fx-fill: green");
        root.getChildren().add(arrowX);

        Polygon arrowY = new Polygon(MIN_X, MIN_Y,
                MIN_X - 7.5, MIN_Y + 15,
                MIN_X + 7.5, MIN_Y + 15);
        arrowY.setStyle("-fx-stroke: green;" +
                "-fx-fill: green");
        root.getChildren().add(arrowY);

        Line served = new Line(MIN_X, MAX_Y - 200, MAX_X, MAX_Y - 200);
        served.setStyle("-fx-stroke: blue; -fx-stroke-width: 2px");
        root.getChildren().add(served);

        Line notServed = new Line(MIN_X, MAX_Y - 150, MAX_X, MAX_Y - 150);
        notServed.setStyle("-fx-stroke: red; -fx-stroke-width: 2px");
        root.getChildren().add(notServed);

        Line endTime = new Line(maxTime * ZOOM, MIN_Y, maxTime * ZOOM, MAX_Y);
        endTime.setStyle("-fx-stroke-dash-array: 5px;" +
                "-fx-stroke: red");
        root.getChildren().add(endTime);

        Text text = new Text(maxTime * ZOOM + 5, MAX_Y - 85, "" + maxTime);
        text.setStyle("-fx-stroke: red;-fx-font-size: 12px; -fx-font-family: cursive; -fx-font-style: italic;");
        root.getChildren().add(text);

        int nextY = MAX_Y - 200 - (countOfChannels + 1) * 50;
        for (int i = 0; i < countOfChannels; i++) {
            nextY += 50;
            gChanel[i] = nextY;
            root.getChildren().add(new Line(MIN_X, nextY, maxTime * ZOOM, nextY));
        }

    }

    private void graphicalLaunch(Group root, List<Double> averageStartTime,
                                 List<Double> averageWaitTime, List<Double> averageDuration){

        clearChannels();

        start(root);

        int served = 0;
        int notServed = 0;

        channels = new ArrayList<>(this.countOfChannels);

        for (int i = 0; i < this.countOfChannels; i++) {
            channels.add(i, new Channel()); //this
        }

        Bid bid = new Bid();

        double startTime = 0;
        Circle circle;
        Text text;
        MoveTo moveTo;
        LineTo lineTo;
        ArcTo arcTo;
        Path path = new Path();
        for (int i = 0; i < averageStartTime.size(); i++) {
            startTime += averageStartTime.get(i);

            bid.setStartTime(startTime);
            bid.setDuration(averageDuration.get(i));
            bid.setWaitTime(averageWaitTime.get(i));

            if (bid.getStartTime() > maxTime){
                break;
            }

            circle = new Circle(bid.getStartTime() * ZOOM, BID_Y, 3);
            circle.setStyle("-fx-stroke: midnightblue; -fx-fill: midnightblue");
            root.getChildren().add(circle);

            text = new Text(bid.getStartTime() * ZOOM - 5, 65, (bid.getStartTime() != (int)bid.getStartTime()
                    ? String.format("%.1f",bid.getStartTime()) : String.format("%.0f",bid.getStartTime())));
            text.setStyle("-fx-font-size: 8px; -fx-font-family: cursive; -fx-font-style: italic;");
            root.getChildren().add(text);

            double minRest;
            int indexOfChannel;
            int s = served;

            if (bid.getStartTime() + bid.getDuration() > maxTime) {
                notServed++;
                moveTo = new MoveTo(bid.getStartTime() * ZOOM, BID_Y);
                path.getElements().add(moveTo);

                lineTo = new LineTo(bid.getStartTime() * ZOOM, NOT_SERVED);
                path.getElements().add(lineTo);

                circle = new Circle(bid.getStartTime() * ZOOM, NOT_SERVED, 3);
                circle.setStyle("-fx-stroke: red; -fx-fill: red");

                root.getChildren().add(circle);
                continue;
            }

            for (Channel c : channels) {
                if (!c.isBusy() || c.getBusyTo() <= bid.getStartTime()) {
                    c.addBid(bid);
                    served++;

                    text = new Text(bid.getStartTime() * ZOOM + 10, gChanel[channels.indexOf(c)] - 4, String.format("%.2f", bid.getDuration()));
                    text.setStyle("-fx-font-size: 8px; -fx-font-family: cursive; -fx-font-style: italic;");
                    root.getChildren().add(text);

                    moveTo = new MoveTo(bid.getStartTime() * ZOOM, BID_Y);
                    path.getElements().add(moveTo);

                    lineTo = new LineTo(bid.getStartTime() * ZOOM, gChanel[channels.indexOf(c)]);
                    path.getElements().add(lineTo);

                    arcTo = new ArcTo(bid.getDuration() * ZOOM, 100, 0,bid.getEndTime() * ZOOM, lineTo.getY(), false, true);
                    path.getElements().addAll(arcTo);

                    lineTo = new LineTo(bid.getEndTime() * ZOOM, SERVED);
                    path.getElements().add(lineTo);

                    circle = new Circle(bid.getEndTime() * ZOOM, SERVED, 3);
                    circle.setStyle("-fx-stroke: blue; -fx-fill: blue");
                    root.getChildren().add(circle);
                    break;
                }
            }

            if (s == served) {
                minRest = channels.get(0).getBusyTo() - bid.getStartTime();
                indexOfChannel = 0;
                for (Channel c : channels) {
                    if (c.getBusyTo() - bid.getStartTime() < minRest) {
                        minRest = c.getBusyTo() - bid.getStartTime();
                        indexOfChannel = channels.indexOf(c);
                    }
                }

                moveTo = new MoveTo(bid.getStartTime() * ZOOM, BID_Y);
                path.getElements().add(moveTo);
                lineTo = new LineTo(bid.getStartTime() * ZOOM, gChanel[indexOfChannel]);
                path.getElements().add(lineTo);

                if (minRest <= bid.getWaitTime()) {

                    bid.setStartTime(channels.get(indexOfChannel).getBusyTo());
                    if (bid.getStartTime() + bid.getDuration() > maxTime) {
                        lineTo = new LineTo(lineTo.getX()/*channels.get(indexOfChannel).getBusyTo() * ZOOM*/, NOT_SERVED);
                        path.getElements().add(lineTo);

                        circle = new Circle(channels.get(indexOfChannel).getBusyTo() * ZOOM, NOT_SERVED, 3);
                        circle.setStyle("-fx-stroke: red; -fx-fill: red");
                        root.getChildren().add(circle);
                        notServed++;
                        continue;
                    }
                    channels.get(indexOfChannel).addBid(bid);

                    text = new Text(bid.getStartTime() * ZOOM + 10, gChanel[indexOfChannel] - 4, String.format("%.2f", bid.getDuration()));
                    text.setStyle("-fx-font-size: 8px; -fx-font-family: cursive; -fx-font-style: italic;");
                    root.getChildren().add(text);

                    moveTo = new MoveTo(lineTo.getX(), gChanel[indexOfChannel]);
                    path.getElements().add(moveTo);
                    arcTo = new ArcTo(minRest * ZOOM, 100, 0, bid.getStartTime() * ZOOM, moveTo.getY(), false, false);
                    path.getElements().add(arcTo);

                    arcTo = new ArcTo(bid.getDuration() * ZOOM, 100, 0, bid.getEndTime() * ZOOM, gChanel[indexOfChannel], false, true);
                    path.getElements().add(arcTo);

                    lineTo = new LineTo(bid.getEndTime() * ZOOM, SERVED);
                    path.getElements().add(lineTo);

                    circle = new Circle(bid.getEndTime() * ZOOM, SERVED, 3);
                    circle.setStyle("-fx-stroke: blue; -fx-fill: blue");
                    root.getChildren().add(circle);
                    served++;

                } else {
                    lineTo = new LineTo(lineTo.getX(), NOT_SERVED);
                    path.getElements().add(lineTo);
                    circle = new Circle(lineTo.getX(), NOT_SERVED, 3);
                    circle.setStyle("-fx-stroke: red; -fx-fill: red");
                    root.getChildren().add(circle);
                    notServed++;
                }
            }

        }
        root.getChildren().add(path);


        java.lang.System.out.println(served + "  " + notServed);
        for (Channel c : channels) {
            Text eachCannel = new Text(maxTime * ZOOM + 5, gChanel[channels.indexOf(c)] - 5,
                    c.getMyCountOfBids() == 0
                            ? "սպ․ հայտեր՝ " + c.getMyCountOfBids()
                            : "սպ․ հայտեր՝ " + c.getMyCountOfBids() + ", թողունակություն " + String.format("%.3f", c.getMyCountOfBids() / maxTime));
            eachCannel.setStyle("-fx-font-size: 12px; -fx-font-family: cursive; -fx-font-style: italic;");
            root.getChildren().add(eachCannel);

            eachCannel = new Text(maxTime * ZOOM + 5, gChanel[channels.indexOf(c)] - 5 + 15,
                    c.getMyCountOfBids() == 0
                    ? ""
                    : "զբաղ․ հավ․՝ " + String.format("%.2f", c.getBusyTime() / maxTime));
            eachCannel.setStyle("-fx-font-size: 12px; -fx-font-family: cursive; -fx-font-style: italic;");
            root.getChildren().add(eachCannel);

            eachCannel = new Text(maxTime * ZOOM + 5, gChanel[channels.indexOf(c)] - 5 + 30,
                    c.getMyCountOfBids() == 0
                            ? ""
                            : "պարաբուրդ՝ " + String.format("%.2f", maxTime - c.getBusyTime()));
            eachCannel.setStyle("-fx-font-size: 12px; -fx-font-family: cursive; -fx-font-style: italic;");
            root.getChildren().add(eachCannel);

            java.lang.System.out.println(c.getMyCountOfBids());
        }

        Text notServedText = new Text(maxTime * ZOOM + 5, NOT_SERVED - 5, "Չսպասարկված  " + notServed);
        notServedText.setStyle("-fx-stroke: red;-fx-font-size: 12px; -fx-font-family: cursive; -fx-font-style: italic;");
        root.getChildren().add(notServedText);

        Text servedText = new Text(maxTime * ZOOM + 5, SERVED - 5, "Սպասարկված  " + served);
        servedText.setStyle("-fx-stroke: blue;-fx-font-size: 12px; -fx-font-family: cursive; -fx-font-style: italic;");
        root.getChildren().add(servedText);

        Text criterion;
        criterion  = new Text(maxTime * ZOOM + 5, BID_Y, "Սպասարկման հավ.՝  " + String.format("%.3f", served / (double)(served + notServed)));
        criterion.setStyle("-fx-font-size: 12px; -fx-font-family: cursive; -fx-font-style: italic;");
        root.getChildren().add(criterion);

        criterion  = new Text(maxTime * ZOOM + 5, BID_Y + 15, "Մերժման հավ.՝  " + String.format("%.3f", 1 - served / (double)(served + notServed)));
        criterion.setStyle("-fx-font-size: 12px; -fx-font-family: cursive; -fx-font-style: italic;");
        root.getChildren().add(criterion);

        criterion  = new Text(maxTime * ZOOM + 5, BID_Y + 30, "Թողունակություն՝  " + String.format("%.3f",served / maxTime));
        criterion.setStyle("-fx-font-size: 12px; -fx-font-family: cursive; -fx-font-style: italic;");
        root.getChildren().add(criterion);

        Text epsilonText;
        epsilonText = new Text(maxTime * ZOOM + 5, BID_Y - 15,"Ճշտություն = " + String.format("%.6f", epsilon));
        epsilonText.setStyle("-fx-font-size: 12px; -fx-font-family: cursive; -fx-font-style: italic;");
        root.getChildren().add(epsilonText);
    }

    protected void run(){
        clearChannels();
        averageStartTime.clear();
        averageWaitTime.clear();
        averageDuration.clear();
        count = 0;

        Bid bid = new Bid();

        double startTime = 0;
        double tmpSt;

        int served = 0;
        int notServed = 0;
        do {
            tmpSt = Distributions.poissonDistribution(3);
            startTime += tmpSt;

            bid.setStartTime(startTime);
            bid.setDuration(Distributions.normalDistribution(4, 0.5));
            bid.setWaitTime(Distributions.poissonDistribution(2));

            averageStartTime.add(tmpSt);
            averageWaitTime.add(bid.getWaitTime());
            averageDuration.add(bid.getDuration());

            ++count;

            if (bid.getStartTime() > maxTime) {
                break;
            }

            double minRest;
            int indexOfChannel;
            int s = served;

            if (bid.getStartTime() + bid.getDuration() > maxTime) {
                notServed++;
            }

            for (Channel c : channels) {
                if (!c.isBusy() || c.getBusyTo() <= bid.getStartTime()) {
                    c.addBid(bid);
                    served++;
                    break;
                }
            }

            if (s == served) {
                minRest = channels.get(0).getBusyTo() - bid.getStartTime();
                indexOfChannel = 0;
                for (Channel c : channels) {
                    if (c.getBusyTo() - bid.getStartTime() < minRest) {
                        minRest = c.getBusyTo() - bid.getStartTime();
                        indexOfChannel = channels.indexOf(c);
                    }
                }

                if (minRest <= bid.getWaitTime()) {
                    bid.setStartTime(channels.get(indexOfChannel).getBusyTo());
                    if (bid.getStartTime() + bid.getDuration() > maxTime) {
                        notServed++;
                        continue;
                    }

                    channels.get(indexOfChannel).addBid(bid);
                    served++;

                } else {
                    notServed++;
                }
            }

        } while (true);
        pForEach.add((double)served / (notServed + served));
    }

    public void launch(int countOfTests, Group root) {
        averageStartTime = new ArrayList<>();
        averageWaitTime = new ArrayList<>();
        averageDuration = new ArrayList<>();
        List<Double> averageStartTime = new ArrayList<>();
        List<Double> averageWaitTime = new ArrayList<>();
        List<Double> averageDuration = new ArrayList<>();
        int count = 0;

        for (int i = 0; i < countOfTests; i++) {
            run();
            for (int j = 0; j < getAverageStartTime().size(); j++) {
                if (averageStartTime.size() <= j){
                    averageStartTime.add(getAverageStartTime().get(j));
                    averageWaitTime.add(getAverageWaitTime().get(j));
                    averageDuration.add(getAverageDuration().get(j));
                } else {
                    averageStartTime.set(j, averageStartTime.get(j) + getAverageStartTime().get(j));
                    averageWaitTime.set(j, averageWaitTime.get(j) + getAverageWaitTime().get(j));
                    averageDuration.set(j, averageDuration.get(j) + getAverageDuration().get(j));
                }
            }
            count += getCount();
        }

        count = Math.round(count / countOfTests);
        for (int i = 0; i < averageStartTime.size(); i++) {
            if (i >= count){
                averageStartTime.remove(i);
                averageWaitTime.remove(i);
                averageDuration.remove(i);
            } else {
                averageStartTime.set(i, averageStartTime.get(i) / countOfTests);
                averageWaitTime.set(i, averageWaitTime.get(i) / countOfTests);
                averageDuration.set(i, averageDuration.get(i) / countOfTests);
            }
        }

        double avP = 0;
        for (Double p : pForEach) {
            avP += p;
        }
        avP /= pForEach.size();

        double p = 0;

        for (Double pp : pForEach) {
            p = Math.pow(pp - avP, 2);
        }

        epsilon = 2.58 * Math.sqrt(p / (countOfTests - 1)) / Math.sqrt(countOfTests);

        graphicalLaunch(root, averageStartTime, averageWaitTime, averageDuration);

    }

    private List<Double> getAverageStartTime(){
        return averageStartTime;
    }
    private List<Double> getAverageWaitTime(){
        return averageWaitTime;
    }
    private List<Double> getAverageDuration(){
        return averageDuration;
    }
    private int getCount(){
        return count;
    }
}
