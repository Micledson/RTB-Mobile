package com.rtb.rtb.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.rtb.rtb.R
import com.rtb.rtb.databinding.ActivityCreateProjectBinding
import com.rtb.rtb.view.components.AppBarFragment
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment

class CreateProject : AppCompatActivity() {
    private val binding by lazy {
        ActivityCreateProjectBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_project)

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
        button.setOnClickListener{
            sayMyName(projectName.getText())
            sayMyName(description.getText())
            sayMyName(alias.getText())

        }

    }

    fun sayMyName(name: String) {
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
    }
}