package com.rtb.rtb.view

import android.content.Intent
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

    private val projectDao by lazy {
        DatabaseHelper.getInstance(this).projectDao()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val requirementId = UUID.fromString(intent.getStringExtra("requirementId"))
        val requirement = dao.getRequirementById(requirementId)

        val appBar = supportFragmentManager.findFragmentById(R.id.app_bar) as AppBarFragment
        appBar.setupAppBar(this)
        appBar.setModule(getString(R.string.rms))

        val requirementCodeTextView = findViewById<TextView>(R.id.update_requirement_requirement_code)
        requirementCodeTextView.text = getString(R.string.requirement_code_value,
            projectDao.getProjectAliasByProjectId(requirement.projectId), requirement.code)

        val type = findViewById<Spinner>(R.id.update_requirement_type)
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
        var position = options.indexOf(requirement.type)
        type.setSelection(position)

        val origin = findViewById<Spinner>(R.id.update_requirement_origin)
        options = listOf(
            getString(R.string.requirement_select_origin),
            getString(R.string.requirement_select_origin_product),
            getString(R.string.requirement_select_origin_organization),
            getString(R.string.requirement_select_origin_external),
        )
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        origin.adapter = adapter
        position = options.indexOf(requirement.origin)
        origin.setSelection(position)

        val priority = findViewById<Spinner>(R.id.update_requirement_priority)
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
        position = options.indexOf(requirement.priority)
        priority.setSelection(position)

        val title = supportFragmentManager.findFragmentById(R.id.update_requirement_title) as InputFragment
        title.setText(requirement.title)

        val userStory = supportFragmentManager.findFragmentById(R.id.update_requirement_user_story) as InputFragment
        userStory.setLines(5)
        userStory.setText(requirement.userStory)

        val notes = supportFragmentManager.findFragmentById(R.id.update_requirement_notes) as InputFragment
        notes.setLines(5)
        notes.setText(requirement.notes)

        val buttonFragment = supportFragmentManager.findFragmentById(R.id.update_requirement_button) as ButtonFragment

        configInputFields(title, userStory, notes)
        configUpdateRequirementButton(buttonFragment, requirement.id, requirement.code, type,
            origin, priority, title, userStory, notes, requirement.projectId, requirement.createdAt)
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
                    typeText,
                    originText,
                    priorityText,
                    titleText,
                    userStoryText,
                    notesText,
                    projectId,
                    createdAt,
                    Calendar.getInstance().time,
                    null
                )

                try {
                    dao.updateRequirement(requirement)
                    showMessage(getString(R.string.requirement_updated_successfully))

                    val intent = Intent(this, ProjectHome::class.java)
                    startActivity(intent)

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
}
