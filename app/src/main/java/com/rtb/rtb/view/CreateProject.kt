package com.rtb.rtb.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.rtb.rtb.R
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ActivityCreateProjectBinding
import com.rtb.rtb.model.Project
import com.rtb.rtb.view.components.AppBarFragment
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment
import java.util.Date
import java.util.UUID

class CreateProject : AppCompatActivity() {
    private val binding by lazy {
        ActivityCreateProjectBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        DatabaseHelper.getInstance(this).projectDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val projectName = supportFragmentManager.findFragmentById(R.id.projectName) as InputFragment
        val description = supportFragmentManager.findFragmentById(R.id.projectDescription) as InputFragment
        val alias = supportFragmentManager.findFragmentById(R.id.projectAlias) as InputFragment
        val buttonFragment = supportFragmentManager.findFragmentById(R.id.btn) as ButtonFragment
        val appBar = supportFragmentManager.findFragmentById(R.id.appBar) as AppBarFragment
        appBar.setupAppBar(this)

        projectName.setHint(getString(R.string.project_name))

        description.setHint(getString(R.string.description))
        description.setHeight(175F)

        alias.setHint(getString(R.string.project_alias))
        alias.setWidth(175F)

        val button = buttonFragment.setupButton(getString(R.string.create))
        button.setOnClickListener {
            val project = Project(
                UUID.randomUUID(),
                projectName.getText(),
                alias.getText(),
                description.getText(),
                true,
                Date(),
                null,
                null
            )

            dao.createProject(project)

            sayMyName(projectName.getText())
            sayMyName(description.getText())
            sayMyName(alias.getText())

        }

    }

    fun sayMyName(name: String) {
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
    }
}