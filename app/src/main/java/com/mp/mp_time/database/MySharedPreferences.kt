package com.mp.mp_time.database

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {
    private val prefsFilename = "prefs"
    private val prefsKeyEdt = "myEditText"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename,0)

    //var myEditText: String? = ""
    fun get() = prefs.getString(prefsKeyEdt, "")
    fun set(value:String) = prefs.edit().putString(prefsKeyEdt, value).apply()
}