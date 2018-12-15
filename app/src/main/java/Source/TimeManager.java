package Source;

import java.lang.Math; // headers MUST be above the first class

import java.util.ArrayList;

public class TimeManager {
    public static ArrayList<Double>[] Timing_4(double daycount, double timecount) {
        ArrayList<Double>[] timings = new ArrayList[3];

        ArrayList<Double> timing_1 = new ArrayList<Double>();
        ArrayList<Double> timing_2 = new ArrayList<Double>();
        ArrayList<Double> timing_3 = new ArrayList<Double>();

        double frst_variant = timecount / daycount;

        double dx = frst_variant - (int) frst_variant;

        if ((frst_variant - (int) frst_variant) != 0.0) {
            frst_variant = (int) frst_variant + 1;
        }
        double scnd_variant = 0.0;
        double thrd_variant = 0.0;

        for (int i = 0; i < daycount; i++) {
            timing_1.add(frst_variant);
        }


        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                scnd_variant = frst_variant + 1;
                int new_daycount = (int) Math.round(timecount / scnd_variant);
                int free_day_count = (int) (daycount - new_daycount);
                int interval_btw_weeknd = (int) Math.round(daycount / free_day_count);

                if (free_day_count == 1) {
                    for (int j = 0; j < daycount; j++) {
                        if (j == daycount / 2) {
                            timing_2.add(0.0);
                        } else {
                            timing_2.add(scnd_variant);
                        }
                    }
                } else {
                    int interval = interval_btw_weeknd + 1;
                    for (int j = 0; j < daycount; j++) {
                        if (j == interval) {
                            timing_2.add(0.0);
                            interval += interval;
                        } else {
                            timing_2.add(scnd_variant);
                        }
                    }
                }
            } else {
                thrd_variant = scnd_variant + 0.5;
                int new_daycount = (int) Math.round(timecount / thrd_variant);
                int free_day_count = (int) (daycount - new_daycount);
                int interval_btw_weeknd = (int) Math.round(daycount / free_day_count);

                if (free_day_count == 1) {
                    for (int j = 0; j < daycount; j++) {
                        if (j == daycount / 2) {
                            timing_2.add(0.0);
                        } else {
                            timing_2.add(thrd_variant);
                        }
                    }
                } else {
                    int interval = interval_btw_weeknd + 1;
                    for (int j = 0; j < daycount; j++) {
                        if (j == interval) {
                            timing_2.add(0.0);
                            interval += interval;
                        } else {
                            timing_2.add(thrd_variant);
                        }
                    }
                }
            }
        }

        timings[0] = timing_1;
        timings[1] = timing_2;
        timings[2] = timing_3;

        return timings;
    }

    public static ArrayList<Double>[] Timing_3(double daycount, double timecount) {
        ArrayList<Double>[] timings = new ArrayList[3];

        ArrayList<Double> timing_1 = new ArrayList<Double>();
        ArrayList<Double> timing_2 = new ArrayList<Double>();
        ArrayList<Double> timing_3 = new ArrayList<Double>();

        double frst_variant = timecount / daycount;

        double dx = frst_variant - (int) frst_variant;

        if ((frst_variant - (int) frst_variant) != 0.0) {
            frst_variant = (int) frst_variant + 1;
        }
        double scnd_variant = 0.0;
        double thrd_variant = 0.0;

        for (int i = 0; i < daycount; i++) {
            timing_1.add(frst_variant);
        }


        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                scnd_variant = frst_variant + 0.75;
                int new_daycount = (int) Math.round(timecount / scnd_variant);
                int free_day_count = (int) (daycount - new_daycount);
                int interval_btw_weeknd = (int) Math.round(daycount / free_day_count);

                if (free_day_count == 1) {
                    for (int j = 0; j < daycount; j++) {
                        if (j == daycount / 2) {
                            timing_2.add(0.0);
                        } else {
                            timing_2.add(scnd_variant);
                        }
                    }
                } else {
                    int interval = interval_btw_weeknd + 1;
                    for (int j = 0; j < daycount; j++) {
                        if (j == interval) {
                            timing_2.add(0.0);
                            interval += interval;
                        } else {
                            timing_2.add(scnd_variant);
                        }
                    }
                }
            } else {
                thrd_variant = scnd_variant + 0.5;
                int new_daycount = (int) Math.round(timecount / thrd_variant);
                int free_day_count = (int) (daycount - new_daycount);
                int interval_btw_weeknd = (int) Math.round(daycount / free_day_count);

                if (free_day_count == 1) {
                    for (int j = 0; j < daycount; j++) {
                        if (j == daycount / 2) {
                            timing_2.add(0.0);
                        } else {
                            timing_2.add(thrd_variant);
                        }
                    }
                } else {
                    int interval = interval_btw_weeknd + 1;
                    for (int j = 0; j < daycount; j++) {
                        if (j == interval) {
                            timing_2.add(0.0);
                            interval += interval;
                        } else {
                            timing_2.add(thrd_variant);
                        }
                    }
                }
            }
        }

        timings[0] = timing_1;
        timings[1] = timing_2;
        timings[2] = timing_3;

        return timings;
    }

    public static ArrayList<Double>[] Timing_2(double daycount, double timecount) {
        ArrayList<Double>[] timings = new ArrayList[3];

        ArrayList<Double> timing_1 = new ArrayList<Double>();
        ArrayList<Double> timing_2 = new ArrayList<Double>();
        ArrayList<Double> timing_3 = new ArrayList<Double>();

        double frst_variant = timecount / daycount;

        double dx = frst_variant - (int) frst_variant;

        if ((frst_variant - (int) frst_variant) != 0.0) {
            frst_variant = (int) frst_variant + 1;
        }
        double scnd_variant = 0.0;
        double thrd_variant = 0.0;

        for (int i = 0; i < daycount; i++) {
            timing_1.add(frst_variant);
        }


        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                scnd_variant = frst_variant + 0.5;
                int new_daycount = (int) Math.round(timecount / scnd_variant);
                int free_day_count = (int) (daycount - new_daycount);
                int interval_btw_weeknd = (int) Math.round(daycount / free_day_count);

                if (free_day_count == 1) {
                    for (int j = 0; j < daycount; j++) {
                        if (j == daycount / 2) {
                            timing_2.add(0.0);
                        } else {
                            timing_2.add(scnd_variant);
                        }
                    }
                } else {
                    int interval = interval_btw_weeknd + 1;
                    for (int j = 0; j < daycount; j++) {
                        if (j == interval) {
                            timing_2.add(0.0);
                            interval += interval;
                        } else {
                            timing_2.add(scnd_variant);
                        }
                    }
                }
            } else {
                thrd_variant = scnd_variant + 0.5;
                int new_daycount = (int) Math.round(timecount / thrd_variant);
                int free_day_count = (int) (daycount - new_daycount);
                int interval_btw_weeknd = (int) Math.round(daycount / free_day_count);

                if (free_day_count == 1) {
                    for (int j = 0; j < daycount; j++) {
                        if (j == daycount / 2) {
                            timing_2.add(0.0);
                        } else {
                            timing_2.add(thrd_variant);
                        }
                    }
                } else {
                    int interval = interval_btw_weeknd + 1;
                    for (int j = 0; j < daycount; j++) {
                        if (j == interval) {
                            timing_2.add(0.0);
                            interval += interval;
                        } else {
                            timing_2.add(thrd_variant);
                        }
                    }
                }
            }
        }

        timings[0] = timing_1;
        timings[1] = timing_2;
        timings[2] = timing_3;

        return timings;
    }

    public static ArrayList<Double>[] Timing_1(double daycount, double timecount) {
        ArrayList<Double>[] timings = new ArrayList[3];

        ArrayList<Double> timing_1 = new ArrayList<Double>();
        ArrayList<Double> timing_2 = new ArrayList<Double>();
        ArrayList<Double> timing_3 = new ArrayList<Double>();

        double frst_variant = timecount / daycount;

        double dx = frst_variant - (int) frst_variant;

        if ((frst_variant - (int) frst_variant) != 0.0) {
            frst_variant = (int) frst_variant + 1;
        }
        double scnd_variant = 0.0;
        double thrd_variant = 0.0;

        for (int i = 0; i < daycount; i++) {
            timing_1.add(frst_variant);
        }


        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                scnd_variant = frst_variant + 0.5;
                int new_daycount = (int) Math.round(timecount / scnd_variant);
                int free_day_count = (int) (daycount - new_daycount);
                int interval_btw_weeknd = (int) Math.round(daycount / free_day_count);

                if (free_day_count == 1) {
                    for (int j = 0; j < daycount; j++) {
                        if (j == daycount / 2) {
                            timing_2.add(0.0);
                        } else {
                            timing_2.add(scnd_variant);
                        }
                    }
                } else {
                    int interval = interval_btw_weeknd + 1;
                    for (int j = 0; j < daycount; j++) {
                        if (j == interval) {
                            timing_2.add(0.0);
                            interval += interval;
                        } else {
                            timing_2.add(scnd_variant);
                        }
                    }
                }
            } else {
                thrd_variant = scnd_variant + 0.25;
                int new_daycount = (int) Math.round(timecount / thrd_variant);
                int free_day_count = (int) (daycount - new_daycount);
                int interval_btw_weeknd = (int) Math.round(daycount / free_day_count);

                if (free_day_count == 1) {
                    for (int j = 0; j < daycount; j++) {
                        if (j == daycount / 2) {
                            timing_2.add(0.0);
                        } else {
                            timing_2.add(thrd_variant);
                        }
                    }
                } else {
                    int interval = interval_btw_weeknd + 1;
                    for (int j = 0; j < daycount; j++) {
                        if (j == interval) {
                            timing_2.add(0.0);
                            interval += interval;
                        } else {
                            timing_2.add(thrd_variant);
                        }
                    }
                }
            }
        }

        timings[0] = timing_1;
        timings[1] = timing_2;
        timings[2] = timing_3;

        return timings;
    }
}
