package caribou;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class AssetFedCaribou implements Caribou {
    private static final String MIGRATION_PATH_FORMAT = "db/%s/migrate";

    private final Context context;
    private final String name;
    private final int age;

    public AssetFedCaribou(Context context, String name) {
        this.context = context;
        this.name = name;
        try {
            age = context.getAssets().list(String.format(MIGRATION_PATH_FORMAT, name)).length + 1;
        } catch (IOException ex) {
            throw new CaribouException("Exception trying to determine the database version", ex);
        }
    }

    @Override
    public void init(SQLiteDatabase db) {
        createMigrationsTable(db);
    }

    @Override
    public void migrate(SQLiteDatabase db) {
        try {
            insertMigrationsIntoTable(db);
            runMigrations(db);
        } catch (IOException ex) {
            throw new CaribouException("Exception during database migration", ex);
        }
    }

    @Override
    public void populate(SQLiteDatabase db) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void createMigrationsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE migrations ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "migration TEXT, "
                + "has_run INTEGER)");
    }

    private void insertMigrationsIntoTable(SQLiteDatabase db) throws IOException {
        String[] migrations = context.getAssets().list(getMigrationPath());
        for (String file : migrations) {
            runMigration(db, file);
            markMigrationRan(db, file);
        }
    }

    private void runMigration(SQLiteDatabase db, String migrationFilename) throws IOException {
        InputStream input = context.getAssets().open(getMigrationPath() + "/" + migrationFilename);
        Scanner scanner = new Scanner(input).useDelimiter(";");
        try {
            while (scanner.hasNext()) {
                String command = scanner.next().trim();
                if (!command.isEmpty()) {
                    db.execSQL(command);
                }
            }
        } catch (SQLException ex) {
            throw new CaribouException("Error performing migration: " + migrationFilename, ex);
        }
    }

    private void runMigrations(SQLiteDatabase db) throws IOException {
        Cursor c = getUnprocessedMigrations(db);

        int migrationIndex = c.getColumnIndex("migration");
        while (c.moveToNext()) {
            String migration = c.getString(migrationIndex);
            runMigration(db, migration);
            markMigrationRan(db, migration);
        }
        c.close();
    }

    private Cursor getUnprocessedMigrations(SQLiteDatabase db) {
        return db.query(
                "migrations",
                new String[]{"migration"},
                "has_run != ?",
                new String[]{String.valueOf(1)},
                null,
                null,
                "migration");
    }


    private void markMigrationRan(SQLiteDatabase db, String migration) {
        ContentValues values = new ContentValues(1);
        values.put("has_run", 1);
        db.update("migrations", values, "migration=?", new String[]{migration});
    }

    private String getMigrationPath() {
        return String.format(MIGRATION_PATH_FORMAT, name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }
}
