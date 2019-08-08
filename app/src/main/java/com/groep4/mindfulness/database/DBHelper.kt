package com.groep4.mindfulness.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.groep4.mindfulness.database.DBHelper.Companion.TASK_TABLE_NAME
import java.text.ParseException
import java.text.SimpleDateFormat
import java.nio.file.Files.delete
import java.time.LocalDateTime
import java.util.*


class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 3) {


    val data: Cursor
        get() {
            val db = this.readableDatabase
            return db.rawQuery("select * from $TASK_TABLE_NAME order by id desc", null)

        }

    val dataToday: Cursor
        get() {
            val db = this.readableDatabase
            return db.rawQuery("select * from " + TASK_TABLE_NAME +
                    " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date('now', 'localtime') order by id desc", null)

        }


    val dataTomorrow: Cursor
        get() {
            val db = this.readableDatabase
            return db.rawQuery("select * from " + TASK_TABLE_NAME +
                    " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date('now', '+1 day', 'localtime')  order by id desc", null)

        }


    val dataUpcoming: Cursor
        get() {
            val db = this.readableDatabase
            return db.rawQuery("select * from " + TASK_TABLE_NAME +
                    " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) > date('now', '+1 day', 'localtime') order by id desc", null)

        }

    override fun onCreate(db: SQLiteDatabase) {
        // TODO Auto-generated method stub

        db.execSQL(
                "CREATE TABLE " + TASK_TABLE_NAME +
                        "(id TEXT PRIMARY KEY, task TEXT, dateStr INTEGER)"
        )
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS $TASK_TABLE_NAME")
        onCreate(db)
    }


    private fun getDate(day: String): Long {
        val dateFormat = SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault())
        var date = Date()
        try {
            date = dateFormat.parse(day)
        } catch (e: ParseException) {
        }

        return date.time
    }


    fun insertTask(task: String, dateStr: String): Boolean {
        val date = Calendar.getInstance().toString()
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("task", task)
        contentValues.put("dateStr", getDate(dateStr))
        contentValues.put("id",date)
        db.insert(TASK_TABLE_NAME, null, contentValues)
        return true
    }


    fun insertTaskwithid(id: String, task: String, dateStr: String): Boolean {
        val date: Date
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("id",id)
        contentValues.put("task", task)
        contentValues.put("dateStr", dateStr)
        db.insert(TASK_TABLE_NAME, null, contentValues)
        return true
    }

    fun updateTask(id: String, task: String, dateStr: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("task", task)
        contentValues.put("dateStr", getDate(dateStr))

        db.update(TASK_TABLE_NAME, contentValues, "id = ? ", arrayOf(id))
        return true
    }

    fun deleteTask(id:String?): Boolean{

        val db = this.writableDatabase
        val whereClause = "id=?"
        val whereArgs = arrayOf(id)
        db.delete(TASK_TABLE_NAME, whereClause, whereArgs)
        return true
    }

    fun getDataSpecific(id: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("select * from $TASK_TABLE_NAME WHERE id = '$id' order by id desc", null)

    }

    companion object {

        val DATABASE_NAME = "TasksDBHelper.db"
        val TASK_TABLE_NAME = "tasks"
    }
}
