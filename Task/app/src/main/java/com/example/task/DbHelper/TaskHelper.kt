package com.example.task.DbHelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.task.model.Task

class TaskHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    companion object {
        private const val DATABASE_NAME: String = "task.db"
        private const val VERSION: Int = 1
        private const val TABLE_NAME = "task"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TASK = "task"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_STATUS = "status"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_TITLE TEXT," +
                    "$COLUMN_TASK TEXT," +
                    "$COLUMN_DATE TEXT," +
                    "$COLUMN_STATUS BIT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(title: String, task: String, date: String, status: Boolean): Long {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TASK, task)
        contentValues.put(COLUMN_TITLE, title)
        contentValues.put(COLUMN_DATE, date)
        contentValues.put(COLUMN_STATUS, status)
        return db.insert(TABLE_NAME, null, contentValues)
    }

    @SuppressLint("Recycle")
    fun getAllData(): List<Task> {
        val listTask = mutableListOf<Task>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val task = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val status = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS))
                listTask.add(Task(id, title, task, date, status))
            } while (cursor.moveToNext())
        }
        return listTask
    }

    fun getDataById(Id: Int): Task {
        val db = readableDatabase
        var id: Int = 0
        var task: String = "asd"
        var title: String = "asd"
        var date: String = "asd"
        var status: Int = 0
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $Id", null)
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                task = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK))
                title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                status = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATUS))

            } while (cursor.moveToNext())
        }

        val getTask = Task(id, title, task, date, status)

        return getTask
    }

    fun updateData(id: Int, title: String, task: String, date: String): Int {
        val db = readableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TASK, task)
        contentValues.put(COLUMN_TITLE, title)
        contentValues.put(COLUMN_DATE, date)
        val condition = "$COLUMN_ID = $id"
        return db.update(TABLE_NAME, contentValues, condition, null)
    }

    fun deleteData(id: Int): Int {
        val condition = "$COLUMN_ID = $id"
        val db = readableDatabase
        return db.delete(TABLE_NAME, condition, null)
    }

    fun finishTask(id : Int) : Int{
        val db = readableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_STATUS, true)
        val condition = "$COLUMN_ID = $id"
        return db.update(TABLE_NAME, contentValues, condition, null)
    }

}