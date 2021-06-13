package com.mp.mp_time.database

import android.content.Context
import android.content.SharedPreferences
import com.mp.mp_time.R

class MySharedPreferences(context: Context) {
    private val prefsFilename = "prefs"
    private val prefsKeyEdt = "myEditText"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename,0)

    //var myEditText: String? = ""
    fun get() = prefs.getString(prefsKeyEdt, "")
    fun set(value:String) = prefs.edit().putString(prefsKeyEdt, value).apply()

    fun getUserTheme(): Int {
        val prefsString: String = prefs.getString(prefsKeyEdt, "").toString()
        val selectedTheme = if(prefsString.isEmpty()) 0 else prefsString.toInt()

        return when(selectedTheme){
            0-> R.style.Theme_MPTIME_light_pink
            1-> R.style.Theme_MPTIME_light_green
            2-> R.style.Theme_MPTIME_dark_blue
            3-> R.style.Theme_MPTIME_dark
            4 -> R.style.Theme_MPTIME_dark_red
            else -> R.style.Theme_MPTIME_dark
        }
    }
}