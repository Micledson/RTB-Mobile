package com.rtb.rtb.view

import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.rtb.rtb.R
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ActivityUpdateRequirementBinding
import com.rtb.rtb.model.Origin
import com.rtb.rtb.model.Priority
import com.rtb.rtb.model.Requirement
import com.rtb.rtb.model.Type
import com.rtb.rtb.model.fromResponse
import com.rtb.rtb.model.toRequest
import com.rtb.rtb.networks.ProjectRepository
import com.rtb.rtb.networks.RequirementRepository
import com.rtb.rtb.networks.ResourceRepository
import com.rtb.rtb.view.components.AppBarFragment
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import java.util.UUID

class UpdateRequirement : BaseActivity() {
    private val binding by lazy {
        ActivityUpdateRequirementBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        DatabaseHelper.getInstance(this).requirementDao()
    }

    private val projectDao by lazy {
        DatabaseHelper.getInstance(this).projectDao()
    }

    private lateinit var types: List<Type>
    private lateinit var priorities: List<Priority>
    private lateinit var origins: List<Origin>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val requirementId = UUID.fromString(intent.getStringExtra("requirementId"))

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                getResource()
            }

            val requirementRepository = RequirementRepository()
            try {
                requirementRepository.getRequirementById(requirementId) { requirementResponse ->
                    if (requirementResponse != null) {
                        val requirement = fromResponse(requirementResponse)
                        setupRequirementCode(requirement)
                        setupRequirementUI(requirement)

                    } else {
                        Toast.makeText(
                            this@UpdateRequirement,
                            getString(R.string.error_getting_requirement_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@UpdateRequirement,
                    "An unexpected error appeared",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun setupRequirementCode(requirement: Requirement) {
        val projectRepository = ProjectRepository()
        projectRepository.getProjectByID(requirement.projectId) { projectResponse ->
            if (projectResponse != null) {
                val project = fromResponse(projectResponse)
                val requirementCodeTextView =
                    findViewById<TextView>(R.id.update_requirement_requirement_code)
                requirementCodeTextView.text = getString(
                    R.string.requirement_code_value,
                    project.alias, requirement.code
                )
            } else {
                Toast.makeText(
                    this@UpdateRequirement,
                    getString(R.string.error_getting_requirement_toast),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRequirementUI(requirement: Requirement) {
        val appBar = supportFragmentManager.findFragmentById(R.id.app_bar) as AppBarFragment
        appBar.setupAppBar(this)
        appBar.setModule(getString(R.string.rms))

        val type = findViewById<Spinner>(R.id.update_requirement_type)
        var options = types.map { it.name }

        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        type.adapter = adapter

        val origin = findViewById<Spinner>(R.id.update_requirement_origin)
        options = origins.map { it.name }
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        origin.adapter = adapter

        val priority = findViewById<Spinner>(R.id.update_requirement_priority)
        options = priorities.map { it.level }
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        priority.adapter = adapter

        val title =
            supportFragmentManager.findFragmentById(R.id.update_requirement_title) as InputFragment
        title.setText(requirement.title)

        val userStory =
            supportFragmentManager.findFragmentById(R.id.update_requirement_user_story) as InputFragment
        userStory.setLines(5)
        userStory.setText(requirement.userStory)

        val notes =
            supportFragmentManager.findFragmentById(R.id.update_requirement_notes) as InputFragment
        notes.setLines(5)
        notes.setText(requirement.description)

        val buttonFragment =
            supportFragmentManager.findFragmentById(R.id.update_requirement_button) as ButtonFragment

        configInputFields(title, userStory, notes)
        configUpdateRequirementButton(
            buttonFragment, requirement.id, requirement.code, type,
            origin, priority, title, userStory, notes, requirement.projectId, requirement.createdAt
        )
    }

    private fun configInputFields(
        title: InputFragment,
        userStory: InputFragment,
        notes: InputFragment
    ) {
        title.setHint(getString(R.string.requirement_title))
        userStory.setHint(getString(R.string.requirement_user_story))
        notes.setHint(getString(R.string.requirement_notes))
    }

    private fun configUpdateRequirementButton(
        buttonFragment: ButtonFragment,
        id: UUID,
        code: Int,
        type: Spinner,
        origin: Spinner,
        priority: Spinner,
        title: InputFragment,
        userStory: InputFragment,
        notes: InputFragment,
        projectId: UUID,
        createdAt: Date,
    ) {
        val button = buttonFragment.setupButton(getString(R.string.update), getColor(R.color.rms_purple))
        button.setOnClickListener {
            val typeText = type.selectedItem.toString()
            val originText = origin.selectedItem.toString()
            val priorityText = priority.selectedItem.toString()
            val titleText = title.getText()
            val userStoryText = userStory.getText()
            val notesText = notes.getText()

            val isRequirementValid = validateRequirement(id, typeText, originText, priorityText, titleText, userStoryText)
            if (isRequirementValid) {
                val requirement = Requirement(
                    id,
                    code,
                    UUID.randomUUID(),//typeText,
                    UUID.randomUUID(),//originText,
                    UUID.randomUUID(), //priorityText,
                    titleText,
                    userStoryText,
                    notesText,
                    projectId,
                    createdAt,
                    Calendar.getInstance().time,
                )

                try {
                    val requirementRepository = RequirementRepository()
                    requirementRepository.updateRequirement(this, requirement.id, requirement.toRequest())
                    showMessage(getString(R.string.requirement_updated_successfully))

                    finish()
                } catch (e: Exception) {
                    showMessage(getString(R.string.requirement_not_updated))
                }
            }
        }
    }

    private fun validateRequirement(
        id: UUID?,
        type: String?,
        origin: String?,
        priority: String?,
        title: String?,
        userStory: String?
    ): Boolean {
        if (id == null){
            showMessage(getString(R.string.requirement_id_validation))
            return false
        } else if (type.equals(getString(R.string.requirement_select_type))) {
            showMessage(getString(R.string.requirement_type_field_validation))
            return false
        } else if (origin.equals(getString(R.string.requirement_select_origin))) {
            showMessage(getString(R.string.requirement_origin_field_validation))
            return false
        } else if (priority.equals(getString(R.string.requirement_select_priority))) {
            showMessage(getString(R.string.requirement_priority_field_validation))
            return false
        } else if (title.isNullOrEmpty()) {
            showMessage(getString(R.string.requirement_title_field_validation))
            return false
        } else if(userStory.isNullOrEmpty()) {
            showMessage(getString(R.string.requirement_user_story_field_validation))
            return false
        }

        return true
    }

    private suspend fun getResource() {
        val resourceRepository = ResourceRepository()
        resourceRepository.getResources { resource ->
            if (resource != null) {
                types = fromResponse(resource).types
                origins = fromResponse(resource).origins
                priorities = fromResponse(resource).priorities
            }
        }
    }
}
