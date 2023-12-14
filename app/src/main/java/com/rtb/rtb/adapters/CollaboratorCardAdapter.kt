package com.rtb.rtb.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.rtb.rtb.R
import com.rtb.rtb.databinding.ComponentCollaboratorCardBinding
import com.rtb.rtb.model.Collaborator
import com.rtb.rtb.networks.BaseRepository
import com.rtb.rtb.networks.CollaboratorRepository
import java.util.UUID

class CollaboratorCardAdapter(
    val context: Context,
    val projectId: UUID,
    val collaborators: MutableList<Collaborator>
) : BaseAdapter() {

    override fun getCount(): Int {
        return collaborators.size
    }

    override fun getItem(i: Int): Any {
        return collaborators[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }


    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        val collaboratorCardInflater = LayoutInflater.from(context)
        val collaboratorCardBinding = ComponentCollaboratorCardBinding.inflate(collaboratorCardInflater, viewGroup, false)

        collaboratorSetup(collaboratorCardBinding, i)

        val menuButton = collaboratorCardBinding.ccConstraintLayoutButton
        menuButton.setOnClickListener {
            deleteCollaborator(collaborators[i], i)
        }

        return collaboratorCardBinding.root
    }

    private fun collaboratorSetup(collaboratorCardBinding: ComponentCollaboratorCardBinding, i: Int) {
        collaboratorCardBinding.ccTextViewCollaboratorFirstName.text = collaborators[i].userFirstName
        collaboratorCardBinding.ccTextViewCollaboratorLastName.text = collaborators[i].userLastName
        collaboratorCardBinding.ccTextViewCollaboratorEmail.text = collaborators[i].userEmail
    }

    fun deleteCollaborator(collaborator: Collaborator, i: Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Delete ${collaborator}?")

        alertDialogBuilder.setPositiveButton("Delete") { dialog, _ ->
            try {
                CollaboratorRepository().deleteCollaborator(context, this.projectId, collaborator.userId) { result ->
                    when(result) {
                        is BaseRepository.Result.Success -> {
                            Toast.makeText(context, context.getString(R.string.delete_collaborator_toast), Toast.LENGTH_SHORT).show()
                            collaborators.removeAt(i)
                            notifyDataSetChanged()
                            dialog.dismiss()
                        }

                        is BaseRepository.Result.Error -> {
                            Toast.makeText(context, context.getString(R.string.error_deleting_collaborator_toast), Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, context.getString(R.string.error_deleting_collaborator_toast), Toast.LENGTH_SHORT).show()
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
