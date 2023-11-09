package com.rtb.rtb.view

import android.os.Bundle
import android.widget.Toast
import com.rtb.rtb.R
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ActivityUpdateProjectBinding
import com.rtb.rtb.model.Project
import com.rtb.rtb.view.components.AppBarFragment
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment
import java.util.Date
import java.util.UUID

class UpdateProject : BaseActivity() {

    private val binding by lazy {
        ActivityUpdateProjectBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        DatabaseHelper.getInstance(this).projectDao()
    }

    lateinit var project: Project

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val uuid = UUID.fromString(intent.getStringExtra(ID))
        getProject(uuid)

        val projectName = supportFragmentManager.findFragmentById(R.id.projectName) as InputFragment
        val description =
            supportFragmentManager.findFragmentById(R.id.projectDescription) as InputFragment
        val alias = supportFragmentManager.findFragmentById(R.id.projectAlias) as InputFragment
        val buttonFragment = supportFragmentManager.findFragmentById(R.id.btn) as ButtonFragment
        val appBar = supportFragmentManager.findFragmentById(R.id.appBar) as AppBarFragment
        appBar.setupAppBar(this)


        projectName.setHint(getString(R.string.project_name))
        projectName.setText(project.name)
        projectName.setMaxLenght(30)

        description.setHint(getString(R.string.description))
        description.setHeight(175F)
        description.setText(project.description)
        description.setLines(5)

        alias.setHint(getString(R.string.project_alias))
        alias.setWidth(175F)
        alias.setText(project.alias)
        alias.setMaxLenght(12)

        binding.switch1.isChecked = project.isActive

        val button = buttonFragment.setupButton(getString(R.string.update))
        button.setOnClickListener {
            val isActive = binding.switch1.isChecked

            if (projectName.hasText() && description.hasText() && alias.hasText()) {
                val project = Project(
                    uuid,
                    projectName.getText(),
                    alias.getText(),
                    description.getText(),
                    isActive,
                    Date(),
                    Date(),
                    null
                )
                dao.updateProject(project)

                Toast.makeText(this, getString(R.string.update_project_toast), Toast.LENGTH_SHORT)
                    .show()

                finish()
            }
            else {
                Toast.makeText(this, getString(R.string.required_field), Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun getProject(uuid: UUID) {
        project = dao.getProjectByUUID(uuid)

    }

    companion object {
        const val ID: String = "uuid"
    }

}