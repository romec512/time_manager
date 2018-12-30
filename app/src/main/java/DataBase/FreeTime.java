package DataBase;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

@Table(name="free_time")
public class FreeTime extends SugarRecord {
    @Column(name = "day_of_week")
    public int dayOfWeek;
    @Column(name="free_time_variant")
    public int variant;
    @Column(name = "time_start")
    public String timeStart;
    @Column(name = "time_stop")
    public String timeStop;
    public FreeTime(){

    }

    public FreeTime(int _dayOfWeek, int _variant, String _timeStart, String _timeStop){
        dayOfWeek = _dayOfWeek;
        variant = _variant;
        timeStart = _timeStart;
        timeStop = _timeStop;
    }
}
