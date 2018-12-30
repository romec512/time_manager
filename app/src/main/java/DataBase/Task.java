package DataBase;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

@Table(name = "tasks")
public class Task extends SugarRecord{
    @Column(name = "task_deadline")
    public String deadline;
    @Column(name = "task_comment")
    public String comment;
    @Column(name = "task_run_time")
    public int runTime;
    @Column(name = "task_priority")
    public int priority;
    public Task(){

    }

    public Task(String _deadline, String _comment, int _runTime, int _priority){
        deadline = _deadline;
        comment = _comment;
        runTime = _runTime;
        priority = _priority;
    }

}
