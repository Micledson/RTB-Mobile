package com.rtb.rtb.view

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.rtb.rtb.R
import com.rtb.rtb.adapters.ResumeCardAdapter
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ActivityProjectHomeBinding
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
        appBar.setupAppBar(this)
    }

    override fun onResume() {
        super.onResume()

        var projects = dao.getProjects()

        val searchedProjects = supportFragmentManager.findFragmentById(R.id.ph_text_input_layout_search_project_field) as InputFragment
        searchedProjects.setHint(getString(R.string.search_project))

        val readAllProjects = binding.phButtonAll
        readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
        val readActiveProjects = binding.phButtonActives
        val readInactiveProjects = binding.phButtonInactivates

        val projectListView = binding.phListViewOfProjects
        val projectsCardAdapter = ResumeCardAdapter(this, projects)
        projectListView.adapter = projectsCardAdapter

        val searchProjectByName = binding.phImageViewSearchGlass
        searchProjectByName.setOnClickListener {
            val searchProjects = dao.getProjectsByName(searchedProjects.getText())

            val searchProjectsCardAdapter = ResumeCardAdapter(this, searchProjects)
            projectListView.adapter = searchProjectsCardAdapter
        }

        readAllProjects.setOnClickListener {
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green_50))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red_50))

            projects = dao.getProjects()

            val projectsCardAdapter = ResumeCardAdapter(this, projects)
            projectListView.adapter = projectsCardAdapter
        }

        readActiveProjects.setOnClickListener {
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_50))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red_50))

            val activeProjects = dao.getProjectsByIsActive(true)

            val activeProjectsCardAdapter = ResumeCardAdapter(this, activeProjects)
            projectListView.adapter = activeProjectsCardAdapter
        }

        readInactiveProjects.setOnClickListener {
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_50))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green_50))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red))

            val inactivateProjects = dao.getProjectsByIsActive(false)

            val inactiveProjectsCardAdapter = ResumeCardAdapter(this, inactivateProjects)
            projectListView.adapter = inactiveProjectsCardAdapter
        }

        val createProject = supportFragmentManager.findFragmentById(R.id.ph_button_new_project) as ButtonFragment
        val createButton = createProject.setupButton(getString(R.string.new_project))
        createButton.setOnClickListener {
            val createProjectIntent = Intent(this, CreateProject::class.java)
            startActivity(createProjectIntent)
        }
    }
}