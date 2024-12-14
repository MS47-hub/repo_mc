
package org.example.todolist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.example.todolist.database.TaskDatabaseHelper;
import org.example.todolist.database.TaskContract;

import java.util.ArrayList;

public class TaskManagerActivity extends AppCompatActivity {

    private TaskDatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> taskList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        dbHelper = new TaskDatabaseHelper(this);
        taskList = new ArrayList<>();
        listView = findViewById(R.id.task_list_view);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        listView.setAdapter(adapter);

        loadTasksFromDatabase();
    }

    private void loadTasksFromDatabase() {
        taskList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String task = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME));
            taskList.add(task);
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_manager_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_task) {
            showAddTaskDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddTaskDialog() {
        final EditText taskInput = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("New Task")
                .setMessage("What task would you like to add?")
                .setView(taskInput)
                .setPositiveButton("Add", (dialog, which) -> {
                    String task = taskInput.getText().toString().trim();
                    if (!task.isEmpty()) {
                        saveTaskToDatabase(task);
                    } else {
                        Toast.makeText(this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveTaskToDatabase(String taskName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TASK_NAME, taskName);
        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
        db.close();
        loadTasksFromDatabase();
    }

    public void deleteTask(View view) {
        String taskName = (String) view.getTag();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE_NAME, TaskContract.TaskEntry.COLUMN_TASK_NAME + "=?", new String[]{taskName});
        db.close();
        loadTasksFromDatabase();
        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
    }
}
