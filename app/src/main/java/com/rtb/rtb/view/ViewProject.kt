package com.rtb.rtb.view

import android.os.Build
import android.os.Bundle
import com.rtb.rtb.R
import com.rtb.rtb.databinding.ActivityViewProjectBinding
import com.rtb.rtb.model.Project
import com.rtb.rtb.view.components.AppBarFragment
import java.text.SimpleDateFormat
import java.util.Locale

class ViewProject : BaseActivity() {
    private val binding by lazy {
        ActivityViewProjectBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val appBar = supportFragmentManager.findFragmentById(R.id.appBar) as AppBarFragment
        appBar.setupAppBar(this)

        val viewProject = getProjectModelAccordingVersion()

        getProject(viewProject)

    }

    private fun getProjectModelAccordingVersion(): Project? {
        val viewProject = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("projectModel", Project::class.java)
        } else {
            intent.getParcelableExtra("projectModel")
        }
        return viewProject
    }

    private fun getProject(viewProject: Project?) {
        binding.vpTextViewProjectName.text = viewProject?.name
        binding.vpTextViewProjectAlias.text = viewProject?.alias
        binding.vpTextViewProjectIdField.text = viewProject?.id.toString()
        binding.vpTextViewProjectDescription.text = viewProject?.description

        if (!viewProject?.isActive!!) {
            binding.vpImageViewIsActiveIcon.setImageResource(R.drawable.baseline_inactive_24)
            binding.vpTextViewIsActiveText.text = getString(R.string.inactive)
        }

        val dateConverter = SimpleDateFormat("MM/dd/yyyy", Locale.US)

        val createdAtParam = viewProject?.createdAt
        val createdAt = dateConverter.format(createdAtParam)
        binding.vpTextViewCreatedAtField.text = createdAt

        if (viewProject?.updatedAt != null) {
            val updatedAtParam = viewProject?.createdAt
            val updatedAt = dateConverter.format(updatedAtParam)
            binding.vpTextViewUpdatedAtField.text = updatedAt
        }
    }
}