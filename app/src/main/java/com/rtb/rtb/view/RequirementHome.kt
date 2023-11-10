package com.rtb.rtb.view

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.rtb.rtb.R
import com.rtb.rtb.adapters.RequirementResumeCardAdapter
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ActivityRequirementHomeBinding
import com.rtb.rtb.model.Requirement
import com.rtb.rtb.view.components.AppBarFragment
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment
import java.util.UUID

class RequirementHome : AppCompatActivity() {
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAppBar()
    }

    override fun onResume() {
        super.onResume()

        setupBody()
        setupListView()
        setupSearchInput()
        setupCreateButton()
    }

    private fun setupAppBar() {
        val appBar = supportFragmentManager.findFragmentById(R.id.app_bar) as AppBarFragment
        appBar.setupAppBar(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appBar.setModule(getString(R.string.rms))
        }
    }

    private fun setupBody() {
        val project = projectDao.getProjectByUUID(projectId)
        binding.rhTextViewTitle.text = project.name
    }

    private fun setupListView() {
        val requirements = dao.getRequirementsByProjectId(projectId)
        val project = projectDao.getProjectByUUID(projectId)
        val requirementListView = binding.rhListViewOfRequirements
        val requirementsCardAdapter = RequirementResumeCardAdapter(this, requirements, project)
        requirementListView.adapter = requirementsCardAdapter
    }

    private fun setupSearchInput() {
        val searchRequirementByTitleInput = supportFragmentManager.findFragmentById(R.id.rh_text_input_search_requirement) as InputFragment
        searchRequirementByTitleInput.setHint(getString(R.string.search_requirement))

        searchRequirementByTitleInput.addTextChangedListener { text ->
            val requirements = dao.getRequirements()
            val project = projectDao.getProjectByUUID(projectId)
            val foundRequirements = searchRequirements(requirements, text)

            val foundRequirementsCardAdapter = RequirementResumeCardAdapter(this, foundRequirements, project)
            binding.rhListViewOfRequirements.adapter = foundRequirementsCardAdapter
        }
    }

    private fun setupCreateButton() {
        val createRequirement = supportFragmentManager.findFragmentById(R.id.rh_button_new_requirement) as ButtonFragment
        val createButton = createRequirement.setupButton(getString(R.string.new_requirement), Color.argb(255, 93, 63, 211))
        createButton.setOnClickListener {
            val createRequirementIntent = Intent(this, CreateRequirement::class.java)
            val bundle = Bundle()
            bundle.putString("projectId", projectId.toString())
            createRequirementIntent.putExtras(bundle)
            startActivity(createRequirementIntent)
        }
    }

    private fun searchRequirements(requirements: MutableList<Requirement>, title: String): MutableList<Requirement> {
        val foundRequirements = mutableListOf<Requirement>()
        val project = projectDao.getProjectByUUID(projectId)
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
