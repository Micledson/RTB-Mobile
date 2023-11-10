package com.rtb.rtb.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rtb.rtb.model.Project
import java.util.UUID

@Dao
abstract class ProjectDao {
    @Insert
    abstract fun createProject(myProject : Project)

    @Query("SELECT * FROM project WHERE id = :uuid AND deletedAt IS NULL")
    abstract fun getProjectByUUID(uuid: UUID) : Project

    @Query("SELECT * FROM project WHERE deletedAt IS NULL AND owner = :owner ORDER BY name")
    abstract fun getProjects(owner: String) : MutableList<Project>

    @Query("SELECT * FROM project WHERE isActive = :isActive AND deletedAt IS NULL  AND owner = :owner ORDER BY name")
    abstract fun getProjectsByIsActive(isActive : Boolean, owner: String) : MutableList<Project>

    @Query("SELECT * FROM project WHERE name LIKE '%' || :name || '%' AND deletedAt IS NULL AND owner = :owner ORDER BY name")
    abstract fun getProjectsByName(name : String, owner: String) : MutableList<Project>

    @Query("SELECT alias FROM project WHERE id = :uuid")
    abstract fun getProjectAliasByProjectId(uuid: UUID) : String

    @Update
    abstract fun updateProject(project: Project)

    @Delete
    abstract fun deleteProject(myProject : Project)
}