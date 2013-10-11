package caribou;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CaribouHelper extends SQLiteOpenHelper {

    private final Caribou caribou;

    private CaribouHelper(Context context, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler, Caribou caribou) {
        super(context, caribou.getName() + ".sqlite", factory, caribou.getAge(), errorHandler);
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

    public CaribouHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        this(context, factory, errorHandler, new AssetFedCaribou(context, name));
    }

    public CaribouHelper(Context context, String name) {
        this(context, name, null, null);
    }

    public CaribouHelper(Context context, String name, DatabaseErrorHandler errorHandler) {
        this(context, name, null, errorHandler);
    }

    public CaribouHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        this(context, name, factory, null);
    }
}