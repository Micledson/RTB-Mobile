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

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        val sql = """
           CREATE TABLE IF NOT EXISTS `Project` (
            `id` TEXT NOT NULL,
            `name` TEXT NOT NULL,
            `alias` TEXT NOT NULL,
            `description` TEXT NOT NULL,
            `is_active` BOOLEAN NOT NULL,
            `created_at` TEXT NOT NULL,
            `updated_at` TEXT NOT NULL,
            `deleted_at` TEXT NOT NULL,
            PRIMARY KEY(`id`));
        """

        database.execSQL(sql)
    }
}