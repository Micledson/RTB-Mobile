package com.rtb.rtb.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import com.rtb.rtb.R
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ActivityCreateRequirementBinding
import com.rtb.rtb.model.Origin
import com.rtb.rtb.model.Priority
import com.rtb.rtb.model.Requirement
import com.rtb.rtb.model.Type
import com.rtb.rtb.model.fromResponse
import com.rtb.rtb.model.toRequest
import com.rtb.rtb.networks.ApiService
import com.rtb.rtb.networks.BaseRepository
import com.rtb.rtb.networks.RequirementRepository
import com.rtb.rtb.networks.ResourceRepository
import com.rtb.rtb.view.components.AppBarFragment
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment
import java.util.Date
import java.util.UUID

class CreateRequirement : BaseActivity() {
    private val binding by lazy {
        ActivityCreateRequirementBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        DatabaseHelper.getInstance(this).requirementDao()
    }

    private var types: List<Type>? = null
    private var priorities: List<Priority>? = null
    private var origins: List<Origin>? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        setupActivity()
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

    private fun configCreateRequirementButton(
        buttonFragment: ButtonFragment,
        type: Spinner,
        origin: Spinner,
        priority: Spinner,
        title: InputFragment,
        userStory: InputFragment,
        notes: InputFragment,
        projectId: UUID
    ) {
        val button =
            buttonFragment.setupButton(getString(R.string.create), getColor(R.color.rms_purple))
        button.setOnClickListener {
            val typeText = type.selectedItem.toString()
            val typePosition = type.selectedItemPosition

            val originText = origin.selectedItem.toString()
            val originPosition = origin.selectedItemPosition

            val priorityText = priority.selectedItem.toString()
            val priorityPosition = priority.selectedItemPosition

            val titleText = title.getText()
            val userStoryText = userStory.getText()
            val notesText = notes.getText()

            val isRequirementValid = validateRequirement(
                typeText, originText, priorityText,
                titleText, userStoryText, projectId
            )
            if (isRequirementValid) {
                val requirement = Requirement(
                    UUID.randomUUID(),
                    0,
                    types!![typePosition].id,
                    origins!![originPosition].id,
                    priorities!![priorityPosition].id,
                    titleText,
                    userStoryText,
                    notesText,
                    projectId,
                    Date(),
                    Date(),
                )

                try {
                    val apiService = ApiService(this)
                    val requirementRepository = RequirementRepository(apiService)
                    requirementRepository.createRequirement(requirement.toRequest()) { result ->
                        when(result) {
                            is BaseRepository.Result.Success -> {
                                showMessage(getString(R.string.requirement_registered_successfully))
                                finish()
                            }

                            is BaseRepository.Result.Error -> {
                                showMessage(getString(R.string.requirement_not_registered))
                            }
                        }
                    }
                } catch (e: Exception) {
                    showMessage(getString(R.string.requirement_not_registered))
                }
            }
        }
    }

    private fun validateRequirement(
        type: String?,
        origin: String?,
        priority: String?,
        title: String?,
        userStory: String?,
        projectId: UUID?
    ): Boolean {
        if (projectId == null) {
            showMessage(getString(R.string.requirement_project_id_validation))
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
        } else if (userStory.isNullOrEmpty()) {
            showMessage(getString(R.string.requirement_user_story_field_validation))
            return false
        }

        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupActivity() {
        setupResources()

        val projectId = UUID.fromString(intent.getStringExtra("projectId"))
        Log.d("salve", "ID BOLADO ${intent.getStringExtra("projectId")}")

        val appBar = supportFragmentManager.findFragmentById(R.id.app_bar) as AppBarFragment
        appBar.setupAppBar(this)
        appBar.setModule(getString(R.string.rms))

        val type = findViewById<Spinner>(R.id.create_requirement_type)
        var options = types?.map { it.name }
        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options ?: emptyList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        type.adapter = adapter

        val origin = findViewById<Spinner>(R.id.create_requirement_origin)
        options = origins?.map { it.name }
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options ?: emptyList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        origin.adapter = adapter

        val priority = findViewById<Spinner>(R.id.create_requirement_priority)
        options = priorities?.map { it.level }
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options ?: emptyList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        priority.adapter = adapter

        val title =
            supportFragmentManager.findFragmentById(R.id.create_requirement_title) as InputFragment

        val userStory =
            supportFragmentManager.findFragmentById(R.id.create_requirement_user_story) as InputFragment
        userStory.setLines(5)

        val notes =
            supportFragmentManager.findFragmentById(R.id.create_requirement_notes) as InputFragment
        notes.setLines(5)

        val buttonFragment =
            supportFragmentManager.findFragmentById(R.id.create_requirement_button) as ButtonFragment

        configInputFields(title, userStory, notes)
        configCreateRequirementButton(
            buttonFragment,
            type,
            origin,
            priority,
            title,
            userStory,
            notes,
            projectId
        )
    }

    private fun setupResources() {
        var resources = ResourcesManager.getResources(this)
        types = resources?.types
        origins = resources?.origins
        priorities = resources?.priorities
    }
}
