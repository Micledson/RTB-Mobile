package com.rtb.rtb.database.preferences

import android.content.Context

class SharedPrefs (context: Context) {

    private val userKey : String = "USER"
    private val emailKey : String = "EMAIL"
    private val sharedPrefs =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun setUserValue(value : Boolean) {
        sharedPrefs.edit().putBoolean(userKey, value).apply()
    }

    fun getUser() : Boolean {
        return sharedPrefs.getBoolean(userKey, false)
    }

    fun setUserEmail(value : String) {
        sharedPrefs.edit().putString(emailKey, value).apply()
    }

    fun getUserEmail() : String? {
        return sharedPrefs.getString(emailKey, null)
    }

}