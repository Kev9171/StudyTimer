package com.mp.mp_time.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.android.gms.maps.model.LatLng
import com.mp.mp_time.data.Place

class PlaceDBHelper (val context: Context
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        val DB_NAME = "place.db"
        val DB_VERSION = 1
        val TABLE_PLACE = "place"
        var PLACENAME = "placename" // 장소 이름
        var LATITUDE = "location" // 위도
        var LONGITUDE = "longitude" // 경도
        var COMMENT = "comment" // 한줄평
        var RATING = "rating" // 평점
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSql1 = "create table if not exists $TABLE_PLACE(" +
                "$PLACENAME text," +
                "$LATITUDE double," +
                "$LONGITUDE double," +
                "$COMMENT text," +
                "$RATING real);"
        db!!.execSQL(createTableSql1)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableSql1 = "drop table if exists $TABLE_PLACE;"
        db!!.execSQL(dropTableSql1)
        onCreate(db)
    }

    private fun getResultListFromCursor(cursor: Cursor): MutableList<Place> {
        cursor.moveToFirst()

        val results = mutableListOf<Place>()
        do {
            results.add(
                Place(
                    placeName = cursor.getString(0),
                    location = LatLng(cursor.getDouble(1), cursor.getDouble(2)),
                    comment = cursor.getString(3),
                    rating = cursor.getFloat(4)
                )
            )
        } while (cursor.moveToNext())

        return results
    }

    fun insertPlace(data: Place): Boolean {
        val values = ContentValues()
        values.put(PLACENAME, data.placeName)
        values.put(LATITUDE, data.location.latitude)
        values.put(LONGITUDE, data.location.longitude)
        values.put(COMMENT, data.comment)
        values.put(RATING, data.rating)

        val db = writableDatabase
        val flag = db.insert(TABLE_PLACE, null, values) > 0
        db.close()
        return flag
    }

    fun findPlaceByName(name: String): MutableList<Place>? {
        val selectSql = "select * from $TABLE_PLACE where $PLACENAME='$name';"
        val db = readableDatabase
        val cursor = db.rawQuery(selectSql, null)
        val flag = cursor.count != 0

        val results: MutableList<Place>? = if (flag) getResultListFromCursor(cursor) else null
        cursor.close()
        db.close()

        return results
    }

    fun findPlaceByLocation(location: LatLng): MutableList<Place>? {
        val lat = location.latitude
        val lng = location.longitude
        val selectSql = "select * from $TABLE_PLACE where $LATITUDE= '$lat' and $LONGITUDE = '$lng';"
        val db = readableDatabase
        val cursor = db.rawQuery(selectSql, null)
        val flag = cursor.count != 0

        val results: MutableList<Place>? = if (flag) getResultListFromCursor(cursor) else null
        cursor.close()
        db.close()

        return results
    }

    // 해당 name 의 장소 (모두) 삭제
    fun deletePlaceByName(name: String): Boolean {
        // 먼저 해당 place 가 존재하는지 확인
        val selectSql = "select * from $TABLE_PLACE where $PLACENAME='$name';"
        val db = writableDatabase
        val cursor = db.rawQuery(selectSql, null)
        val flag = cursor.count != 0
        if (flag) {
            cursor.moveToFirst()
            db.delete(TABLE_PLACE, "$PLACENAME=?", arrayOf(name))
        }
        cursor.close()
        db.close()
        return flag
    }

    // 해당 location 의 장소 (모두) 삭제
    fun deletePlaceByLocation(location: LatLng): Boolean {
        // 먼저 해당 place 가 존재하는지 확인
        val lat = location.latitude
        val lng = location.longitude
        val selectSql = "select * from $TABLE_PLACE where $LATITUDE='$lat' and $LONGITUDE = $lng;"
        val db = writableDatabase
        val cursor = db.rawQuery(selectSql, null)
        val flag = cursor.count != 0
        if (flag) {
            cursor.moveToFirst()
            db.delete(TABLE_PLACE, "$LATITUDE=? and $LONGITUDE=?",
                arrayOf(arrayOf(lat, lng).toString())
            )
        }
        cursor.close()
        db.close()
        return flag
    }

    fun findAll(): MutableList<Place>? {
        // 평점순으로 정렬하여 제공
        val selectAllSql = "select * from $TABLE_PLACE order by $RATING;"
        val db = readableDatabase
        val cursor = db.rawQuery(selectAllSql, null)
        val flag = cursor.count != 0

        val results: MutableList<Place>? = if (flag) getResultListFromCursor(cursor) else null
        cursor.close()
        db.close()

        return results
    }
}