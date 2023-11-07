package com.rtb.rtb.util.converter

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    @TypeConverter
    fun stringToDate(value: String?): Date? {
        return value?.let { dateFormat.parse(value) }
    }

    @TypeConverter
    fun dateToString(date: Date?): String? {
        return date?.let { dateFormat.format(it) }
    }
}