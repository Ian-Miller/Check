package jack.sinceiwasme.check.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import jack.sinceiwasme.check.data.DataFactory;
import jack.sinceiwasme.check.data.Record;

public class MyViewModel extends AndroidViewModel {
    public final LiveData<PagedList<Record>> records;

    public MyViewModel(@NonNull Application application) {
        super(application);
        records = new LivePagedListBuilder<Integer, Record>(
                DataFactory.instance(application).getDao().get(),
                30)
                .build();
    }
}
