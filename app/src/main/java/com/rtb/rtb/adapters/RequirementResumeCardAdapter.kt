package com.rtb.rtb.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.rtb.rtb.R
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ComponentRequirementCardBinding
import com.rtb.rtb.model.Project
import com.rtb.rtb.model.Requirement
import com.rtb.rtb.view.ViewRequirement

class RequirementResumeCardAdapter(val context: Context, val requirements: MutableList<Requirement>) : BaseAdapter() {

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
            openRequirement(requirements[i])
        }

        val readRequirement = requirementCardBinding.rcImageViewRead
        readRequirement.setOnClickListener {
            openRequirement(requirements[i])
        }

        val updateRequirement = requirementCardBinding.rcImageViewEdit
        updateRequirement.setOnClickListener {
            // TODO: implement me
        }

        val deleteRequirement = requirementCardBinding.pcImageViewDelete
        deleteRequirement.setOnClickListener {
            // TODO: implement me
        }

        return requirementCardBinding.root
    }

    private fun requirementSetup(requirementCardBinding: ComponentRequirementCardBinding, i: Int) {
        requirementCardBinding.requirementCode.text = requirements[i].code
        requirementCardBinding.requirementTitle.text = requirements[i].title
    }

    private fun openRequirement(requirement: Requirement) {
        val requirementCardIntent = Intent(context, ViewRequirement::class.java)

        val bundle = Bundle()
        bundle.putParcelable("requirementModel", requirement)

        requirementCardIntent.putExtras(bundle)
        context.startActivity(requirementCardIntent)
    }
}
