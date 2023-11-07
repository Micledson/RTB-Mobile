package com.rtb.rtb.view

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rtb.rtb.databinding.ActivityViewRequirementBinding
import com.rtb.rtb.model.RequirementModel
import java.util.Date
import java.util.UUID

class ViewRequirement : AppCompatActivity() {
    private val binding by lazy {
        ActivityViewRequirementBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //val requirement = RequirementModel(UUID.randomUUID(), "RF-1", "Title 1", "Description 1", "User story 1", "Type 1", "Origin 1", "Priority 1", "Project 1", Date(), Date())
        //getRequirement(requirement)
    }

    private fun getRequirementModelAccordingVersion(): RequirementModel? {
        val requirement = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("requirementModel", RequirementModel::class.java)
        } else {
            intent.getParcelableExtra("requirementModel")
        }
        return requirement
    }

    private fun getRequirement(requirement: RequirementModel?) {
        binding.vrRequirementCodeValue.text = requirement?.code
        binding.vrRequirementTitleValue.text = requirement?.title
        binding.vrRequirementDescriptionValue.text = requirement?.description
        binding.vrRequirementUserStoryValue.text = requirement?.userStory
        binding.vrRequirementTypeValue.text = requirement?.type
        binding.vrRequirementOriginValue.text = requirement?.origin
        binding.vrRequirementPriorityValue.text = requirement?.priority
        binding.vrRequirementProjectValue.text = requirement?.project
        binding.vrRequirementCreatedAtValue.text = requirement?.createdAt.toString()
        binding.vrRequirementUpdatedAtValue.text = requirement?.updatedAt.toString()
    }
}