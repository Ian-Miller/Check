package jack.sinceiwasme.check.data;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface RecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void add(Record... records);

    @Delete()
    public void remove(Record... records);

    @Query(value = "SELECT * FROM Record ORDER BY time")
    public DataSource.Factory<Integer,Record> get();
}
