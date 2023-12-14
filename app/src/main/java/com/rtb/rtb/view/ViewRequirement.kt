package com.rtb.rtb.view

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rtb.rtb.R
import com.rtb.rtb.databinding.ActivityViewRequirementBinding
import com.rtb.rtb.model.Project
import com.rtb.rtb.model.Requirement
import com.rtb.rtb.view.components.AppBarFragment
import java.text.SimpleDateFormat
import java.util.Locale

class ViewRequirement : AppCompatActivity() {
    private val binding by lazy {
        ActivityViewRequirementBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAppBar()
        displayRequirementDetails()
    }

    private fun setupAppBar() {
        val appBar = supportFragmentManager.findFragmentById(R.id.appBar) as AppBarFragment
        appBar.setupAppBar(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appBar.setModule(getString(R.string.rms))
        }
    }

    private fun displayRequirementDetails() {
        val requirement = getRequirementAccordingVersion()
        val project = getProjectAccordingVersion()

        var resources = ResourcesManager.getResources(this)

        binding.txtTitle.text = requirement?.title
        binding.txtText.text = "${project?.alias}-${requirement?.code}"
        binding.txtTypeField.text = resources?.types?.find { it.id == requirement?.typeId }?.name ?: ""
        binding.txtOriginField.text = resources?.origins?.find { it.id == requirement?.originId }?.name ?: ""
        binding.txtUserStoryField.text = requirement?.userStory
        binding.txtDescriptionField.text = requirement?.description

        val dateConverter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val createdAt = dateConverter.format(requirement?.createdAt!!)
        val updatedAt = dateConverter.format(requirement.updatedAt!!)

        binding.txtCreatedAt.text = createdAt
        binding.txtUpdatedAt.text = updatedAt
    }

    private fun getRequirementAccordingVersion(): Requirement? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("requirementModel", Requirement::class.java)
        } else {
            intent.getParcelableExtra("requirementModel")
        }
    }

    private fun getProjectAccordingVersion(): Project? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("projectModel", Project::class.java)
        } else {
            intent.getParcelableExtra("projectModel")
        }
    }
}
