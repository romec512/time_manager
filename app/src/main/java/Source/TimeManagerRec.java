package Source;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orm.SugarRecord;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import DataBase.Task;
import DataBase.TaskDistribution;

public class TimeManagerRec {
    public String deadline;
    private static final String FREE_TIME_VARIANT = "3";

    public List<String[]> timingResults;
    public boolean isPossible = true;
    public DBHelper dbHelper;
    private int [] daysOfWeeks = {6,0,1,2,3,4,5};
    public int impossibleTime;
    private String _today;

    public TimeManagerRec(DBHelper _dbHelper, String _deadline){
        timingResults = new ArrayList<>();
        dbHelper = _dbHelper;
        deadline = _deadline;
    }

    public TimeManagerRec(){
        Date todayDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-YYYY");
        _today = format.format(todayDate);
    }

    public void timing(int hours, String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String todayStr = date;
        if (todayStr.compareTo(deadline) == 0 || hours <= 0) {
            if(hours > 0){
                isPossible = false;
                impossibleTime = hours;
            }
            return;
        }
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int freeHours = 0;
        int busyHour, startFreeHour = 0;
        String [] freeSplit = new String[]{"", ""};
        String startFreeTime = "", stopFreeTime = "";
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dayOfWeek = daysOfWeeks[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        String [] args = new String[]{dayOfWeek + "", FREE_TIME_VARIANT};
        //Выбираем кол-во свободного времени у пользователя в данный день недели
        Cursor freeTime = database.query("free_time", null, "day_of_week = ? AND free_time_variant = ?",
                args, null, null, null);
        if(freeTime != null){
            if(freeTime.moveToFirst()){
                startFreeTime = freeTime.getString(freeTime.getColumnIndex("time_start"));
                stopFreeTime = freeTime.getString(freeTime.getColumnIndex("time_stop"));
                freeSplit = startFreeTime.split(":");
                int startMinutes, endHour, endMinutes;
                startFreeHour = Integer.parseInt(freeSplit[0]);
                startMinutes = Integer.parseInt(freeSplit[1]);
                String[] stopSplit = stopFreeTime.split(":");
                endHour = Integer.parseInt(stopSplit[0]);
                endMinutes = Integer.parseInt(stopSplit[1]);
                freeHours = endHour - startFreeHour;
                if(startMinutes > endMinutes){
                    freeHours--;
                }
            }
        }
        String [] args1 = new String[]{date};
        Cursor cursor = database.query("tasks_distribution", null, "task_date = ?", args1, null, null, "start_time");
        if(cursor != null){
            //Если у нас уже есть распределенные задачи на эти день
            if(cursor.moveToFirst()){
                do{
                    String startBusyTime = cursor.getString(cursor.getColumnIndex("start_time"));
                    String stopBusyTime = cursor.getString(cursor.getColumnIndex("stop_time"));
                    String [] split = startBusyTime.split(":");
                    int startHour, startMinutes, endHour, endMinutes;
                    startHour = Integer.parseInt(split[0]);
                    startMinutes = Integer.parseInt(split[1]);
                    split = stopBusyTime.split(":");
                    endHour = Integer.parseInt(split[0]);
                    endMinutes = Integer.parseInt(split[1]);
                    busyHour = endHour - startHour;
                    if(startMinutes > endMinutes){
                        busyHour++;
                    }
                    freeHours -= busyHour;
                }while(cursor.moveToNext());
                if(freeHours > 0){
                    cursor.moveToLast();
                    //Время конца предыдущей задачи - это время начала новой
                    String start_time = cursor.getString(cursor.getColumnIndex("stop_time"));
                    String [] split = start_time.split(":");
                    int startHour = Integer.parseInt(split[0]);
                    int endTime;
                    if(freeHours >= 3){
                        if(hours < 3){
                            endTime = (startHour + hours) % 24;
                            hours = 0;
                        } else {
                            hours -= 3;
                            endTime = (startHour + 3) % 24;
                        }
                        timingResults.add(new String[]{
                           start_time,
                           (endTime / 10) + "" + (endTime % 10) + ":" + split[1],
                           todayStr
                        });
                    } else {
                        if(hours < freeHours){
                            endTime = (startHour + hours) % 24;
                            hours = 0;
                        } else {
                            hours -= freeHours;
                            endTime = (startHour + freeHours) % 24;
                        }
                        timingResults.add(new String[]{
                                start_time,
                                (endTime / 10) + "" + (endTime % 10) + ":" + split[1],
                                todayStr
                        });
                    }
                }
            } else {
                if(freeHours >= 3){
                    int endTime;
                    if(hours < 3){
                        endTime = (startFreeHour + hours) % 24;
                        hours = 0;
                    } else {
                        endTime = (startFreeHour + 3) % 24;
                        hours -= 3;
                    }
                    timingResults.add(new String[]{
                            startFreeTime,
                            (endTime / 10) + "" + (endTime % 10) + ":" + freeSplit[1],
                            todayStr
                    });
                } else {
                    int endTime;
                    if(hours < freeHours){
                        endTime = (Integer.parseInt(freeSplit[0]) + hours) % 24;
                        hours = 0;
                    } else {
                        endTime = Integer.parseInt(freeSplit[0]) + freeHours;
                        hours -= freeHours;
                    }
                    timingResults.add(new String[]{
                            startFreeTime,
                            (endTime / 10) + "" + (endTime % 10) + ":" + freeSplit[1],
                            todayStr
                    });
                }
            }
        }
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
}
