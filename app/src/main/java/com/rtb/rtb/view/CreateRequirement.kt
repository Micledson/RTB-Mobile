package com.rtb.rtb.view

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import com.rtb.rtb.R
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ActivityCreateRequirementBinding
import com.rtb.rtb.model.Project
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

        val project = getProjectModelAccordingVersion()

        val appBar = supportFragmentManager.findFragmentById(R.id.appBar) as AppBarFragment
        appBar.setupAppBar(this)
        appBar.setModule("RMS")

        val type = findViewById<Spinner>(R.id.create_requirement_type)
        val title = supportFragmentManager.findFragmentById(R.id.create_requirement_title) as InputFragment
        val description = supportFragmentManager.findFragmentById(R.id.create_requirement_description) as InputFragment
        val userStory = supportFragmentManager.findFragmentById(R.id.create_requirement_user_story) as InputFragment
        val priority = findViewById<Spinner>(R.id.create_requirement_priority)
        val buttonFragment = supportFragmentManager.findFragmentById(R.id.create_requirement_button) as ButtonFragment

        description.setLines(5)
        userStory.setLines(5)
        var options = listOf("Select the requirement type...", "Functional Requirement", "Non-Functional Requirement", "Inverse Requirement", "Business Rule")
        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        type.adapter = adapter
        options = listOf("Select the requirement priority...", "Urgent", "High", "Normal", "Low")
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        priority.adapter = adapter

        configInputFields(title, description, userStory)
        configCreateRequirementButton(buttonFragment, type, title, description, userStory, priority, project)
    }

    private fun getProjectModelAccordingVersion(): Project? {
        val project = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("projectModel", Project::class.java)
        } else {
            intent.getParcelableExtra("projectModel")
        }
        return project
    }

    private fun configInputFields(
        title: InputFragment,
        description: InputFragment,
        userStory: InputFragment
    ) {
        title.setHint(getString(R.string.requirement_title))
        description.setHint(getString(R.string.requirement_description))
        userStory.setHint(getString(R.string.requirement_user_story))
    }

    private fun configCreateRequirementButton(
        buttonFragment: ButtonFragment,
        type: Spinner,
        title: InputFragment,
        description: InputFragment,
        userStory: InputFragment,
        priority: Spinner,
        project: Project?
    ) {
        val button = buttonFragment.setupButton(getString(R.string.create), Color.argb(255, 93, 63, 211))
        button.setOnClickListener {
            val typeText = type.selectedItem.toString()
            val titleText = title.getText()
            val descriptionText = description.getText()
            val userStoryText = userStory.getText()
            val priorityText = priority.selectedItem.toString()

            val isRequirementValid = validateRequirement(typeText, titleText, descriptionText, userStoryText, priorityText, project)
            if (project != null && isRequirementValid) {
                val requirement = Requirement(
                    UUID.randomUUID(),
                    UUID.randomUUID().toString(),
                    titleText,
                    descriptionText,
                    userStoryText,
                    typeText,
                    "Origin",
                    priorityText,
                    project.id,
                    Calendar.getInstance().time,
                    null,
                )

                val result = dao.save(requirement)
                if (result > 0) {
                    showMessage("Requirement successfully created!")
                    val intent = Intent(this, ProjectHome::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showMessage("Requirement could not be registered, please try again!")
                }
            }
        }
    }

    private fun validateRequirement(
        type: String?,
        title: String?,
        description: String?,
        userStory: String?,
        priority: String?,
        project: Project?
    ): Boolean {
        if (project == null){
            showMessage("Requirement must be linked to a project!")
            return false
        } else if (type.equals("Select the requirement type...")) {
            showMessage("Requirement Type needs to be selected!")
            return false
        } else if (title.isNullOrEmpty()) {
            showMessage("Title field cannot be empty!")
            return false
        } else if(description.isNullOrEmpty()) {
            showMessage("Description field cannot be empty!")
            return false
        } else if(userStory.isNullOrEmpty()) {
            showMessage("User Story field cannot be empty!")
            return false
        } else if(priority.equals("Select the requirement priority...")) {
            showMessage("Requirement Priority needs to be selected!")
            return false
        }

        return true
    }
}