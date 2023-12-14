package com.rtb.rtb.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.rtb.rtb.R
import com.rtb.rtb.databinding.ComponentCollaboratorCardBinding
import com.rtb.rtb.model.Collaborator
import com.rtb.rtb.networks.BaseRepository
import com.rtb.rtb.networks.CollaboratorRepository
import com.rtb.rtb.networks.dto.request.CollaboratorRequest
import java.util.UUID

class PossibleCollaboratorCardAdapter(
    val context: Context,
    val projectId: UUID,
    val users: MutableList<Collaborator>
) : BaseAdapter() {

    override fun getCount(): Int {
        return users.size
    }

    override fun getItem(i: Int): Any {
        return users[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }


    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        val possibleCollaboratorCardInflater = LayoutInflater.from(context)
        val possibleCollaboratorCardBinding = ComponentCollaboratorCardBinding.inflate(possibleCollaboratorCardInflater, viewGroup, false)

        val newDrawable = ContextCompat.getDrawable(context, R.drawable.baseline_add_24)
        possibleCollaboratorCardBinding.ccImageViewAction.setImageDrawable(newDrawable)

        val newColorTint = ContextCompat.getColorStateList(context, R.color.green)
        possibleCollaboratorCardBinding.ccImageViewAction.imageTintList = newColorTint

        possibleCollaboratorSetup(possibleCollaboratorCardBinding, i)

        val menuButton = possibleCollaboratorCardBinding.ccConstraintLayoutButton
        menuButton.setOnClickListener {
            addNewCollaborator(users[i])
        }

        return possibleCollaboratorCardBinding.root
    }

    private fun possibleCollaboratorSetup(possibleCollaboratorCardBinding: ComponentCollaboratorCardBinding, i: Int) {
        possibleCollaboratorCardBinding.ccTextViewCollaboratorFirstName.text = users[i].userFirstName
        possibleCollaboratorCardBinding.ccTextViewCollaboratorLastName.text = users[i].userLastName
        possibleCollaboratorCardBinding.ccTextViewCollaboratorEmail.text = users[i].userEmail
    }

    fun addNewCollaborator(newCollaborator: Collaborator) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Add ${newCollaborator.userFirstName}?")

        alertDialogBuilder.setPositiveButton("Add") { dialog, _ ->
            try {
                val newCollaboratorRequest = CollaboratorRequest(newCollaborator.userEmail)
                CollaboratorRepository().createCollaborator(context, this.projectId, newCollaboratorRequest) { result ->
                    when(result) {
                        is BaseRepository.Result.Success -> {
                            Toast.makeText(context, context.getString(R.string.add_collaborator_toast), Toast.LENGTH_SHORT).show()
                            users.add(newCollaborator)
                            notifyDataSetChanged()
                            dialog.dismiss()
                        }

                        is BaseRepository.Result.Error -> {
                            Toast.makeText(context, context.getString(R.string.error_adding_collaborator_toast), Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, context.getString(R.string.error_adding_collaborator_toast), Toast.LENGTH_SHORT).show()
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
