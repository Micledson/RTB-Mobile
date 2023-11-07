package com.rtb.rtb.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rtb.rtb.model.Requirement
import java.util.UUID

@Dao
abstract class RequirementDao {

    @Insert
    abstract fun createRequirement(requirement : Requirement)

    @Update
    abstract fun updateRequirement(requirement: Requirement)

    @Query("SELECT * FROM requirement WHERE id = :uuid")
    abstract fun getRequirementById(uuid: UUID) : Requirement
}
