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
import com.rtb.rtb.database.preferences.SharedPrefs
import com.rtb.rtb.databinding.ComponentCollaboratorCardBinding
import com.rtb.rtb.model.Collaborator
import com.rtb.rtb.networks.ApiService
import com.rtb.rtb.networks.BaseRepository
import com.rtb.rtb.networks.CollaboratorRepository
import com.rtb.rtb.networks.dto.request.CollaboratorRequest
import com.rtb.rtb.observer.CollaboratorsUpdateObserver
import java.util.UUID

class PossibleCollaboratorCardAdapter(
    val context: Context,
    val projectId: UUID,
    val owner: String?,
    val users: MutableList<Collaborator>,
    val collaboratorsUpdateObserver: CollaboratorsUpdateObserver
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

        possibleCollaboratorSetup(possibleCollaboratorCardBinding, i)

        val sharedPrefs = SharedPrefs(context)
        val loggedUser = sharedPrefs.getUserEmail()
        val menuButton = possibleCollaboratorCardBinding.ccConstraintLayoutButton
        if (owner.toString().equals(loggedUser, true)) {
            if (users[i].userEmail.equals(owner, true).not()) {
                val newDrawable = ContextCompat.getDrawable(context, R.drawable.baseline_add_24)
                possibleCollaboratorCardBinding.ccImageViewAction.setImageDrawable(newDrawable)

                val newColorTint = ContextCompat.getColorStateList(context, R.color.green)
                possibleCollaboratorCardBinding.ccImageViewAction.imageTintList = newColorTint

                menuButton.setOnClickListener {
                    addNewCollaborator(users[i])
                }
            } else {
                menuButton.visibility = View.GONE
            }
        } else {
            menuButton.visibility = View.GONE
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
                val apiService = ApiService(context)
                val newCollaboratorRequest = CollaboratorRequest(newCollaborator.userEmail)
                CollaboratorRepository(apiService).createCollaborator(this.projectId, newCollaboratorRequest) { result ->
                    when(result) {
                        is BaseRepository.Result.Success -> {
                            Toast.makeText(context, context.getString(R.string.add_collaborator_toast), Toast.LENGTH_SHORT).show()
                            users.remove(newCollaborator)
                            notifyDataSetChanged()
                            collaboratorsUpdateObserver.onCollaboratorsUpdated()
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
