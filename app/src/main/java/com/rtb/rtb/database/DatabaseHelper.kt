package com.rtb.rtb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rtb.rtb.database.dao.RequirementDao
import androidx.room.TypeConverters
import com.rtb.rtb.database.dao.ProjectDao
import com.rtb.rtb.model.User
import com.rtb.rtb.database.dao.UserDao
import com.rtb.rtb.model.Project
import com.rtb.rtb.util.converter.Converter
import com.rtb.rtb.database.migration.MIGRATION_1_3
import com.rtb.rtb.model.Requirement

@Database(
    entities = [
        User::class,
        Project::class,
        Requirement::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converter::class)
abstract class DatabaseHelper : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun projectDao(): ProjectDao
    abstract fun requirementDao(): RequirementDao

    companion object {
        fun getInstance(context: Context): DatabaseHelper {
            return Room.databaseBuilder(context, DatabaseHelper::class.java, "rtb.db")
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_3)
                .build()
        }
    }
}