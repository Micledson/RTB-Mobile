package com.rtb.rtb.view

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rtb.rtb.R
import com.rtb.rtb.databinding.ActivityViewRequirementBinding
import com.rtb.rtb.model.Requirement
import com.rtb.rtb.view.components.AppBarFragment

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
            appBar.setModule("RMS")
        }
    }

    private fun displayRequirementDetails() {
        val requirement = getRequirementAccordingVersion()

        binding.vrRequirementCodeValue.text = requirement?.code
        binding.vrRequirementTitleValue.text = requirement?.title
        binding.vrRequirementDescriptionValue.text = requirement?.description
        binding.vrRequirementUserStoryValue.text = requirement?.userStory
        binding.vrRequirementTypeValue.text = requirement?.type
        binding.vrRequirementOriginValue.text = requirement?.origin
        binding.vrRequirementPriorityValue.text = requirement?.priority
        binding.vrRequirementProjectValue.text = requirement?.projectID.toString()
        binding.vrRequirementCreatedAtValue.text = requirement?.createdAt.toString()
        binding.vrRequirementUpdatedAtValue.text = requirement?.updatedAt.toString()
    }

    private fun getRequirementAccordingVersion(): Requirement? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("requirementModel", Requirement::class.java)
        } else {
            intent.getParcelableExtra("requirementModel")
        }
    }
}
