package com.rtb.rtb.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_1 = object : Migration(1, 1) {
    override fun migrate(database: SupportSQLiteDatabase) {
        val sql = """ 
            CREATE TABLE IF NOT EXISTS `User` (
            `email` TEXT NOT NULL, 
            `first_name` TEXT NOT NULL, 
            `last_name` TEXT NOT NULL, 
            `password` TEXT NOT NULL, 
            PRIMARY KEY(`email`));
        """

        database.execSQL(sql)
    }
}