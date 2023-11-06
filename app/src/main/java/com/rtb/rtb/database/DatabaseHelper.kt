package com.rtb.rtb.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rtb.rtb.model.User
import com.rtb.rtb.database.dao.UserDao
import com.rtb.rtb.database.migration.MIGRATION_1_1

@Database(
    entities = [
        User::class
    ],
    version = 1,
    exportSchema = true
)
abstract class DatabaseHelper : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        fun getInstance(context: Context): DatabaseHelper {
            return Room.databaseBuilder(context, DatabaseHelper::class.java, "rtb.db")
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_1)
                .build()
        }
    }
}