package com.mp.mp_time.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mp.mp_time.data.Schedule

class ScheduleDBHelper (
    val context: Context
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){

    companion object {
        val DB_NAME = "schedule.db"
        val DB_VERSION = 1
        val TABLE_SCHEDULE = "schedule"
        var DATE = "date" // 일정 날짜
        var TITLE = "title" // 일정 제목
        var CONTENT = "content" // 상세 일정
        var DDAY = "dday" // D-Day 설정 여부
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSql1 = "create table if not exists $TABLE_SCHEDULE(" +
                "$DATE text," +
                "$TITLE text," +
                "$CONTENT text," +
                "$DDAY integer);"
        db!!.execSQL(createTableSql1)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableSql1 = "drop table if exists $TABLE_SCHEDULE;"
        db!!.execSQL(dropTableSql1)
        onCreate(db)
    }

    private fun getResultListFromCursor(cursor: Cursor): MutableList<Schedule>{
        cursor.moveToFirst()

        val results = mutableListOf<Schedule>()
        do {
            results.add(
                Schedule(
                    date = cursor.getString(0),
                    title = cursor.getString(1),
                    content = cursor.getString(2),
                    dDay = cursor.getString(3).toInt() == 1)
            )
        } while(cursor.moveToNext())

        return results
    }

    fun insertSchedule(data: Schedule): Boolean {
        val values = ContentValues()
        values.put(DATE, data.date)
        values.put(TITLE, data.title)
        values.put(CONTENT, data.content)
        values.put(DDAY, if(data.dDay) 1 else 0) // true:1 , false:0

        val db = writableDatabase
        val flag = db.insert(TABLE_SCHEDULE, null, values) > 0
        db.close()
        return flag
    }

    fun findScheduleByDate(date: String): MutableList<Schedule>? {
        val selectSql = "select * from $TABLE_SCHEDULE where $DATE='$date';"
        val db = readableDatabase
        val cursor = db.rawQuery(selectSql, null)
        val flag = cursor.count != 0

        val results: MutableList<Schedule>? = if(flag) getResultListFromCursor(cursor) else null
        cursor.close()
        db.close()

        return results
    }

    fun findDdaySchedule(): MutableList<Schedule>? {
        val selectSql = "select * from $TABLE_SCHEDULE where $DDAY=1;"
        val db = readableDatabase
        val cursor = db.rawQuery(selectSql, null)
        val flag = cursor.count != 0

        val results: MutableList<Schedule>? = if(flag) getResultListFromCursor(cursor) else null
        cursor.close()
        db.close()

        return results
    }

    // date, title 정보로 해당 일정 삭제
    fun deleteScheduleByDateAndTitle(date: String, title: String): Boolean {
        // 먼저 해당 schedule 이 존재하는지 확인
        val selectSql = "select * from $TABLE_SCHEDULE where $DATE='$date' and $TITLE='$title';"
        val db = writableDatabase
        val cursor = db.rawQuery(selectSql, null)
        val flag = cursor.count != 0
        if(flag){
            cursor.moveToFirst()
            db.delete(TABLE_SCHEDULE, "$DATE=? and $TITLE=?", arrayOf(date, title))
        }
        cursor.close()
        db.close()
        return flag
    }

    // 해당 date 의 일정 (모두) 삭제
    fun deleteScheduleByDate(date: String): Boolean {
        // 먼저 해당 schedule 이 존재하는지 확인
        val selectSql = "select * from $TABLE_SCHEDULE where $DATE='$date';"
        val db = writableDatabase
        val cursor = db.rawQuery(selectSql, null)
        val flag = cursor.count != 0
        if(flag){
            cursor.moveToFirst()
            db.delete(TABLE_SCHEDULE, "$DATE=?", arrayOf(date))
        }
        cursor.close()
        db.close()
        return flag
    }

    fun findAll(): MutableList<Schedule>?{
        // 날짜로 정렬하여 제공
        val selectAllSql = "select * from $TABLE_SCHEDULE order by $DATE;"
        val db = readableDatabase
        val cursor = db.rawQuery(selectAllSql, null)
        val flag = cursor.count != 0

        val results: MutableList<Schedule>? = if(flag) getResultListFromCursor(cursor) else null
        cursor.close()
        db.close()

        return results
    }
}