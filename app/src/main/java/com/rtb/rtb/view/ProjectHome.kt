package com.rtb.rtb.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.rtb.rtb.R
import com.rtb.rtb.adapters.ProjectResumeCardAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val appBar = supportFragmentManager.findFragmentById(R.id.appBar) as AppBarFragment
        appBar.setupAppBar(this, true)
    }

    override fun onResume() {
        super.onResume()

        binding.projectHomeProgressBar.visibility = View.VISIBLE
        binding.phListViewOfProjects.visibility = View.GONE

        val searchedProjects = supportFragmentManager.findFragmentById(R.id.ph_text_input_search_project) as InputFragment
        searchedProjects.setHint(getString(R.string.search_project))
        searchedProjects.configSearchInputType()

        val readAllProjects = binding.phButtonAll
        readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
        val readActiveProjects = binding.phButtonActives
        val readInactiveProjects = binding.phButtonInactivates

        val projectListView = binding.phListViewOfProjects

        val projectList = mutableListOf<Project>()

        runBlocking {
            withContext(Dispatchers.IO){
                try {
                    val projectRepository = ProjectRepository()
                    projectRepository.getProjects {
                        it?.map { project ->
                            projectList.add(fromResponse(project))
                        }

                        val projectsCardAdapter =  ProjectResumeCardAdapter(this@ProjectHome, projectList)
                        projectListView.adapter = projectsCardAdapter

                        callSearchedProjects(searchedProjects, readAllProjects, readActiveProjects, readInactiveProjects, projectListView)

                        callAllProjects(readAllProjects, readActiveProjects, readInactiveProjects, projectListView)

                        callActiveProjects(readActiveProjects, readAllProjects, readInactiveProjects, projectListView)

                        callInactiveProjects(readInactiveProjects, readAllProjects, readActiveProjects, projectListView)

                        binding.projectHomeProgressBar.visibility = View.GONE
                        binding.phListViewOfProjects.visibility = View.VISIBLE
                    }
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

        val createProject = supportFragmentManager.findFragmentById(R.id.ph_button_new_project) as ButtonFragment
        val createButton = createProject.setupButton(getString(R.string.new_project))
        createButton.setOnClickListener {
            val createProjectIntent = Intent(this, CreateProject::class.java)
            startActivity(createProjectIntent)
        }
    }

    private fun callInactiveProjects(
        readInactiveProjects: Button,
        readAllProjects: Button,
        readActiveProjects: Button,
        projectListView: ListView,
    ) {
        readInactiveProjects.setOnClickListener {
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_50))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green_50))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red))

            val inactiveProjectList = mutableListOf<Project>()

            binding.projectHomeProgressBar.visibility = View.VISIBLE
            binding.phListViewOfProjects.visibility = View.GONE

            runBlocking {
                withContext(Dispatchers.IO){
                    try {
                        val projectRepository = ProjectRepository()
                        projectRepository.getProjects {
                            it?.map { project ->
                                if (project.isActive == false) {
                                    inactiveProjectList.add(fromResponse(project))
                                }
                            }

                            val inactiveProjectCardAdapter =  ProjectResumeCardAdapter(this@ProjectHome, inactiveProjectList)
                            projectListView.adapter = inactiveProjectCardAdapter

                            binding.projectHomeProgressBar.visibility = View.GONE
                            binding.phListViewOfProjects.visibility = View.VISIBLE
                        }
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
        }
    }

    private fun callActiveProjects(
        readActiveProjects: Button,
        readAllProjects: Button,
        readInactiveProjects: Button,
        projectListView: ListView,
    ) {
        readActiveProjects.setOnClickListener {
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_50))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red_50))

            val activeProjectList = mutableListOf<Project>()

            binding.projectHomeProgressBar.visibility = View.VISIBLE
            binding.phListViewOfProjects.visibility = View.GONE

            runBlocking {
                withContext(Dispatchers.IO){
                    try {
                        val projectRepository = ProjectRepository()
                        projectRepository.getProjects {
                            it?.map { project ->
                                if (project.isActive == true) {
                                    activeProjectList.add(fromResponse(project))
                                }
                            }

                            val activeProjectCardAdapter =  ProjectResumeCardAdapter(this@ProjectHome, activeProjectList)
                            projectListView.adapter = activeProjectCardAdapter

                            binding.projectHomeProgressBar.visibility = View.GONE
                            binding.phListViewOfProjects.visibility = View.VISIBLE
                        }
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
        }
    }

    private fun callAllProjects(
        readAllProjects: Button,
        readActiveProjects: Button,
        readInactiveProjects: Button,
        projectListView: ListView,
    ) {
        readAllProjects.setOnClickListener {
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green_50))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red_50))

            val projectList = mutableListOf<Project>()

            binding.projectHomeProgressBar.visibility = View.VISIBLE
            binding.phListViewOfProjects.visibility = View.GONE

            runBlocking {
                withContext(Dispatchers.IO){
                    try {
                        val projectRepository = ProjectRepository()
                        projectRepository.getProjects {
                            it?.map { project ->
                                projectList.add(fromResponse(project))
                            }

                            val projectCardAdapter =  ProjectResumeCardAdapter(this@ProjectHome, projectList)
                            projectListView.adapter = projectCardAdapter

                            binding.projectHomeProgressBar.visibility = View.GONE
                            binding.phListViewOfProjects.visibility = View.VISIBLE
                        }
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
        }
    }

    private fun callSearchedProjects(
        searchedProjects: InputFragment,
        readAllProjects: Button,
        readActiveProjects: Button,
        readInactiveProjects: Button,
        projectListView: ListView,
        ) {
        searchedProjects.addTextChangedListener { newText ->
            readAllProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_50))
            readActiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.green_50))
            readInactiveProjects.setBackgroundColor(ContextCompat.getColor(this, R.color.red_50))

            val searchedProjectList = mutableListOf<Project>()

            binding.projectHomeProgressBar.visibility = View.VISIBLE
            binding.phListViewOfProjects.visibility = View.GONE

            runBlocking {
                withContext(Dispatchers.IO){
                    try {
                        val projectRepository = ProjectRepository()
                        projectRepository.getProjects { projectsResponse ->
                            projectsResponse?.let {
                                val filteredProjects = it.filter { project ->
                                    project.name!!.contains(newText, ignoreCase = true)
                                }

                                searchedProjectList.addAll(filteredProjects.map { filteredProject ->
                                    fromResponse(filteredProject)
                                })
                            }

                            val searchedProjectsCardAdapter =  ProjectResumeCardAdapter(this@ProjectHome, searchedProjectList)
                            projectListView.adapter = searchedProjectsCardAdapter

                            binding.projectHomeProgressBar.visibility = View.GONE
                            binding.phListViewOfProjects.visibility = View.VISIBLE
                        }
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
        }
    }
}