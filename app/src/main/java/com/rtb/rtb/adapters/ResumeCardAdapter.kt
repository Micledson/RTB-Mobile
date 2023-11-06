package com.rtb.rtb.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.rtb.rtb.R
import com.rtb.rtb.databinding.ComponentProjectCardBinding
import com.rtb.rtb.model.ProjectModel
import com.rtb.rtb.view.ViewProject
import java.text.SimpleDateFormat
import java.util.Locale

class ResumeCardAdapter(val context: Context, val projects: MutableList<ProjectModel>) :BaseAdapter() {
    override fun getCount(): Int {
        return projects.size
    }

    override fun getItem(p0: Int): Any {
        return projects[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val projectCardInflater = LayoutInflater.from(context)
        val projectCardBinding = ComponentProjectCardBinding.inflate(projectCardInflater, p2, false)

        projectSetup(projectCardBinding, p0)

        val selectProject = projectCardBinding.rcConstraintLayoutProjectContent
        selectProject.setOnClickListener{
            //TODO: Implement me
        }

        val readProject = projectCardBinding.rcImageViewRead
        readProject.setOnClickListener{
            val projectCardIntent = Intent(context, ViewProject::class.java)

            val bundle = Bundle()
            bundle.putParcelable("projectModel", projects[p0])

            projectCardIntent.putExtras(bundle)
            context.startActivity(projectCardIntent)
        }

        val updateProject = projectCardBinding.rcImageViewEdit
        updateProject.setOnClickListener{
            //TODO: Implement me
        }

        val deleteProject = projectCardBinding.rcImageViewDelete
        deleteProject.setOnClickListener{
            deleteProjectMethod(p0)
        }

        return projectCardBinding.root
    }

    private fun deleteProjectMethod(p0: Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setIcon(R.drawable.baseline_are_you_sure_delete_24)
        alertDialogBuilder.setTitle("Delete this?")
        alertDialogBuilder.setMessage("This action cannot be undone!")

        alertDialogBuilder.setPositiveButton("Delete") { dialog, which ->
            projects.removeAt(p0)
            notifyDataSetChanged()
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun projectSetup(
        projectCardBinding: ComponentProjectCardBinding,
        p0: Int
    ) {
        projectCardBinding.rcTextViewProjectName.text = projects[p0].name
        projectCardBinding.rcTextViewProjectAlias.text = projects[p0].alias

        val dateParam = projects[p0].createdAt
        val dateConverter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val date = dateConverter.format(dateParam)

        projectCardBinding.rcTextViewProjectCreatedAt.text = date

        if (!projects[p0].isActive) {
            projectCardBinding.rcOverlayViewIsActive.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
        }
    }
}