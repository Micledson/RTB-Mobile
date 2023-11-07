package com.rtb.rtb.view.components

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rtb.rtb.R
import com.rtb.rtb.database.preferences.SharedPrefs
import com.rtb.rtb.databinding.FragmentAppBarBinding
import com.rtb.rtb.view.SignIn

class AppBarFragment : Fragment() {
    private lateinit var binding: FragmentAppBarBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppBarBinding.inflate(inflater, container, false)

        return binding.root
    }

    fun setupAppBar(context: Context, disableBackButton: Boolean = false) {
        if (disableBackButton) {
            binding.topAppBar.navigationIcon = null
        } else {
            binding.topAppBar.setNavigationOnClickListener{
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    logout(context)
                    true
                }

                else -> false
            }
        }
    }

    private fun logout(context: Context) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Exit the app?")
        alertDialogBuilder.setMessage("Are you sure you want to exit the app?")

        alertDialogBuilder.setPositiveButton("Exit") { dialog, _ ->
            dialog.dismiss()
            SharedPrefs(context).setUserValue(false)

            val intent = Intent(context, SignIn::class.java)
            startActivity(intent)
        }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}