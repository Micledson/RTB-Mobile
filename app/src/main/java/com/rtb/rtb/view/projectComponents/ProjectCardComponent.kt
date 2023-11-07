package com.rtb.rtb.view.projectComponents

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rtb.rtb.databinding.ComponentProjectCardBinding


class ProjectCardComponent : AppCompatActivity() {
    private val binding by lazy {
        ComponentProjectCardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        val selectProject = binding.pcConstraintLayoutProjectContent
        selectProject.setOnClickListener {
            //TODO: Implement me
        }
    }
}