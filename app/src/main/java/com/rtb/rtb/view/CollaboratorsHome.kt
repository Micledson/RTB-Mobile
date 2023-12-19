package com.rtb.rtb.view

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.rtb.rtb.R
import com.rtb.rtb.adapters.CollaboratorCardAdapter
import com.rtb.rtb.adapters.PossibleCollaboratorCardAdapter
import com.rtb.rtb.databinding.ActivityCollaboratorsHomeBinding
import com.rtb.rtb.model.Collaborator
import com.rtb.rtb.model.Project
import com.rtb.rtb.model.fromResponse
import com.rtb.rtb.networks.ApiService
import com.rtb.rtb.networks.BaseRepository
import com.rtb.rtb.networks.CollaboratorRepository
import com.rtb.rtb.networks.ProjectRepository
import com.rtb.rtb.view.components.AppBarFragment
import com.rtb.rtb.view.components.InputFragment
import java.util.UUID

class CollaboratorsHome : BaseActivity() {
    private val binding by lazy {
        ActivityCollaboratorsHomeBinding.inflate(layoutInflater)
    }

    private val projectId by lazy {
        UUID.fromString(intent.getStringExtra("uuid"))
    }

    private val possibleCollaborators = ArrayList<Collaborator>()
    private val alreadyCollaborators = ArrayList<Collaborator>()
    private lateinit var project: Project

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val appBar = supportFragmentManager.findFragmentById(R.id.appBar) as AppBarFragment
        appBar.setupAppBar(this)
    }

    override fun onResume() {
        super.onResume()
        setupRepository()
    }

    private fun setupBody() {
        binding.chTextViewProjectTitle.text = project.name
    }

    private fun setupListView() {
        val possibleCollaboratorListView = binding.chListViewOfNewCollaborators
        val possibleCollaboratorAdapter = PossibleCollaboratorCardAdapter(this, projectId, possibleCollaborators)
        possibleCollaboratorListView.adapter = possibleCollaboratorAdapter

        val collaboratorListView = binding.chListViewOfNewCollaborators
        val collaboratorAdapter = CollaboratorCardAdapter(this, projectId, alreadyCollaborators)
        collaboratorListView.adapter = collaboratorAdapter
    }

    private fun setupRepository() {
        binding.chProgressBarNewCollaborators.visibility = View.VISIBLE
        binding.chConstraintLayoutNewCollaborator.visibility = View.GONE
        binding.collaboratorsNotFound.visibility = View.GONE
        possibleCollaborators.clear()

        binding.chProgressBarAlreadyInProject.visibility = View.VISIBLE
        binding.chConstraintLayoutAlreadyInProject.visibility = View.GONE
        alreadyCollaborators.clear()

        try {
            val apiService = ApiService(this)
            CollaboratorRepository(apiService).getPossibleCollaborators(projectId) { possibleCollaboratorsResult ->
                when(possibleCollaboratorsResult) {
                    is BaseRepository.Result.Success -> {
                        if (possibleCollaboratorsResult.data != null) {
                            possibleCollaboratorsResult.data.map {
                                possibleCollaborators.add(fromResponse(it))
                            }

                            ProjectRepository(apiService).getProjectByID(projectId) { projectResult ->
                                when (projectResult) {
                                    is BaseRepository.Result.Success -> {
                                        if (projectResult.data != null) {
                                            project = fromResponse(projectResult.data)

                                            setupBody()
                                            setupListView()
                                            setupPossibleCollaboratorsSearchInput()
                                        }
                                    }

                                    is BaseRepository.Result.Error -> {
                                        showMessage(getString(R.string.error_getting_new_collaborators_toast))
                                    }
                                }

                                binding.chProgressBarNewCollaborators.visibility = View.GONE
                                if (possibleCollaborators.size > 0) {
                                    binding.collaboratorsNotFound.visibility = View.GONE
                                    binding.chConstraintLayoutNewCollaborator.visibility =
                                        View.VISIBLE
                                } else {
                                    binding.collaboratorsNotFound.visibility = View.VISIBLE
                                }
                            }
                        }
                    }

                    is BaseRepository.Result.Error -> {
                        showMessage(getString(R.string.error_getting_requirements_toast))
                    }
                }
            }
        } catch (e: Exception) {
            showMessage("An unexpected error appeared")
            finish()
        }

        try {
            val apiService = ApiService(this)
            CollaboratorRepository(apiService).getCollaborators(projectId) { collaboratorsResult ->
                when(collaboratorsResult) {
                    is BaseRepository.Result.Success -> {
                        if (collaboratorsResult.data != null) {
                            collaboratorsResult.data.map {
                                alreadyCollaborators.add(fromResponse(it))
                            }

                            ProjectRepository(apiService).getProjectByID(projectId) { projectResult ->
                                when (projectResult) {
                                    is BaseRepository.Result.Success -> {
                                        if (projectResult.data != null) {
                                            project = fromResponse(projectResult.data)

                                            setupAlreadyCollaboratorsSearchInput()
                                        }
                                    }

                                    is BaseRepository.Result.Error -> {
                                        showMessage(getString(R.string.error_getting_new_collaborators_toast))
                                    }
                                }

                                binding.chProgressBarAlreadyInProject.visibility = View.GONE
                                if (possibleCollaborators.size > 0) {
                                    binding.chConstraintLayoutAlreadyInProject.visibility =
                                        View.VISIBLE
                                } else {
                                    binding.chConstraintLayoutAlreadyInProject.visibility =
                                        View.VISIBLE
                                }
                            }
                        }
                    }

                    is BaseRepository.Result.Error -> {
                        showMessage(getString(R.string.error_getting_requirements_toast))
                    }
                }
            }
        } catch (e: Exception) {
            showMessage("An unexpected error appeared")
            finish()
        }
    }

    private fun setupPossibleCollaboratorsSearchInput() {
        val searchCollaboratorByEmail =
            supportFragmentManager.findFragmentById(R.id.ch_text_input_search_collaborator) as InputFragment
        searchCollaboratorByEmail.setHint(getString(R.string.search_collaborator))

        searchCollaboratorByEmail.addTextChangedListener { text ->
            val foundPossibleCollaborators = searchPossibleCollaborators(possibleCollaborators, text)

            val foundPossibleCollaboratorsCardAdapter =
                PossibleCollaboratorCardAdapter(this, projectId, foundPossibleCollaborators)
            binding.chListViewOfNewCollaborators.adapter = foundPossibleCollaboratorsCardAdapter
        }
    }

    private fun setupAlreadyCollaboratorsSearchInput() {
        val searchCollaboratorByEmail =
            supportFragmentManager.findFragmentById(R.id.ch_text_input_search_collaborator) as InputFragment

        searchCollaboratorByEmail.addTextChangedListener { text ->
            val foundAlreadyCollaborators = searchAlreadyCollaborators(alreadyCollaborators, text)

            val foundAlreadyCollaboratorsCardAdapter =
                CollaboratorCardAdapter(this, projectId, foundAlreadyCollaborators)
            binding.chListViewOfAlreadyInProject.adapter = foundAlreadyCollaboratorsCardAdapter
        }
    }

    private fun searchPossibleCollaborators(
        possibleCollaborators: MutableList<Collaborator>,
        title: String
    ): MutableList<Collaborator> {
        val foundPossibleCollaborators = mutableListOf<Collaborator>()
        for (possibleCollaborator in possibleCollaborators) {

            if (possibleCollaborator.userEmail.contains(title, ignoreCase = true)) {
                foundPossibleCollaborators.add(possibleCollaborator)
            } else if (possibleCollaborator.userFirstName.contains(title, ignoreCase = true)) {
                foundPossibleCollaborators.add(possibleCollaborator)
            } else if (possibleCollaborator.userLastName.contains(title, ignoreCase = true)) {
                foundPossibleCollaborators.add(possibleCollaborator)
            }
        }
        return foundPossibleCollaborators
    }

    private fun searchAlreadyCollaborators(
        collaborators: MutableList<Collaborator>,
        title: String
    ): MutableList<Collaborator> {
        val foundCollaborators = mutableListOf<Collaborator>()
        for (collaborator in collaborators) {

            if (collaborator.userEmail.contains(title, ignoreCase = true)) {
                foundCollaborators.add(collaborator)
            } else if (collaborator.userFirstName.contains(title, ignoreCase = true)) {
                foundCollaborators.add(collaborator)
            } else if (collaborator.userLastName.contains(title, ignoreCase = true)) {
                foundCollaborators.add(collaborator)
            }
        }
        return foundCollaborators
    }
}