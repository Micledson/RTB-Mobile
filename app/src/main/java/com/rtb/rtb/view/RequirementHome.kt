package com.rtb.rtb.view

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.rtb.rtb.R
import com.rtb.rtb.adapters.RequirementResumeCardAdapter
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ActivityRequirementHomeBinding
import com.rtb.rtb.model.Project
import com.rtb.rtb.model.Requirement
import com.rtb.rtb.model.fromResponse
import com.rtb.rtb.networks.BaseRepository
import com.rtb.rtb.networks.ProjectRepository
import com.rtb.rtb.networks.RequirementRepository
import com.rtb.rtb.view.components.AppBarFragment
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment
import java.util.UUID

class RequirementHome : BaseActivity() {
    private val binding by lazy {
        ActivityRequirementHomeBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        DatabaseHelper.getInstance(this).requirementDao()
    }

    private val projectDao by lazy {
        DatabaseHelper.getInstance(this).projectDao()
    }

    private val projectId by lazy {
        UUID.fromString(intent.getStringExtra("projectId"))

    }

    private val requirements = ArrayList<Requirement>()
    private lateinit var project: Project

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupAppBar()
    }

    override fun onResume() {
        super.onResume()
        setupRepository()
    }

    private fun setupAppBar() {
        val appBar = supportFragmentManager.findFragmentById(R.id.app_bar) as AppBarFragment
        appBar.setupAppBar(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appBar.setModule(getString(R.string.rms))
        }
    }

    private fun setupBody() {
        binding.rhTextViewTitle.text = project.name
    }

    private fun setupListView() {
        val requirementListView = binding.rhListViewOfRequirements
        val requirementsCardAdapter = RequirementResumeCardAdapter(this, requirements, project)
        requirementListView.adapter = requirementsCardAdapter
    }

    private fun setupRepository() {
        binding.requirementHomeProgressBar.visibility = View.VISIBLE
        binding.requirementHomeConstraintLayout.visibility = View.GONE
        binding.requirementsNotFound.visibility = View.GONE
        requirements.clear()
        try {
            RequirementRepository().getRequirements(projectId) { requirementResult ->
                when(requirementResult) {
                    is BaseRepository.Result.Success -> {
                        if (requirementResult.data != null) {
                            requirementResult.data.map {
                                requirements.add(fromResponse(it))
                            }

                            ProjectRepository().getProjectByID(projectId) { projectResult ->
                                when(projectResult) {
                                    is BaseRepository.Result.Success -> {
                                        if (projectResult.data != null) {
                                            project = fromResponse(projectResult.data)

                                            setupBody()
                                            setupListView()
                                            setupSearchInput()
                                            setupCreateButton()
                                        }
                                    }

                                    is BaseRepository.Result.Error -> {
                                        showMessage(getString(R.string.error_getting_requirements_toast))
                                    }
                                }

                                binding.requirementHomeProgressBar.visibility = View.GONE
                                if (requirements.size > 0) {
                                    binding.requirementsNotFound.visibility = View.GONE
                                    binding.requirementHomeConstraintLayout.visibility = View.VISIBLE
                                } else {
                                    binding.requirementHomeConstraintLayout.visibility = View.VISIBLE
                                    binding.requirementsNotFound.visibility = View.VISIBLE
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

    private fun setupSearchInput() {
        val searchRequirementByTitleInput =
            supportFragmentManager.findFragmentById(R.id.rh_text_input_search_requirement) as InputFragment
        searchRequirementByTitleInput.setHint(getString(R.string.search_requirement))

        searchRequirementByTitleInput.addTextChangedListener { text ->
            val foundRequirements = searchRequirements(requirements, text)

            val foundRequirementsCardAdapter =
                RequirementResumeCardAdapter(this, foundRequirements, project)
            binding.rhListViewOfRequirements.adapter = foundRequirementsCardAdapter
        }
    }

    private fun setupCreateButton() {
        val createRequirement =
            supportFragmentManager.findFragmentById(R.id.rh_button_new_requirement) as ButtonFragment
        val createButton = createRequirement.setupButton(
            getString(R.string.new_requirement),
            Color.argb(255, 93, 63, 211)
        )
        createButton.setOnClickListener {
            val createRequirementIntent = Intent(this, CreateRequirement::class.java)
            val bundle = Bundle()
            bundle.putString("projectId", projectId.toString())
            createRequirementIntent.putExtras(bundle)
            startActivity(createRequirementIntent)
        }
    }

    private fun searchRequirements(
        requirements: MutableList<Requirement>,
        title: String
    ): MutableList<Requirement> {
        val foundRequirements = mutableListOf<Requirement>()
        for (requirement in requirements) {
            val code = "${project.alias}-${requirement.code}"
            if (code.contains(title, ignoreCase = true)) {
                foundRequirements.add(requirement)
            } else if (requirement.title.contains(title, ignoreCase = true)) {
                foundRequirements.add(requirement)
            }
        }
        return foundRequirements
    }
}
