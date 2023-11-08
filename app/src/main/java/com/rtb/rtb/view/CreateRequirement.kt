package com.rtb.rtb.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import com.rtb.rtb.R
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ActivityCreateRequirementBinding
import com.rtb.rtb.model.Requirement
import com.rtb.rtb.view.components.AppBarFragment
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment
import java.util.Calendar
import java.util.UUID

class CreateRequirement : BaseActivity() {
    private val binding by lazy {
        ActivityCreateRequirementBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        DatabaseHelper.getInstance(this).requirementDao()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val projectId = UUID.fromString(intent.getStringExtra("projectId"))

        val appBar = supportFragmentManager.findFragmentById(R.id.app_bar) as AppBarFragment
        appBar.setupAppBar(this)
        appBar.setModule(getString(R.string.rms))

        val type = findViewById<Spinner>(R.id.create_requirement_type)
        var options = listOf(
            getString(R.string.requirement_select_type),
            getString(R.string.requirement_select_type_functional),
            getString(R.string.requirement_select_type_non_functional),
            getString(R.string.requirement_select_type_inverse),
            getString(R.string.requirement_select_type_business_rule)
        )
        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        type.adapter = adapter

        val origin = findViewById<Spinner>(R.id.create_requirement_origin)
        options = listOf(
            getString(R.string.requirement_select_origin),
            getString(R.string.requirement_select_origin_product),
            getString(R.string.requirement_select_origin_organization),
            getString(R.string.requirement_select_origin_external),
        )
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        origin.adapter = adapter

        val priority = findViewById<Spinner>(R.id.create_requirement_priority)
        options = listOf(
            getString(R.string.requirement_select_priority),
            getString(R.string.requirement_select_priority_urgent),
            getString(R.string.requirement_select_priority_high),
            getString(R.string.requirement_select_priority_normal),
            getString(R.string.requirement_select_priority_low),
        )
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        priority.adapter = adapter

        val title = supportFragmentManager.findFragmentById(R.id.create_requirement_title) as InputFragment

        val userStory = supportFragmentManager.findFragmentById(R.id.create_requirement_user_story) as InputFragment
        userStory.setLines(5)

        val notes = supportFragmentManager.findFragmentById(R.id.create_requirement_notes) as InputFragment
        notes.setLines(5)

        val buttonFragment = supportFragmentManager.findFragmentById(R.id.create_requirement_button) as ButtonFragment

        configInputFields(title, userStory, notes)
        configCreateRequirementButton(buttonFragment, type, origin, priority, title, userStory, notes, projectId)
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
        val button = buttonFragment.setupButton(getString(R.string.create), getColor(R.color.rms_purple))
        button.setOnClickListener {
            val typeText = type.selectedItem.toString()
            val originText = origin.selectedItem.toString()
            val priorityText = priority.selectedItem.toString()
            val titleText = title.getText()
            val userStoryText = userStory.getText()
            val notesText = notes.getText()

            val isRequirementValid = validateRequirement(typeText, originText, priorityText,
                titleText, userStoryText, projectId)
            if (isRequirementValid) {
                val requirement = Requirement(
                    UUID.randomUUID(),
                    dao.getLastRequirementCodeByProjectId(projectId) + 1,
                    typeText,
                    originText,
                    priorityText,
                    titleText,
                    userStoryText,
                    notesText,
                    projectId,
                    Calendar.getInstance().time,
                    null,
                    null
                )

                try {
                    dao.createRequirement(requirement)
                    showMessage(getString(R.string.requirement_registered_successfully))

                    val intent = Intent(this, ProjectHome::class.java)
                    startActivity(intent)

                    finish()
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
        if (projectId == null){
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
        } else if(userStory.isNullOrEmpty()) {
            showMessage(getString(R.string.requirement_user_story_field_validation))
            return false
        }

        return true
    }
}
