package caribou;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CaribouHelper extends SQLiteOpenHelper {

    private final Caribou caribou;

    public CaribouHelper(Context context, String name) {
        this(context, new AssetFedCaribou(context, name));
    }

    private CaribouHelper(Context context, Caribou caribou) {
        super(context, caribou.getName() + ".sqlite", null, caribou.getAge());
        this.caribou = caribou;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        caribou.init(db);
        caribou.migrate(db);
        caribou.populate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        caribou.migrate(db);
    }
}