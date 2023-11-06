package com.rtb.rtb.database.preferences

import android.content.Context

class SharedPrefs (context: Context) {

    private val key : String = "USER"
    private val sharedPrefs =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun setUserValue(value : Boolean) {
        sharedPrefs.edit().putBoolean(key, value).apply()
    }

    fun getUser() : Boolean {
        return sharedPrefs.getBoolean(key, false)
    }

}