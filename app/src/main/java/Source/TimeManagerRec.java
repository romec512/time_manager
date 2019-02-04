package Source;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orm.SugarRecord;

import java.lang.reflect.Type;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import DataBase.FreeTime;
import DataBase.Task;
import DataBase.TaskDistribution;

public class TimeManagerRec {
    public String deadline;
    private static final String FREE_TIME_VARIANT = "3";

    public List<String[]> timingResults;
    public DistributionState isPossible = DistributionState.SUCCESS;
    public DBHelper dbHelper;
    private int [] daysOfWeeks = {6,0,1,2,3,4,5};
    public int impossibleTime;
    private String _today;
    private float distrRatio = 0;
    private int initalHours = 0;
    private String initalDate;
    private int freeHours, startFreeHours, endFreeHours;

    public TimeManagerRec(DBHelper _dbHelper, String _deadline){
        timingResults = new ArrayList<>();
        dbHelper = _dbHelper;
        deadline = _deadline;
        impossibleTime = 0;
    }

    public TimeManagerRec(){
        Date todayDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-YYYY");
        _today = format.format(todayDate);
        impossibleTime = 0;
    }

    public void timing(int hours, String date) {
        if(initalHours == 0){
            initalHours = hours;
            initalDate = date;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String todayStr = date;
        if (todayStr.compareTo(deadline) == 0 || hours <= 0) {
            if(hours > 0){
                isPossible = DistributionState.IMPOSSIBLE;
                impossibleTime = hours;
                distrRatio += (float)0.25;
                timingResults.clear();
                timing(initalHours, initalDate);
            } else {
                isPossible = DistributionState.SUCCESS;
            }
            return;
        }
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DistributionState state = getFreeTime(date);
        if(state == DistributionState.FREE_TIME_IS_EMPTY){
            isPossible = DistributionState.FREE_TIME_IS_EMPTY;
            return;
        }
        hours = getTimeRange(hours, date);
        String [] splitDates = todayStr.split("-");
        int dayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + 1;
        int day = (Integer.parseInt(splitDates[0]) + 1) % dayInMonth;
        int month = (Integer.parseInt(splitDates[1]));
        if(day == 0){
            day = 1;
            month = (Integer.parseInt(splitDates[1]) + 1) % 13;
        }
        if(month == 0){
            splitDates[1] = "01";
            splitDates[2] = Integer.parseInt(splitDates[2]) + 1 + "";
        } else {
            splitDates[1] = month / 10 + "" + month % 10;
        }
        String nextDate = (day / 10) + "" + (day % 10) + "-" + splitDates[1] + "-" + splitDates[2];
        timing(hours, nextDate);
    }


     //метод сдвига времени, если пользователь не успевает приступить к выполнению задачи
    //Параметры: смещение(кол-во часов, на которое смещается), выбранная дата(день, в котором смешаются задачи)
    //индекс выбранной карточки для смещения карточек в дне, начиная с выбранной

    public void moveTaskTime(int offset, String selectedDate, int selectedIndex){
        String[] args = new String[]{selectedDate};
        List<TaskDistribution> distributions = TaskDistribution.find(TaskDistribution.class, "task_date = ?",
                args, null, "start_time", null);
        for(int i = selectedIndex; i < distributions.size(); i++){
            int newStartHours = distributions.get(i).getStartHours() + offset;
            int newStopHours = distributions.get(i).getEndHours() + offset;
            if(newStartHours >= 24){
                newStartHours = 24;
            }
            if(newStopHours >= 24){
                newStopHours = 24;
            }
            distributions.get(i).setStartTime(newStartHours, distributions.get(i).getStartMinutes());
            distributions.get(i).setStopTime(newStopHours, distributions.get(i).getEndMinutes());
            distributions.get(i).save();
        }
    }

    //метод сохранения в бд распределенного времени
    public void distributeTask(long taskId){
        Task task = SugarRecord.findById(Task.class, taskId);
        for(String[] taskInfo : timingResults){
            TaskDistribution distribution = new TaskDistribution(task, taskInfo[0], taskInfo[1],taskInfo[2]);
            distribution.save();
        }
    }

    public void moveDeletedTaskTime(int deletedTaskId){
        String[] args = new String[]{String.valueOf(deletedTaskId)};
        List<TaskDistribution> distributions = TaskDistribution.find(TaskDistribution.class, "task_id = ?", args,
                null, "task_date", null);
        for (TaskDistribution distribution: distributions) {
            String[] dayliArgs = new String[]{distribution.date};
            List<TaskDistribution> dayDistributions = TaskDistribution.find(TaskDistribution.class, "task_date = ?", dayliArgs,
                    null, "start_time", null);
            int offset = distribution.getEndHours() - distribution.getStartHours();
            for(TaskDistribution dayDistribution : dayDistributions){
                if(distribution.getStartHours() < dayDistribution.getStartHours()){
                    int newStartHours = dayDistribution.getStartHours() - offset;
                    int newStopHours = dayDistribution.getEndHours() - offset;
                    dayDistribution.setStartTime(newStartHours, distribution.getStartMinutes());
                    dayDistribution.setStopTime(newStopHours, distribution.getEndMinutes());
                    dayDistribution.save();
                }
            }
            distribution.delete();
        }
        Task.deleteAll(Task.class, "ID = ?", args);
    }

    private DistributionState getFreeTime(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dayOfWeek = daysOfWeeks[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        String [] args = new String[]{dayOfWeek + "", FREE_TIME_VARIANT};
        //Находим свободное время для текущего дня недели
        List<FreeTime> freeTime = FreeTime.find(FreeTime.class, "day_of_week = ? AND free_time_variant = ?", args);
        //для текущего времени находим часы начала и конца свободного времени
        int startFreeHour = 0, endFreeHour = 0,startFreeMinutes = 0, endFreeMinutes = 0;
        if(freeTime.size() != 0) {
            startFreeHour = Integer.parseInt(freeTime.get(0).timeStart.split(":")[0]);
            endFreeHour = Integer.parseInt(freeTime.get(0).timeStop.split(":")[0]);
            startFreeMinutes = Integer.parseInt(freeTime.get(0).timeStart.split(":")[1]);
            endFreeMinutes = Integer.parseInt(freeTime.get(0).timeStop.split(":")[1]);
        }
        //Если пользователь не заполнил свободное время
        if(startFreeHour == 0 && endFreeHour == 0){
            return DistributionState.FREE_TIME_IS_EMPTY;
        }


        Date today = new Date();
        String todayStr = dateFormat.format(today);

        args = new String[]{date};
        List<TaskDistribution> distributions = TaskDistribution.find(TaskDistribution.class, "task_date = ?", args, null, "task_date", null);
        int count = distributions.size();


        SimpleDateFormat realtimeformat = new SimpleDateFormat("HH:mm");
        calendar.setTime(today);   // assigns calendar to given date
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinutes = calendar.get(Calendar.MINUTE);


        //если на сегодня нет задач
        if(count == 0){
            //Если текущая дата - это сегодня, то расчитываем текущее время и сравниваем его с временем начала
            if(date.compareTo(todayStr) == 0) {

                //Если текущее время больше старта свободного времени
                if(currentHour > startFreeHour){
                    freeHours = endFreeHour - currentHour;
                    startFreeHour = currentHour;
                    if(currentMinutes > endFreeMinutes){
                        freeHours--;
                    }
                }
             } else {
                freeHours = endFreeHour - startFreeHour;
                if(startFreeMinutes > endFreeMinutes){
                    freeHours--;
                }
            }
        } else {
            int lastElement = distributions.size() - 1;
            //время окончания последней задачи - предварительно время начала свободного времени
            startFreeHour = Integer.parseInt(distributions.get(lastElement).stopTime.split(":")[0]);
            //если на текущий день уже есть задачи
            if(date.compareTo(todayStr) == 0){
                if(startFreeHour > currentHour){
                    freeHours = endFreeHour - startFreeHour;
                    if(startFreeMinutes > endFreeMinutes){
                        freeHours--;
                    }
                } else {
                    //если текущее время уже прошло окончание свободного времени
                    if(currentHour > endFreeHour){
                        freeHours = 0;
                        return DistributionState.SUCCESS;
                    }
                    freeHours = endFreeHour - currentHour - 1;
                    startFreeHour = currentHour + 1;
                }
            } else {
                freeHours = endFreeHour - startFreeHour;
            }
        }
        this.startFreeHours = startFreeHour;
        this.endFreeHours = endFreeHour;
        return DistributionState.SUCCESS;
    }

    private int getTimeRange(int hours, String date){
        if(freeHours == 0){
          return hours;
        } else if(freeHours >= 3){
            int endTime;
            if(hours < 3){
                endTime = (startFreeHours + hours) % 24;
                hours = 0;
            } else {
                if(impossibleTime == 0) {
                    hours -= 3;
                    endTime = (startFreeHours + 3) % 24;
                } else { //Если алгоритм не смог найти распределение по 3 часа и у него осталось "лишнее" время
                    int distr = (int)((freeHours - 3) * distrRatio + 3);
                    if(distr > hours) {
                        distr = hours;
                    }
                    hours -= distr;
                    endTime = (startFreeHours + distr) % 24;
                    impossibleTime -= distr;
                }
            }
            timingResults.add(new String[]{
                    (startFreeHours / 10) + "" + (startFreeHours % 10) + ":00",
                    (endTime / 10) + "" + (endTime % 10) + ":00",
                    date
            });
        } else {
            int endTime;
            if(hours < freeHours){
                endTime = (startFreeHours + hours) % 24;
                hours = 0;
            } else {
                endTime = startFreeHours + freeHours;
                hours -= freeHours;
            }
            timingResults.add(new String[]{
                    (startFreeHours / 10) + "" + (startFreeHours % 10) + ":00",
                    (endTime / 10) + "" + (endTime % 10) + ":00",
                    date
            });
        }
        return hours;
    }
}
