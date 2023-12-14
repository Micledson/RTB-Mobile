package com.rtb.rtb.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.rtb.rtb.R
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ComponentRequirementCardBinding
import com.rtb.rtb.model.Project
import com.rtb.rtb.model.Requirement
import com.rtb.rtb.networks.ApiService
import com.rtb.rtb.networks.BaseRepository
import com.rtb.rtb.networks.RequirementRepository
import com.rtb.rtb.view.UpdateRequirement
import com.rtb.rtb.view.ViewRequirement
import com.rtb.rtb.view.requirementComponents.RequirementCardOptionsModal
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

        val menuButton = requirementCardBinding.rcConstraintLayoutButton
        menuButton.setOnClickListener {
            val requirementCardOptionsModal = RequirementCardOptionsModal(this, i)

            val location = IntArray(2)
            menuButton.getLocationInWindow(location)

            val x = location[0]
            val y = location[1]

            val bundle = Bundle()
            bundle.putInt("x", x)
            bundle.putInt("y", y)
            requirementCardOptionsModal.arguments = bundle

            val fragmentManager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            requirementCardOptionsModal.show(fragmentManager , requirementCardOptionsModal.tag)
        }

        return requirementCardBinding.root
    }

    private fun requirementSetup(requirementCardBinding: ComponentRequirementCardBinding, i: Int) {
        requirementCardBinding.rcRequirementCode.text = "${project.alias}-${requirements[i].code}"
        requirementCardBinding.rcRequirementTitle.text = requirements[i].title
    }

    fun openRequirementDetailsView(requirement: Requirement) {
        val requirementCardIntent = Intent(context, ViewRequirement::class.java)

        val bundle = Bundle()
        bundle.putParcelable("requirementModel", requirement)
        bundle.putParcelable("projectModel", project)

        requirementCardIntent.putExtras(bundle)
        context.startActivity(requirementCardIntent)
    }

    fun openRequirementEditView(requirementId: UUID) {
        val requirementCardIntent = Intent(context, UpdateRequirement::class.java)

        val bundle = Bundle()
        bundle.putString("requirementId", requirementId.toString())

        requirementCardIntent.putExtras(bundle)
        context.startActivity(requirementCardIntent)
    }

    fun deleteRequirement(requirement: Requirement, i: Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Delete this?")
        alertDialogBuilder.setMessage("This action cannot be undone!")

        alertDialogBuilder.setPositiveButton("Delete") { dialog, _ ->
            try {
                val apiService = ApiService(context)
                RequirementRepository(apiService).deleteRequirement(context, requirement.id) { result ->
                    when(result) {
                        is BaseRepository.Result.Success -> {
                            Toast.makeText(context, context.getString(R.string.delete_requirement_toast), Toast.LENGTH_SHORT).show()
                            requirements.removeAt(i)
                            notifyDataSetChanged()
                            dialog.dismiss()
                        }

                        is BaseRepository.Result.Error -> {
                            Toast.makeText(context, context.getString(R.string.error_deleting_requirement_toast), Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, context.getString(R.string.error_deleting_requirement_toast), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
