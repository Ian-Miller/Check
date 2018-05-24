package jack.sinceiwasme.check.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.text.NumberFormat;

@Entity
public class Record {
    @PrimaryKey(autoGenerate = true)
    public int _id;
    public long time;
    public double record;
    public String label = "unlabelled";

    @Ignore
    public String primaryDisplay = null;

    public String getDisplay(){
        if (primaryDisplay != null){
            return primaryDisplay;
        }
        return String.valueOf(record);
    }

    public Record() {
        time = System.currentTimeMillis();
    }

    @Ignore
    public Record(String display) {
        this();
        this.primaryDisplay = display;
    }

    @Ignore
    public Record(double record){
        this();
        this.record = record;
    }

    public static String getFormattedNumber(double d){
        try {
            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(2);
            format.setMinimumFractionDigits(2);
            return format.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
    }
}
