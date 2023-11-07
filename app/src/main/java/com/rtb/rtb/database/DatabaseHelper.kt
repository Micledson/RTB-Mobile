package com.rtb.rtb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rtb.rtb.database.dao.ProjectDao
import com.rtb.rtb.model.User
import com.rtb.rtb.database.dao.UserDao
import com.rtb.rtb.database.migration.MIGRATION_1_2
import com.rtb.rtb.model.Project
import com.rtb.rtb.util.converter.Converter

@Database(
    entities = [
        Project::class,
        User::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converter::class)
abstract class DatabaseHelper : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun projectDao(): ProjectDao

    companion object {
        fun getInstance(context: Context): DatabaseHelper {
            return Room.databaseBuilder(context, DatabaseHelper::class.java, "rtb.db")
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}