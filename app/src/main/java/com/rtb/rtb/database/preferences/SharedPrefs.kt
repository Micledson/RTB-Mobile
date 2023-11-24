package com.rtb.rtb.database.preferences

import android.content.Context
import com.google.gson.Gson
import com.rtb.rtb.model.Resource

class SharedPrefs (context: Context) {

    private val userKey : String = "USER"
    private val emailKey : String = "EMAIL"
    private val resourcesKey: String = "RESOURCES"
    private val sharedPrefs =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun setUserValue(value : Boolean) {
        sharedPrefs.edit().putBoolean(userKey, value).apply()
    }

    fun getUser() : Boolean {
        return sharedPrefs.getBoolean(userKey, false)
    }

    fun setUserEmail(value : String?) {
        sharedPrefs.edit().putString(emailKey, value).apply()
    }

    fun getUserEmail() : String? {
        return sharedPrefs.getString(emailKey, null)
    }

    fun saveResources(resource: Resource) {
        val resourcesJson = gson.toJson(resource)
        sharedPrefs.edit().putString(resourcesKey, resourcesJson).apply()
    }

    fun getResources(): Resource? {
        val resourcesJson = sharedPrefs.getString(resourcesKey, null)
        return gson.fromJson(resourcesJson, Resource::class.java)
    }

}