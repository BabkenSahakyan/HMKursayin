package functionality;

import functionality.exceptions.RangeException;

import java.awt.*;
import java.sql.Time;
import java.util.*;

public class Distributions {
    public static double uniformDistribution(double a, double b) throws RangeException {
//
//        if (a >= b) {
//            throw new RangeException("a֊ն մեծ է b֊ից");
//        }
//

        if (a >= b) {
            throw new RangeException("a֊ն մեծ է b֊ից");
        }

//        Calendar calendar = new GregorianCalendar();
//        Date time = new Time(calendar.getTimeInMillis());
//
//        Long longTime = time.getTime();
//        StringBuilder strTime = new StringBuilder(longTime.toString());
//
//        strTime.reverse();
//        strTime.delete(3, strTime.length());
//
//        double tmpAns = Double.valueOf(strTime.toString());
//
//        try {
//            Thread.sleep(1);
//            Thread.sleep(7);
//        } catch (InterruptedException ie) {
//            System.out.println(ie.getLocalizedMessage());
//        }
//
//        double ans = tmpAns / Math.pow(10, strTime.length());
        double ans = a + (b - a) * Math.random();

        return ans;
    }

    public static double normalDistribution(double ev, double dispersion) {
        double s = 0;
        try {
            for (int i = 0; i < 12; i++)
                s += uniformDistribution(0, 1);
        } catch (RangeException re) {
            System.out.println(re.getLocalizedMessage());
        }

        return ev + Math.pow(dispersion, 1 / 2) * (s - 6);
    }

    public static int poissonDistribution(int lyambda){
        int j = 0;
        double s = 1;
        do {
            try {
                s *= uniformDistribution(0, 1);
                j++;
            } catch (RangeException re){
                System.out.println(re.getLocalizedMessage());
            }
        } while (s >= Math.pow(Math.E, -lyambda));
        return j;
    }

    public static void main(String[] args) {
    }
}