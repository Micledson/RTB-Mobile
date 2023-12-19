package com.rtb.rtb.view

import ResourcesManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
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
import com.rtb.rtb.networks.ApiService
import com.rtb.rtb.networks.BaseRepository
import com.rtb.rtb.networks.ProjectRepository
import com.rtb.rtb.networks.RequirementRepository
import com.rtb.rtb.view.components.AppBarFragment
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
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

    private var types: List<Type>? = null
    private var priorities: List<Priority>? = null
    private var origins: List<Origin>? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupAppBar()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        setupActivity(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupActivity(context: Context) {
        val requirementId = UUID.fromString(intent.getStringExtra("requirementId"))

        setupResources()

        binding.updateRequirementProgressBar.visibility = View.VISIBLE
        binding.updateRequirementScrollView.visibility = View.GONE

        runBlocking {
            withContext(Dispatchers.IO) {
                val apiService = ApiService(context)
                val requirementRepository = RequirementRepository(apiService)
                try {
                    requirementRepository.getRequirementById(requirementId) { result ->
                        when(result) {
                            is BaseRepository.Result.Success -> {
                                if (result.data != null) {
                                    val requirement = fromResponse(result.data)
                                    setupRequirementActivity(requirement)
                                } else {
                                    showMessage(getString(R.string.error_getting_requirement_toast))
                                    finish()
                                }
                            }

                            is BaseRepository.Result.Error -> {
                                showMessage(getString(R.string.requirement_not_updated))
                                finish()
                            }
                        }
                    }
                } catch (e: Exception) {
                    showMessage("An unexpected error appeared")
                    finish()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRequirementActivity(requirement: Requirement) {
        val apiService = ApiService(this)
        val projectRepository = ProjectRepository(apiService)
        projectRepository.getProjectByID(requirement.projectId) { result ->
            when (result) {
                is BaseRepository.Result.Success -> {
                    if (result.data != null) {
                        val project = fromResponse(result.data)
                        val requirementCodeTextView =
                            findViewById<TextView>(R.id.update_requirement_requirement_code)
                        requirementCodeTextView.text = getString(
                            R.string.requirement_code_value,
                            project.alias, requirement.code
                        )

                        binding.updateRequirementProgressBar.visibility = View.GONE
                        binding.updateRequirementScrollView.visibility = View.VISIBLE
                        setupRequirementUI(requirement)
                    } else {
                        showMessage(getString(R.string.error_getting_requirement_toast))
                        finish()
                    }
                }

                is BaseRepository.Result.Error -> {
                    showMessage(getString(R.string.error_getting_requirement_toast))
                    finish()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRequirementUI(requirement: Requirement) {
        val type = findViewById<Spinner>(R.id.update_requirement_type)
        var options = types?.map { it.name }

        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options ?: emptyList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        type.adapter = adapter

        types?.forEachIndexed { index, it ->
            if (it.id == requirement.typeId) {
                type.setSelection(index)
                return@forEachIndexed
            }
        }

        val origin = findViewById<Spinner>(R.id.update_requirement_origin)
        options = origins?.map { it.name }
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options ?: emptyList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        origin.adapter = adapter

        origins?.forEachIndexed { index, it ->
            if (it.id == requirement.originId) {
                origin.setSelection(index)
                return@forEachIndexed
            }
        }

        val priority = findViewById<Spinner>(R.id.update_requirement_priority)
        options = priorities?.map { it.level }
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options ?: emptyList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        priority.adapter = adapter

        priorities?.forEachIndexed { index, it ->
            if (it.id == requirement.priorityId) {
                priority.setSelection(index)
                return@forEachIndexed
            }
        }

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupAppBar() {
        val appBar = supportFragmentManager.findFragmentById(R.id.app_bar) as AppBarFragment
        appBar.setupAppBar(this)
        appBar.setModule(getString(R.string.rms))
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
            val typePosition = type.selectedItemPosition
            val originText = origin.selectedItem.toString()
            val originPosition = origin.selectedItemPosition
            val priorityText = priority.selectedItem.toString()
            val priorityPosition = priority.selectedItemPosition
            val titleText = title.getText()
            val userStoryText = userStory.getText()
            val notesText = notes.getText()

            val isRequirementValid = validateRequirement(id, typeText, originText, priorityText, titleText, userStoryText)
            if (isRequirementValid) {
                val requirement = Requirement(
                    id,
                    code,
                    types!![typePosition].id,
                    origins!![originPosition].id,
                    priorities!![priorityPosition].id,
                    titleText,
                    userStoryText,
                    notesText,
                    projectId,
                    createdAt,
                    Calendar.getInstance().time,
                )

                try {
                    val apiService = ApiService(this)
                    val requirementRepository = RequirementRepository(apiService)
                    requirementRepository.updateRequirement(requirement.id, requirement.toRequest()) { result ->
                        when(result) {
                            is BaseRepository.Result.Success -> {
                                showMessage(getString(R.string.requirement_updated_successfully))
                                finish()
                            }

                            is BaseRepository.Result.Error -> {
                                showMessage(getString(R.string.requirement_not_updated))
                            }
                        }
                    }
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

    private fun setupResources() {
        var resources = ResourcesManager.getResources(this)
        types = resources?.types
        origins = resources?.origins
        priorities = resources?.priorities
    }
}
