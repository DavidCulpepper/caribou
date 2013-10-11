package caribou;

import android.database.sqlite.SQLiteDatabase;

public interface Caribou {
    void init(SQLiteDatabase db);
    void migrate(SQLiteDatabase db);
    void populate(SQLiteDatabase db);
    String getName();
    int getAge();
}
