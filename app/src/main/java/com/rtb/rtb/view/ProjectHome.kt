package com.rtb.rtb.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.rtb.rtb.R
import com.rtb.rtb.adapters.ResumeCardAdapter
import com.rtb.rtb.databinding.ActivityProjectHomeBinding
import com.rtb.rtb.model.ProjectModel
import com.rtb.rtb.view.components.AppBarFragment
import java.util.Date
import java.util.UUID

class ProjectHome : AppCompatActivity() {
    private val binding by lazy {
        ActivityProjectHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val appBar = supportFragmentManager.findFragmentById(R.id.appBar) as AppBarFragment
        appBar.setupAppBar(this)
    }

    override fun onResume() {
        super.onResume()

        var projectsMock = setProjectModelMutableList()

        var searchProjectsMock: MutableList<ProjectModel>
        var (activeProjectsMock, inactivateProjectsMock) = fillFilters(projectsMock)

        val readAllProjects = binding.phButtonAll
        val readActiveProjects = binding.phButtonActives
        val readInactiveProjects = binding.phButtonInactivates

        val projectListView = binding.phListViewOfProjects
        val projectsCardAdapter = ResumeCardAdapter(this, projectsMock)
        projectListView.adapter = projectsCardAdapter

        val searchProjectByName = binding.phImageViewSearchGlass
        searchProjectByName.setOnClickListener {
            val searchedProjects = binding.phEditTextInputSearchProject.text.toString()

            searchProjectsMock = mutableListOf()
            getProjectsByName(projectsMock, searchedProjects, searchProjectsMock)

            val searchProjectsCardAdapter = ResumeCardAdapter(this, searchProjectsMock)
            projectListView.adapter = searchProjectsCardAdapter
        }

        readAllProjects.setOnClickListener {
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green_50))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red_50))

            projectsMock = mutableListOf()
            getAllProjects(activeProjectsMock, projectsMock, inactivateProjectsMock)

            val projectsCardAdapter = ResumeCardAdapter(this, projectsMock)
            projectListView.adapter = projectsCardAdapter
        }

        readActiveProjects.setOnClickListener {
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_50))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red_50))

            activeProjectsMock = mutableListOf()
            getActiveProjects(projectsMock, activeProjectsMock)

            val activeProjectsCardAdapter = ResumeCardAdapter(this, activeProjectsMock)
            projectListView.adapter = activeProjectsCardAdapter
        }

        readInactiveProjects.setOnClickListener {
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_50))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green_50))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red))

            inactivateProjectsMock = mutableListOf()
            getInactiveProjects(projectsMock, inactivateProjectsMock)

            val inactiveProjectsCardAdapter = ResumeCardAdapter(this, inactivateProjectsMock)
            projectListView.adapter = inactiveProjectsCardAdapter
        }

        val createProject = binding.phButtonNewProject
        createProject.setOnClickListener{
            val createProjectIntent = Intent(this, CreateProject::class.java)
            startActivity(createProjectIntent)
        }
    }

    private fun getInactiveProjects(
        projectsMock: MutableList<ProjectModel>,
        inactivateProjectsMock: MutableList<ProjectModel>
    ) {
        for (project in projectsMock) {
            if (!project.isActive) {
                inactivateProjectsMock.add(project)
            }
        }
    }

    private fun getActiveProjects(
        projectsMock: MutableList<ProjectModel>,
        activeProjectsMock: MutableList<ProjectModel>
    ) {
        for (project in projectsMock) {
            if (project.isActive) {
                activeProjectsMock.add(project)
            }
        }
    }

    private fun getAllProjects(
        activeProjectsMock: MutableList<ProjectModel>,
        projectsMock: MutableList<ProjectModel>,
        inactivateProjectsMock: MutableList<ProjectModel>
    ) {
        for (project in activeProjectsMock) {
            projectsMock.add(project)
        }
        for (project in inactivateProjectsMock) {
            projectsMock.add(project)
        }
    }

    private fun getProjectsByName(
        projectsMock: MutableList<ProjectModel>,
        searchedProjects: String,
        searchProjectsMock: MutableList<ProjectModel>
    ) {
        for (project in projectsMock) {
            if (project.name.lowercase() == searchedProjects.lowercase()) {
                searchProjectsMock.add(project)
            }
        }
    }

    private fun fillFilters(projectsMock: MutableList<ProjectModel>): Pair<MutableList<ProjectModel>, MutableList<ProjectModel>> {
        val activeProjectsMock = mutableListOf<ProjectModel>()
        val inactivateProjectsMock = mutableListOf<ProjectModel>()
        for (project in projectsMock) {
            if (project.isActive) {
                activeProjectsMock.add(project)
            } else {
                inactivateProjectsMock.add(project)
            }
        }
        return Pair(activeProjectsMock, inactivateProjectsMock)
    }

    private fun setProjectModelMutableList(): MutableList<ProjectModel> {
        val fakeDate = Date()
        val projectsMock = mutableListOf(
            ProjectModel(
                UUID.randomUUID(),
                "MyBackendProject",
                "MBP",
                "This is my backend project",
                false,
                fakeDate,
                null,
                null
            ),
            ProjectModel(
                UUID.randomUUID(),
                "RMS Project",
                "RMS",
                "Document your project requirements efficiently and effectively. Using rich tools that enable quick and easy editing",
                true,
                fakeDate,
                null,
                null
            ),
            ProjectModel(
                UUID.randomUUID(),
                "PMS Project",
                "RTB",
                "Manage your sprint board, create your tickets and view your team's productivity graph",
                true,
                fakeDate,
                null,
                null
            ),
            ProjectModel(
                UUID.randomUUID(),
                "TCMS Project",
                "TCMS",
                "Test your system in an integrated way, taking advantage of intuitive, detailed and complete fields",
                true,
                fakeDate,
                null,
                null
            )
        )
        return projectsMock
    }
}