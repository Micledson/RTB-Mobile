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
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ComponentProjectCardBinding
import com.rtb.rtb.model.Project
import com.rtb.rtb.view.ViewProject
import java.text.SimpleDateFormat
import java.util.Locale

class ResumeCardAdapter(val context: Context, val projects: MutableList<Project>): BaseAdapter() {
    private val dao by lazy {
        DatabaseHelper.getInstance(context).projectDao()
    }

    override fun getCount(): Int {
        return projects.size
    }

    override fun getItem(i: Int): Any {
        return projects[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        val projectCardInflater = LayoutInflater.from(context)
        val projectCardBinding = ComponentProjectCardBinding.inflate(projectCardInflater, viewGroup, false)

        projectSetup(projectCardBinding, i)

        val selectProject = projectCardBinding.pcConstraintLayoutProjectContent
        selectProject.setOnClickListener {
            //TODO: Implement me
        }

        val readProject = projectCardBinding.pcImageViewRead
        readProject.setOnClickListener {
            val projectCardIntent = Intent(context, ViewProject::class.java)

            val bundle = Bundle()
            bundle.putParcelable("projectModel", projects[i])

            projectCardIntent.putExtras(bundle)
            context.startActivity(projectCardIntent)
        }

        val updateProject = projectCardBinding.pcImageViewEdit
        updateProject.setOnClickListener {
            //TODO: Implement me
        }

        val deleteProject = projectCardBinding.pcImageViewDelete
        deleteProject.setOnClickListener {
            deleteProjectMethod(projects[i], i)
        }

        return projectCardBinding.root
    }

    private fun deleteProjectMethod(myProject: Project, i : Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setIcon(R.drawable.baseline_are_you_sure_delete_24)
        alertDialogBuilder.setTitle("Delete this?")
        alertDialogBuilder.setMessage("This action cannot be undone!")

        alertDialogBuilder.setPositiveButton("Delete") { dialog, _ ->
            dao.deleteProject(myProject)
            projects.removeAt(i)
            notifyDataSetChanged()
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun projectSetup(
        projectCardBinding: ComponentProjectCardBinding,
        i: Int
    ) {
        projectCardBinding.pcTextViewProjectName.text = projects[i].name
        projectCardBinding.pcTextViewProjectAlias.text = projects[i].alias

        val dateParam = projects[i].createdAt
        val dateConverter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val date = dateConverter.format(dateParam)

        projectCardBinding.pcTextViewProjectCreatedAt.text = date

        if (!projects[i].isActive) {
            projectCardBinding.pcOverlayViewIsActive.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
        }
    }
}