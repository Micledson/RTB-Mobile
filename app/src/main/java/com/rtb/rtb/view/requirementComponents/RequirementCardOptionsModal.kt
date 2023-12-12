package com.rtb.rtb.view.requirementComponents

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.rtb.rtb.adapters.RequirementResumeCardAdapter
import com.rtb.rtb.databinding.ProjectCardOptionsModalBinding

class RequirementCardOptionsModal(private val adapter: RequirementResumeCardAdapter, private val index: Int) : DialogFragment() {
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
            adapter.openRequirementDetailsView(adapter.requirements[index])
            dismiss()
        }

        binding.modalTextViewEdit.setOnClickListener {
            adapter.openRequirementEditView(adapter.requirements[index].id)
            dismiss()
        }

        binding.modalTextViewDelete.setOnClickListener {
            adapter.deleteRequirement(adapter.requirements[index], index)
            dismiss()
        }
    }

    fun dpToPx(valueInDp: Float): Int {
        val metrics = resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics).toInt()
    }
}