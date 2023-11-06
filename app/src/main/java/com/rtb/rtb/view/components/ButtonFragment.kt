package com.rtb.rtb.view.components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.rtb.rtb.databinding.FragmentButtonBinding

class ButtonFragment : Fragment() {
    private lateinit var binding: FragmentButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentButtonBinding.inflate(inflater, container, false)

        return binding.root
    }

    fun setupButton(text: String, color: Int = -474364): Button {
        binding.btn.text = text
        binding.btn.setBackgroundColor(color)
        return binding.btn
    }

}