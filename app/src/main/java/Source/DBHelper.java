package Source;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "time_manager_db";

    public DBHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists tasks(ID integer primary key autoincrement," +
                "task_deadline text," +
                "task_run_time integer," +
                "task_comment text," +
                "task_priority integer" +
                ")");
        sqLiteDatabase.execSQL("create table if not exists free_time(ID integer primary key autoincrement," +
                "day_of_week integer," +
                "time_start text," +
                "time_stop text," +
                "free_time_variant int" +
                ")");
        sqLiteDatabase.execSQL("create table if not exists tasks_distribution(ID integer primary key autoincrement," +
                "task_id int," +
                "start_time text," +
                "stop_time text," +
                "task_date text)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists tasks");
        onCreate(sqLiteDatabase);

    }

}
