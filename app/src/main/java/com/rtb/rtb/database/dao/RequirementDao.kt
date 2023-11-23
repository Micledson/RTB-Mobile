package com.rtb.rtb.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rtb.rtb.model.Project
import com.rtb.rtb.model.Requirement
import java.util.UUID

@Dao
abstract class RequirementDao {

    @Insert
    abstract fun createRequirement(requirement : Requirement)

    @Update
    abstract fun updateRequirement(requirement: Requirement)

    @Query("SELECT * FROM requirement")
    abstract fun getRequirements() : MutableList<Requirement>

    @Query("SELECT * FROM requirement WHERE projectId = :uuid")
    abstract fun getRequirementsByProjectId(uuid: UUID) : MutableList<Requirement>

    @Query("SELECT * FROM requirement WHERE id = :uuid")
    abstract fun getRequirementById(uuid: UUID) : Requirement

    @Query("SELECT requirement.code FROM requirement WHERE projectId = :uuid ORDER BY requirement.code DESC LIMIT 1")
    abstract fun getLastRequirementCodeByProjectId(uuid: UUID) : Int

    @Delete
    abstract fun deleteRequirement(requirement: Requirement)
}
