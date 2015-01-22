package com.codepath.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Himanshu on 1/20/2015.
 */
public class TodoTaskDatabase extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "todoListDatabase";

    // Todo table name
    private static final String TABLE_TODO = "todo_tasks";

    // Todo Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DUE_DATE = "dueDate";

    public TodoTaskDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating our initial tables
    // These is where we need to write create table statements.
    // This is called when database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Construct a table for todo tasks
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_DUE_DATE + " TEXT" + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    // Upgrading the database between versions
    // This method is called when database is upgraded like modifying the table structure,
    // adding constraints to database, etc
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 1) {
            // Wipe older tables if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            // Create tables again
            onCreate(db);
        }
    }

    // Insert record into the database
    public void addTask(Task task) {
        // Open database connection
        SQLiteDatabase db = this.getWritableDatabase();
        // Define values for each field
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_DUE_DATE, task.getDueDate());
        // Insert Row
        db.insertOrThrow(TABLE_TODO, null, values);
        db.close(); // Closing database connection
    }

    // Returns a single todo task by id
    public Task getTask(int id) {
        // Open database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Construct and execute query
        Cursor cursor = db.query(TABLE_TODO,  // TABLE
                new String[] { KEY_ID, KEY_TITLE, KEY_DUE_DATE}, // SELECT
                KEY_ID + "= ?", new String[] { String.valueOf(id) },  // WHERE, ARGS
                null, null, null, null); // GROUP BY, HAVING, ORDER BY
        if (cursor != null)
            cursor.moveToFirst();
        // Load result into model object
        Task task = new Task(cursor.getString(1), cursor.getString(2));
        task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
        // return todo task
        return task;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> todoItems = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TODO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(cursor.getString(1), cursor.getString(2));
                task.setId(cursor.getInt(0));
                // Adding todo task to list
                todoItems.add(task);
            } while (cursor.moveToNext());
        }

        // return todo list
        return todoItems;
    }

    public int getTaskCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public int updateTask(Task task) {
        // Open database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Setup fields to update
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_DUE_DATE, task.getDueDate());
        // Updating row
        int result = db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getId()) });
        // Close the database
        db.close();
        return result;
    }

    public void deleteTask(Task task) {
        // Open database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the record with the specified id
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getId()) });
        // Close the database
        db.close();
    }

}