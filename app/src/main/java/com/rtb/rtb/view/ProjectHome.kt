package com.rtb.rtb.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.rtb.rtb.R
import com.rtb.rtb.adapters.ProjectResumeCardAdapter
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.database.preferences.SharedPrefs
import com.rtb.rtb.databinding.ActivityProjectHomeBinding
import com.rtb.rtb.model.Project
import com.rtb.rtb.model.fromResponse
import com.rtb.rtb.networks.ProjectRepository
import com.rtb.rtb.view.components.AppBarFragment
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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
        val projectsCardAdapter = getProjects()
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

    private fun getSearchedProjects(search : String): ListAdapter {
        val projectList = mutableListOf<Project>()

        val projectRepository = ProjectRepository()
        projectRepository.getProjects {
            it?.map { project ->
                if (project.name.equals(search)) {
                    projectList.add(fromResponse(project))
                }
            }
        }

        return ProjectResumeCardAdapter(this, projectList)
    }

    private fun getIsActiveProjects(isActive: Boolean): ListAdapter {
        val projectList = mutableListOf<Project>()

        val projectRepository = ProjectRepository()
        projectRepository.getProjects {
            it?.map { project ->
                if (project.isActive == isActive) {
                    projectList.add(fromResponse(project))
                }
            }
        }

        return ProjectResumeCardAdapter(this, projectList)
    }

    private fun getProjects(): ListAdapter {
        val projectList = mutableListOf<Project>()

        runBlocking {
            withContext(Dispatchers.IO){
                try {
                    val projectRepository = ProjectRepository()
                    projectRepository.getProjects {
                        it?.map { project ->
                            projectList.add(fromResponse(project))
                            Log.i("pList", "getProjects: "+projectList)
                        }
                    }
                    Log.i("pList2", "getProjects: "+projectList)
                } catch (e: Exception) {
                    Toast.makeText(
                        this@ProjectHome,
                        "An unexpected error appeared",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }

        return ProjectResumeCardAdapter(this, projectList)
    }

    private fun callInactiveProjects(readInactiveProjects: Button, readAllProjects: Button, readActiveProjects: Button,
        loggedUserEmail: String?, projectListView: ListView) {
        readInactiveProjects.setOnClickListener {
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_50))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green_50))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red))

            val inactivateProjects = getIsActiveProjects(false)

            projectListView.adapter = inactivateProjects
        }
    }

    private fun callActiveProjects(readActiveProjects: Button, readAllProjects: Button,
       readInactiveProjects: Button, loggedUserEmail: String?, projectListView: ListView) {
        readActiveProjects.setOnClickListener {
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_50))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red_50))

            val activateProjects = getIsActiveProjects(true)

            projectListView.adapter = activateProjects
        }
    }

    private fun callAllProjects(readAllProjects: Button, readActiveProjects: Button, readInactiveProjects: Button,
        loggedUserEmail: String?, projectListView: ListView) {
        var projects: ListAdapter?
        var projectsCardAdapter: ProjectResumeCardAdapter?
        readAllProjects.setOnClickListener {
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green_50))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red_50))

            projects = getProjects()

            projectListView.adapter = projects
        }
    }

    private fun callSearchedProjects(searchedProjects: InputFragment, readAllProjects: Button, readActiveProjects: Button,
         readInactiveProjects: Button, loggedUserEmail: String?, projectListView: ListView) {
        searchedProjects.addTextChangedListener { newText ->
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_50))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green_50))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red_50))

            val fetchProjects = getSearchedProjects(newText)

            projectListView.adapter = fetchProjects
        }
    }
}