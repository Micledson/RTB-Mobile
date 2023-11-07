package com.rtb.rtb.view

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.rtb.rtb.R
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ActivityUpdateRequirementBinding
import com.rtb.rtb.model.Requirement
import com.rtb.rtb.view.components.AppBarFragment
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val requirementId = UUID.fromString(intent.getStringExtra("requirementId"))
        val requirement = getRequirementById(requirementId)

        val appBar = supportFragmentManager.findFragmentById(R.id.appBar) as AppBarFragment
        appBar.setupAppBar(this)
        appBar.setModule("RMS")

        val requirementIdTextView = findViewById<TextView>(R.id.update_requirement_requirement_id)
        val type = findViewById<Spinner>(R.id.update_requirement_type)
        val title = supportFragmentManager.findFragmentById(R.id.update_requirement_title) as InputFragment
        val description = supportFragmentManager.findFragmentById(R.id.update_requirement_description) as InputFragment
        val userStory = supportFragmentManager.findFragmentById(R.id.update_requirement_user_story) as InputFragment
        val priority = findViewById<Spinner>(R.id.update_requirement_priority)
        val buttonFragment = supportFragmentManager.findFragmentById(R.id.update_requirement_button) as ButtonFragment

        requirementIdTextView.text = requirementId.toString()

        var options = listOf("Select the requirement type...", "Functional Requirement", "Non-Functional Requirement", "Inverse Requirement", "Business Rule")
        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        type.adapter = adapter
        var position = options.indexOf(requirement.type)
        type.setSelection(position)

        title.setText(requirement.title)

        description.setLines(5)
        description.setText(requirement.description)

        userStory.setLines(5)
        userStory.setText(requirement.userStory)

        options = listOf("Select the requirement priority...", "Urgent", "High", "Normal", "Low")
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        priority.adapter = adapter
        position = options.indexOf(requirement.priority)
        priority.setSelection(position)

        configInputFields(title, description, userStory)
        configUpdateRequirementButton(buttonFragment, type, title, description, userStory, priority, requirement.createdAt, requirement.id)
    }

    private fun getRequirementById(requirementId: UUID): Requirement {
        return dao.getRequirementById(requirementId)
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

    private fun configUpdateRequirementButton(
        buttonFragment: ButtonFragment,
        type: Spinner,
        title: InputFragment,
        description: InputFragment,
        userStory: InputFragment,
        priority: Spinner,
        createdAt: Date,
        id: UUID
    ) {
        val button = buttonFragment.setupButton(getString(R.string.update), Color.argb(255, 93, 63, 211))
        button.setOnClickListener {
            val typeText = type.selectedItem.toString()
            val titleText = title.getText()
            val descriptionText = description.getText()
            val userStoryText = userStory.getText()
            val priorityText = priority.selectedItem.toString()

            val isRequirementValid = validateRequirement(typeText, titleText, descriptionText, userStoryText, priorityText, id)
            if (isRequirementValid) {
                val requirement = Requirement(
                    id,
                    typeText,
                    titleText,
                    descriptionText,
                    userStoryText,
                    priorityText,
                    id,
                    true,
                    createdAt,
                    Calendar.getInstance().time,
                    null
                )

                dao.updateRequirement(requirement)
                showMessage("Requirement successfully updated!")

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
        id: UUID?
    ): Boolean {
        if (id == null){
            showMessage("Requirement ID not found!")
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
