package jack.sinceiwasme.check.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Record.class},version = 2)
public abstract class RecordDatabase extends RoomDatabase {
    public static final String name = "RecordDatabase";
    public abstract RecordDao dao();
}
