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
import java.util.List;

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

        //Считываем количество свободного времени из таблицы
        Cursor freeTime = database.query("free_time", null, "day_of_week = ? AND free_time_variant = ?",
                args, null, null, null);
            if (freeTime != null) {
                if (freeTime.moveToFirst()) {
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
                    if(startFreeHour == 0 && endHour == 0){ // Если время свободное время не указано
                        isPossible = DistributionState.FREE_TIME_IS_EMPTY;
                        return;
                    }
                    if (startMinutes > endMinutes) {
                        freeHours--;
                    }
                    Date todayDate = new Date();
                    String todayDateStr = dateFormat.format(todayDate);
                    if(date.compareTo(todayDateStr) == 0){
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        int currentHour = Integer.parseInt(timeFormat.format(todayDate).split(":")[0]);
                        if(currentHour < endHour && currentHour > startFreeHour){
                            freeHours = endHour - currentHour;
                        } else if(currentHour >= endHour){
                            freeHours = 0;
                        }
                    }
                }
            }

        String [] args1 = new String[]{date};
        Cursor cursor = database.query("tasks_distribution", null, "task_date = ?", args1, null, null, "start_time");
        //Для корректировки количества свободного узнаем действительно ли: данный день сегодня?
        //Локально найдем сегодняшнюю дату
        Date realdate = new Date();
        SimpleDateFormat realdateformat = new SimpleDateFormat("dd-MM-YYYY");
        String strrealdate = realdateformat.format(realdate);
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
                    int startMinutes = Integer.parseInt(split[1]);
                    int endTime;
                    String strrealtime;
                    Date realtime = new Date();
                    SimpleDateFormat realtimeformat = new SimpleDateFormat("HH:mm");
                    strrealtime = realtimeformat.format(realtime);
                    String [] realsplit = strrealtime.split(":");
                    int realHour = Integer.parseInt(realsplit[0]);
                    int realMinutes = Integer.parseInt(realsplit[1]);
                    //Узнаем выбранный день-сегодня?
                    if(date.compareTo(strrealdate) == 0) {
                        //Сравниваем текущее время с временем начала свободного времени сегодняшнего дня(для добавления задачи на сегодняшний день)
                        if (realHour == startHour) {
                            if (realMinutes > startMinutes) {
                                startHour++;
                                //Корректируем количество свободного времени
                                freeHours -= 1;
                            }
                        } else if (realHour > startHour) {
                            int endFreeTime = Integer.parseInt(stopFreeTime.split(":")[0]);
                            freeHours = endFreeTime - realHour;
                            startHour = realHour;
                            //Корректируем количество свободного времени
                            if (realMinutes > startMinutes) {
                                startHour++;
                                //Корректируем количество свободного времени
                                freeHours -= 1;
                            }
                        }
                    }

                    if(freeHours >= 3){
                        if(hours < 3){
                            endTime = (startHour + hours) % 24;
                            hours = 0;
                        } else {
                            if(impossibleTime == 0) {
                                hours -= 3;
                                endTime = (startHour + 3) % 24;
                            } else { //Если алгоритм не смог найти распределение по 3 часа и у него осталось "лишнее" время
                                int distr = (int)((freeHours - 3) * distrRatio + 3);
                                if(distr > hours){
                                    distr = hours;
                                }
                                hours -= distr;
                                endTime = (startHour + distr) % 24;
                                impossibleTime -= distr;
                            }
                        }
                        timingResults.add(new String[]{
                                (startHour / 10) + "" + (startHour % 10) + ":" + split[1],
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
                                (startHour / 10) + "" + (startHour % 10) + ":" + split[1],
                                (endTime / 10) + "" + (endTime % 10) + ":" + split[1],
                                todayStr
                        });
                    }
                }
            } else {
                //Если день полностью свободен
                String start_time = freeTime.getString(freeTime.getColumnIndex("time_start"));
                String [] split = start_time.split(":");
                String end_time = freeTime.getString(freeTime.getColumnIndex("time_stop"));
                String [] endFreeTimeSplit = end_time.split(":");
                int startMinutes = Integer.parseInt(split[1]);
                int endFreeHours = Integer.parseInt(endFreeTimeSplit[0]);
                String strrealtime;
                Date realtime = new Date();
                SimpleDateFormat realtimeformat = new SimpleDateFormat("HH:mm");
                strrealtime = realtimeformat.format(realtime);
                String [] realsplit = strrealtime.split(":");
                int realHour = Integer.parseInt(realsplit[0]);
                int realMinutes = Integer.parseInt(realsplit[1]);
                //Узнаем выбранный день-сегодня?
                if(date.compareTo(strrealdate) == 0){
                    if (realHour == startFreeHour) {
                        if (realMinutes > startMinutes) {
                            startFreeHour++;
                            //Корректируем количество свободного времени
                            freeHours--;
                        }
                    } else if (realHour > startFreeHour) {
                        freeHours =  endFreeHours - realHour;
                        startFreeHour = realHour;
                        //Корректируем количество свободного времени
                        if (realMinutes > startMinutes) {
                            startFreeHour++;
                            //Корректируем количество свободного времени
                            freeHours--;
                        }
                    }
                }
                if(freeHours >= 3){
                    int endTime;
                    if(hours < 3){
                        endTime = (startFreeHour + hours) % 24;
                        hours = 0;
                    } else {
                        if(impossibleTime == 0) {
                            hours -= 3;
                            endTime = (startFreeHour + 3) % 24;
                        } else { //Если алгоритм не смог найти распределение по 3 часа и у него осталось "лишнее" время
                            int distr = (int)((freeHours - 3) * distrRatio + 3);
                            if(distr > hours) {
                                distr = hours;
                            }
                            hours -= distr;
                            endTime = (startFreeHour + distr) % 24;
                            impossibleTime -= distr;
                        }
                    }
                    timingResults.add(new String[]{
                            (startFreeHour / 10) + "" + (startFreeHour % 10) + ":" + freeSplit[1],
                            (endTime / 10) + "" + (endTime % 10) + ":" + freeSplit[1],
                            todayStr
                    });
                } else {
                    int endTime;
                    if(hours < freeHours){
                        endTime = (startFreeHour + hours) % 24;
                        hours = 0;
                    } else {
                        endTime = startFreeHour + freeHours;
                        hours -= freeHours;
                    }
                    timingResults.add(new String[]{
                            (startFreeHour / 10) + "" + (startFreeHour % 10) + ":" + freeSplit[1],
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
}
