package com.rtb.rtb.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.core.content.ContextCompat
import com.rtb.rtb.R
import com.rtb.rtb.adapters.ProjectResumeCardAdapter
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.database.preferences.SharedPrefs
import com.rtb.rtb.databinding.ActivityProjectHomeBinding
import com.rtb.rtb.model.Project
import com.rtb.rtb.view.components.AppBarFragment
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment

class ProjectHome : BaseActivity() {
    private val binding by lazy {
        ActivityProjectHomeBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        DatabaseHelper.getInstance(this).projectDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val appBar = supportFragmentManager.findFragmentById(R.id.appBar) as AppBarFragment
        appBar.setupAppBar(this, true)
    }

    override fun onResume() {
        super.onResume()

        val loggedUserEmail = SharedPrefs(this).getUserEmail()
        var projects = loggedUserEmail?.let { dao.getProjects(it) }

        val searchedProjects = supportFragmentManager.findFragmentById(R.id.ph_text_input_search_project) as InputFragment
        searchedProjects.setHint(getString(R.string.search_project))
        searchedProjects.configSearchInputType()

        val readAllProjects = binding.phButtonAll
        readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
        val readActiveProjects = binding.phButtonActives
        val readInactiveProjects = binding.phButtonInactivates

        val projectListView = binding.phListViewOfProjects
        var projectsCardAdapter = projects?.let { ProjectResumeCardAdapter(this, it) }
        projectListView.adapter = projectsCardAdapter

        callSearchedProjects(searchedProjects, readAllProjects, readActiveProjects,
            readInactiveProjects, loggedUserEmail, projectListView)

        callAllProjects(readAllProjects, readActiveProjects, readInactiveProjects,
            loggedUserEmail, projectListView)

        callActiveProjects(readActiveProjects, readAllProjects, readInactiveProjects,
            loggedUserEmail, projectListView)

        callInactiveProjects(readInactiveProjects, readAllProjects, readActiveProjects,
            loggedUserEmail, projectListView)

        val createProject = supportFragmentManager.findFragmentById(R.id.ph_button_new_project) as ButtonFragment
        val createButton = createProject.setupButton(getString(R.string.new_project))
        createButton.setOnClickListener {
            val createProjectIntent = Intent(this, CreateProject::class.java)
            startActivity(createProjectIntent)
        }
    }

    private fun callInactiveProjects(readInactiveProjects: Button, readAllProjects: Button, readActiveProjects: Button,
        loggedUserEmail: String?, projectListView: ListView) {
        readInactiveProjects.setOnClickListener {
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_50))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green_50))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red))

            val inactivateProjects =
                loggedUserEmail?.let { owner -> dao.getProjectsByIsActive(false, owner) }

            val inactiveProjectsCardAdapter =
                inactivateProjects?.let { inactiveProjectsCardAdapter ->
                    ProjectResumeCardAdapter(
                        this,
                        inactiveProjectsCardAdapter
                    )
                }
            projectListView.adapter = inactiveProjectsCardAdapter
        }
    }

    private fun callActiveProjects(readActiveProjects: Button, readAllProjects: Button,
       readInactiveProjects: Button, loggedUserEmail: String?, projectListView: ListView) {
        readActiveProjects.setOnClickListener {
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_50))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red_50))

            val activeProjects = loggedUserEmail?.let { owner ->
                dao.getProjectsByIsActive(true, owner)
            }

            val activeProjectsCardAdapter = activeProjects?.let { activeProjectsCardAdapter ->
                ProjectResumeCardAdapter(this, activeProjectsCardAdapter)
            }
            projectListView.adapter = activeProjectsCardAdapter
        }
    }

    private fun callAllProjects(readAllProjects: Button, readActiveProjects: Button, readInactiveProjects: Button,
        loggedUserEmail: String?, projectListView: ListView) {
        var projects: MutableList<Project>?
        var projectsCardAdapter: ProjectResumeCardAdapter?
        readAllProjects.setOnClickListener {
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green_50))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red_50))

            projects = loggedUserEmail?.let { owner -> dao.getProjects(owner) }

            projectsCardAdapter = ProjectResumeCardAdapter(this, projects!!)
            projectListView.adapter = projectsCardAdapter
        }
    }

    private fun callSearchedProjects(searchedProjects: InputFragment, readAllProjects: Button, readActiveProjects: Button,
         readInactiveProjects: Button, loggedUserEmail: String?, projectListView: ListView) {
        searchedProjects.addTextChangedListener { newText ->
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_50))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green_50))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red_50))
            val searchProjects =
                loggedUserEmail?.let { dao.getProjectsByName(newText.lowercase(), it) }

            val searchProjectsCardAdapter =
                searchProjects?.let { ProjectResumeCardAdapter(this, it) }
            projectListView.adapter = searchProjectsCardAdapter
        }
    }
}