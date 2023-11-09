package com.rtb.rtb.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ComponentRequirementCardBinding
import com.rtb.rtb.model.Project
import com.rtb.rtb.model.Requirement
import com.rtb.rtb.view.UpdateRequirement
import com.rtb.rtb.view.ViewRequirement
import java.util.UUID

class RequirementResumeCardAdapter(
    val context: Context,
    val requirements: MutableList<Requirement>,
    val project: Project
) : BaseAdapter() {

    private val dao by lazy {
        DatabaseHelper.getInstance(context).requirementDao()
    }

    override fun getCount(): Int {
        return requirements.size
    }

    override fun getItem(i: Int): Any {
        return requirements[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        val requirementCardInflater = LayoutInflater.from(context)
        val requirementCardBinding = ComponentRequirementCardBinding.inflate(requirementCardInflater, viewGroup, false)

        requirementSetup(requirementCardBinding, i)

        val selectRequirement = requirementCardBinding.cardView
        selectRequirement.setOnClickListener {
            openRequirementDetailsView(requirements[i])
        }

        val readRequirement = requirementCardBinding.rcImageViewRead
        readRequirement.setOnClickListener {
            openRequirementDetailsView(requirements[i])
        }

        val updateRequirement = requirementCardBinding.rcImageViewEdit
        updateRequirement.setOnClickListener {
            openRequirementEditView(requirements[i].id)
        }

        val deleteRequirement = requirementCardBinding.pcImageViewDelete
        deleteRequirement.setOnClickListener {
            deleteRequirement(requirements[i], i)
        }

        return requirementCardBinding.root
    }

    private fun requirementSetup(requirementCardBinding: ComponentRequirementCardBinding, i: Int) {
        requirementCardBinding.rcRequirementCode.text = "${project.alias}-${requirements[i].code}"
        requirementCardBinding.rcRequirementTitle.text = requirements[i].title
    }

    private fun openRequirementDetailsView(requirement: Requirement) {
        val requirementCardIntent = Intent(context, ViewRequirement::class.java)

        val bundle = Bundle()
        bundle.putParcelable("requirementModel", requirement)
        bundle.putParcelable("projectModel", project)

        requirementCardIntent.putExtras(bundle)
        context.startActivity(requirementCardIntent)
    }

    private fun openRequirementEditView(requirementId: UUID) {
        val requirementCardIntent = Intent(context, UpdateRequirement::class.java)

        val bundle = Bundle()
        bundle.putString("requirementId", requirementId.toString())

        requirementCardIntent.putExtras(bundle)
        context.startActivity(requirementCardIntent)
    }

    private fun deleteRequirement(requirement: Requirement, i: Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Delete this?")
        alertDialogBuilder.setMessage("This action cannot be undone!")

        alertDialogBuilder.setPositiveButton("Delete") { dialog, _ ->
            dao.deleteRequirement(requirement)
            requirements.removeAt(i)
            notifyDataSetChanged()
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
