package DataBase;

import com.orm.SugarRecord;

public class FreeTime extends SugarRecord<FreeTime> {
    public int dayOfWeek, variant;
    public String timeStart, timeStop;
    public FreeTime(){

    }
}
