package com.rtb.rtb.view.projectComponents

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.rtb.rtb.adapters.ProjectResumeCardAdapter
import com.rtb.rtb.databinding.ProjectCardOptionsModalBinding
import com.rtb.rtb.model.Project
import com.rtb.rtb.view.UpdateProject
import com.rtb.rtb.view.ViewProject

class ProjectCardOptionsModal(private val adapter: ProjectResumeCardAdapter, private val index: Int) : DialogFragment() {
    lateinit var binding: ProjectCardOptionsModalBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProjectCardOptionsModalBinding.inflate(inflater, container, false)

        val x = arguments?.getInt("x") ?: 0
        val y = arguments?.getInt("y") ?: 0
        dialog?.window?.setGravity(Gravity.TOP or Gravity.LEFT)

        dialog?.window?.attributes?.x = x
        dialog?.window?.attributes?.y = y  - dpToPx(70.toFloat())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupModalOptions()
    }

    private fun setupModalOptions() {
        binding.modalTextViewRead.setOnClickListener {
            val readProjectIntent = Intent(adapter.context, ViewProject::class.java)

            val bundle = Bundle()
            bundle.putParcelable("readProject", adapter.projects[index])

            readProjectIntent.putExtras(bundle)
            adapter.context.startActivity(readProjectIntent)
            dismiss()
        }

        binding.modalTextViewEdit.setOnClickListener {
            val updateProjectIntent = Intent(context, UpdateProject::class.java)

            val project = adapter.getItem(index) as Project
            updateProjectIntent.putExtra("uuid", project.id.toString())
            adapter.context.startActivity(updateProjectIntent)
            dismiss()
        }

        binding.modalTextViewDelete.setOnClickListener {
            adapter.deleteProjectMethod(adapter.projects[index], index)
            dismiss()
        }
    }

    fun dpToPx(valueInDp: Float): Int {
        val metrics = resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics).toInt()
    }
}