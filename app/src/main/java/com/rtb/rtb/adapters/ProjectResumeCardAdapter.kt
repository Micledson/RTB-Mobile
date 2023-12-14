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
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.rtb.rtb.R
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ComponentProjectCardBinding
import com.rtb.rtb.model.Project
import com.rtb.rtb.networks.BaseRepository
import com.rtb.rtb.networks.ProjectRepository
import com.rtb.rtb.view.CollaboratorsHome
import com.rtb.rtb.view.RequirementHome
import com.rtb.rtb.view.UpdateProject
import com.rtb.rtb.view.ViewProject
import com.rtb.rtb.view.projectComponents.ProjectCardOptionsModal
import java.util.UUID

class ProjectResumeCardAdapter(val context: Context, val projects: MutableList<Project>): BaseAdapter() {

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
            val selectProjectIntent = Intent(context, RequirementHome::class.java)
            val bundle = Bundle()
            bundle.putString("projectId", projects[i].id.toString())
            selectProjectIntent.putExtras(bundle)
            context.startActivity(selectProjectIntent)
        }

        val menuButton = projectCardBinding.pcConstraintLayoutButton
        menuButton.setOnClickListener {
            val projectCardOptionsModal = ProjectCardOptionsModal(this, i)

            val location = IntArray(2)
            menuButton.getLocationInWindow(location)

            val x = location[0]
            val y = location[1]

            val bundle = Bundle()
            bundle.putInt("x", x)
            bundle.putInt("y", y)
            projectCardOptionsModal.arguments = bundle

            val fragmentManager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            projectCardOptionsModal.show(fragmentManager , projectCardOptionsModal.tag)
        }

        return projectCardBinding.root
    }

    fun deleteProjectMethod(myProject: Project, i : Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Delete this?")
        alertDialogBuilder.setMessage("This action cannot be undone!")

        alertDialogBuilder.setPositiveButton("Delete") { dialog, _ ->
            try {
                val projectRepository = ProjectRepository()
                projectRepository.deleteProject(context, myProject.id!!) { result ->
                    when(result) {
                        is BaseRepository.Result.Success -> {
                            projects.removeAt(i)
                            Toast.makeText(context, context.getString(R.string.delete_project_toast), Toast.LENGTH_SHORT).show()
                            notifyDataSetChanged()
                            dialog.dismiss()
                        }

                        is BaseRepository.Result.Error -> {
                            Toast.makeText(context, context.getString(R.string.error_deleting_project_toast), Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "An unexpected error appeared", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
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

        if (!projects[i].isActive) {
            projectCardBinding.pcOverlayViewIsActive.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
        }
    }

    fun openProjectReadView(project: Project) {
        val readProjectIntent = Intent(context, ViewProject::class.java)

        val bundle = Bundle()
        bundle.putParcelable("readProject", project)

        readProjectIntent.putExtras(bundle)
        context.startActivity(readProjectIntent)
    }

    fun openProjectEditView(projectId: UUID) {
        val updateProjectIntent = Intent(context, UpdateProject::class.java)

        updateProjectIntent.putExtra("uuid", projectId.toString())
        context.startActivity(updateProjectIntent)
    }

    fun openProjectCollaboratorsView(projectId: UUID) {
        val projectCollaboratorsIntent = Intent(context, CollaboratorsHome::class.java)

        projectCollaboratorsIntent.putExtra("uuid", projectId.toString())
        context.startActivity(projectCollaboratorsIntent)
    }
}