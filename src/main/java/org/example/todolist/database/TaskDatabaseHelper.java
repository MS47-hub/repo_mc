
package org.example.todolist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "task_database.db";
    public static final int DATABASE_VERSION = 1;

    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " (" +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY, " +
                TaskContract.TaskEntry.COLUMN_TASK_NAME + " TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}
