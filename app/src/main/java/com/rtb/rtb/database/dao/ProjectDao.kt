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
    @Query("SELECT * FROM project")
    abstract fun getProjects() : MutableList<Project>

    @Query("SELECT * FROM project WHERE isActive = :isActive")
    abstract fun getProjectsByIsActive(isActive : Boolean) : MutableList<Project>

    @Query("SELECT * FROM project WHERE name LIKE '%' || :name || '%'")
    abstract fun getProjectsByName(name : String) : MutableList<Project>

    @Query("SELECT * FROM project WHERE id = :uuid")
    abstract fun getProjectByUUID(uuid: UUID) : Project

    @Query("SELECT alias FROM project WHERE id = :uuid")
    abstract fun getProjectAliasByProjectId(uuid: UUID) : String

    @Delete
    abstract fun deleteProject(myProject : Project)
    @Insert
    abstract fun createProject(myProject : Project)

    @Update
    abstract fun updateProject(project: Project)

}