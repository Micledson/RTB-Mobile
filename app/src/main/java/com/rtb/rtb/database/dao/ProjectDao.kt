package com.rtb.rtb.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.rtb.rtb.model.Project

@Dao
abstract class ProjectDao {
    @Query("SELECT * FROM project")
    abstract fun getProjects() : MutableList<Project>

    @Query("SELECT * FROM project WHERE isActive = :isActive")
    abstract fun getProjectsByIsActive(isActive : Boolean) : MutableList<Project>

    @Query("SELECT * FROM project WHERE name LIKE '%' || :name || '%'")
    abstract fun getProjectsByName(name : String) : MutableList<Project>

    @Delete
    abstract fun deleteProject(myProject : Project)

    //@Insert
    //TODO: Implement me

}