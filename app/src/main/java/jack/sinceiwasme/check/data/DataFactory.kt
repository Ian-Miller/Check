package jack.sinceiwasme.check.data

import android.app.Application
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PositionalDataSource
import android.arch.persistence.room.Room
import android.content.Context
import java.util.concurrent.Executors

class DataFactory private constructor(context: Application) {
    private val executor = Executors.newSingleThreadExecutor();
    private val database: RecordDatabase = Room.databaseBuilder(context, RecordDatabase::class.java, RecordDatabase.name)
            .fallbackToDestructiveMigration()
            .build();
    val dao by lazy { database.dao() };

    fun get(): DataSource.Factory<Int,Record> = dao.get()
    fun add(vararg records: Record) {
        executor.execute {
            dao.add(*records)
        }
    }

    fun remove(vararg records: Record) {
        executor.execute {
            dao.remove(*records)
        }
    }

    companion object {
        private var factory: DataFactory? = null
        @JvmStatic
        fun instance(context: Context): DataFactory {
            if (factory == null) {
                synchronized(this) {
                    if (factory == null) {
                        factory = DataFactory(context.applicationContext as Application);
                    }
                }
            }
            return factory!!
        }
    }
}