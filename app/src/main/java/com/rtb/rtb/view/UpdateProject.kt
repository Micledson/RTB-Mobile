package com.rtb.rtb.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.rtb.rtb.R
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.database.preferences.SharedPrefs
import com.rtb.rtb.databinding.ActivityUpdateProjectBinding
import com.rtb.rtb.model.Project
import com.rtb.rtb.model.fromResponse
import com.rtb.rtb.model.toRequest
import com.rtb.rtb.networks.ProjectRepository
import com.rtb.rtb.view.components.AppBarFragment
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val bodyLayout = findViewById<ConstraintLayout>(R.id.body_constraint_layout)

        progressBar.visibility = View.VISIBLE
        bodyLayout.visibility = View.GONE

        val uuid = UUID.fromString(intent.getStringExtra(ID))

        runBlocking {
            withContext(Dispatchers.IO) {
                val projectRepository = ProjectRepository()
                projectRepository.getProjectByID(uuid) { projectResponse ->
                    if (projectResponse != null) {
                        project = fromResponse(projectResponse)
                        progressBar.visibility = View.GONE
                        bodyLayout.visibility = View.VISIBLE
                        setupProjectUI(uuid)
                    } else {
                        Toast.makeText(
                            this@UpdateProject,
                            getString(R.string.error_getting_project_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            }
        }
    }

    private fun setupProjectUI(uuid: UUID) {
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
                    null,
                    SharedPrefs(this).getUserEmail()
                )
                updateProject(uuid, project)
                dao.updateProject(project)

                Toast.makeText(this, getString(R.string.update_project_toast), Toast.LENGTH_SHORT)
                    .show()

                finish()
            } else {
                Toast.makeText(this, getString(R.string.required_field), Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun updateProject(id: UUID, project: Project) {
        val projectRepository = ProjectRepository()
        projectRepository.updateProject(this, id, project.toRequest())
    }


    companion object {
        const val ID: String = "uuid"
    }

}