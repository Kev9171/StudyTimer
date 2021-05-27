package com.mp.mp_time.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mp.mp_time.data.Subject

class SubjectDBHelper(
    val context: Context
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){

    companion object {
        val DB_NAME = "subject.db"
        val DB_VERSION = 1
        val TABLE_SUBJECT = "subject"
        var SUBNAME = "subname"// 과목명
        var ISPAGE = "ispage" // goalInt 가 의미하는 것이 페이지 수 인지, 시간 단위인지 구별
        var GOAL = "goal" // 목표 페이지 수 or 시간
        val STUDYTIME = "studytime" // 공부할 시간. 1.0 = 1시간
        val BREAKTIME = "breaktime" // 쉴 시간
        var DATE = "date"   // 날짜
        var ACHIEVEDTIME = "achievedtime" // 공부한 시간
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSql1 = "create table if not exists $TABLE_SUBJECT(" +
                "$SUBNAME text," +
                "$ISPAGE integer," +
                "$GOAL integer," +
                "$STUDYTIME real," +
                "$BREAKTIME real," +
                "$DATE text," +
                "$ACHIEVEDTIME text" +
                ");"
        db!!.execSQL(createTableSql1)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableSql1 = "drop table if exists $TABLE_SUBJECT;"
        db!!.execSQL(dropTableSql1)
        onCreate(db)
    }

    private fun getResultListFromCursor(cursor: Cursor): MutableList<Subject>{
        cursor.moveToFirst()

        val results = mutableListOf<Subject>()
        do {
            results.add(Subject(
                subName = cursor.getString(0),
                isPage = cursor.getString(1).toInt() == 1,
                goalInt = cursor.getString(2).toInt(),
                studyTime = cursor.getString(3).toFloat(),
                breakTime = cursor.getString(4).toFloat(),
                date = cursor.getString(5),
                achievedTime = cursor.getString(6)
            ))
        } while(cursor.moveToNext())

        return results
    }

    fun insertSubject(data: Subject): Boolean {
        val values = ContentValues()
        values.put(SUBNAME, data.subName)
        values.put(ISPAGE, if(data.isPage) 1 else 0) // true:1 , false:0
        values.put(GOAL, data.goalInt)
        values.put(STUDYTIME, data.studyTime)
        values.put(BREAKTIME, data.breakTime)
        values.put(DATE, data.date)
        values.put(ACHIEVEDTIME, data.achievedTime)

        val db = writableDatabase
        val flag = db.insert(TABLE_SUBJECT, null, values) > 0
        db.close()
        return flag
    }

    fun findSubjectByName(subName: String): MutableList<Subject>? {
        val selectSql = "select * from $TABLE_SUBJECT where $SUBNAME='$subName';"
        val db = readableDatabase
        val cursor = db.rawQuery(selectSql, null)
        val flag = cursor.count != 0

        val results: MutableList<Subject>? = if(flag) getResultListFromCursor(cursor) else null
        cursor.close()
        db.close()

        return results
    }

    fun findSubjectByDate(date: String): MutableList<Subject>? {
        val selectSql = "select * from $TABLE_SUBJECT where $DATE='$date';"
        val db = readableDatabase
        val cursor = db.rawQuery(selectSql, null)
        val flag = cursor.count != 0

        val results: MutableList<Subject>? = if(flag) getResultListFromCursor(cursor) else null
        cursor.close()
        db.close()

        return results
    }


    fun deleteSubjectByName(subject: String): Boolean {
        // 먼저 해당 subject 가 존재하는지 확인
        val selectSql = "select * from $TABLE_SUBJECT where $SUBNAME='$subject';"
        val db = writableDatabase
        val cursor = db.rawQuery(selectSql, null)
        val flag = cursor.count != 0
        if(flag){
            cursor.moveToFirst()
            db.delete(TABLE_SUBJECT, "$SUBNAME=?", arrayOf(subject))
        }
        cursor.close()
        db.close()
        return flag
    }

    fun deleteSubjectByDate(date: String): Boolean {
        // 먼저 해당 subject 가 존재하는지 확인
        val selectSql = "select * from $TABLE_SUBJECT where $DATE='$date';"
        val db = writableDatabase
        val cursor = db.rawQuery(selectSql, null)
        val flag = cursor.count != 0
        if(flag){
            cursor.moveToFirst()
            db.delete(TABLE_SUBJECT, "$DATE=?", arrayOf(date))
        }
        cursor.close()
        db.close()
        return flag
    }

    fun findAll(): MutableList<Subject>?{
        // 과목명으로 정렬하여 제공
        val selectAllSql = "select * from $TABLE_SUBJECT order by $SUBNAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(selectAllSql, null)
        val flag = cursor.count != 0

        val results: MutableList<Subject>? = if(flag) getResultListFromCursor(cursor) else null
        cursor.close()
        db.close()

        return results
    }
}