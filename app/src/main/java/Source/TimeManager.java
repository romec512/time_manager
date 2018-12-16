package Source;

import java.lang.Math; // headers MUST be above the first class

import java.util.ArrayList;

public class TimeManager {
    public static ArrayList<Double> Timing_4(double daycount, double timecount) {
        ArrayList<Double> timing_1 = new ArrayList<Double>();
        ArrayList<Double> timing_2 = new ArrayList<Double>();
        ArrayList<Double> timing_3 = new ArrayList<Double>();

        double frst_variant = timecount / daycount;

        double dx = frst_variant - (int) frst_variant;

        if (dx >= 0.5) {
            for (int i = 0; i < daycount; i++) {
                timing_1.add(frst_variant - dx + 1);
            }

            int sum = 0;
            for (Double i : timing_1) {
                sum += i;
            }

            int delta = (int) (sum - timecount);
            System.out.println(delta);
            for (int i = 0; i < delta; i++) {
                timing_1.set(timing_1.size() - 1 - i, timing_1.get(timing_1.size() - 1 - i) - 1);
                System.out.println(timing_1.get(timing_1.size() - 1 - i));
            }
        } else {
            for (int i = 0; i < daycount; i++) {
                timing_1.add(frst_variant - dx);
            }

            int sum = 0;
            for (Double i : timing_1) {
                sum += i;
            }

            int delta = (int) (timecount - sum);
            for (int i = 0; i < delta; i++) {
                timing_1.set(i, timing_1.get(i) + 1);
            }
        }
        //timings[1] = timing_2;
        //timings[2] = timing_3;

        return timing_1;
    }
}
