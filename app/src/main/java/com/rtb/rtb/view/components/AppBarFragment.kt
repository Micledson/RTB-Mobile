package com.rtb.rtb.view.components

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.rtb.rtb.R
import com.rtb.rtb.database.preferences.SharedPrefs
import com.rtb.rtb.databinding.FragmentAppBarBinding
import com.rtb.rtb.view.CreateProject
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

    fun setupAppBar(context: Context) {
        binding.topAppBar.setNavigationOnClickListener{
            Toast.makeText(context, "back", Toast.LENGTH_SHORT).show()
        }
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    SharedPrefs(context).setUserValue(false)

                    val intent = Intent(context, SignIn::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

}