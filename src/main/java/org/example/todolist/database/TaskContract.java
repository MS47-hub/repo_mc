
package org.example.todolist.database;

import android.provider.BaseColumns;

public final class TaskContract {
    private TaskContract() {}

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TASK_NAME = "task_name";
    }
}
