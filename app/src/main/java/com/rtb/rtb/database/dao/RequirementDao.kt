package com.rtb.rtb.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.rtb.rtb.model.Requirement

@Dao
abstract class RequirementDao {

    @Insert
    abstract fun save(requirement : Requirement): Long
}