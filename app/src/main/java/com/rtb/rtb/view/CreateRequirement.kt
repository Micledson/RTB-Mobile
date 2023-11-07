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
        configCreateRequirementButton(buttonFragment, type, title, description, userStory, priority, projectId)
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
        projectId: UUID
    ) {
        val button = buttonFragment.setupButton(getString(R.string.create), Color.argb(255, 93, 63, 211))
        button.setOnClickListener {
            val typeText = type.selectedItem.toString()
            val titleText = title.getText()
            val descriptionText = description.getText()
            val userStoryText = userStory.getText()
            val priorityText = priority.selectedItem.toString()

            val isRequirementValid = validateRequirement(typeText, titleText, descriptionText, userStoryText, priorityText, projectId)
            if (isRequirementValid) {
                val requirement = Requirement(
                    UUID.randomUUID(),
                    typeText,
                    titleText,
                    descriptionText,
                    userStoryText,
                    priorityText,
                    projectId,
                    true,
                    Calendar.getInstance().time,
                    null,
                    null
                )

                dao.createRequirement(requirement)
                showMessage("Requirement successfully registered!")

                val intent = Intent(this, ProjectHome::class.java)
                startActivity(intent)

                finish()
            }
        }
    }

    private fun validateRequirement(
        type: String?,
        title: String?,
        description: String?,
        userStory: String?,
        priority: String?,
        projectId: UUID?
    ): Boolean {
        if (projectId == null){
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
