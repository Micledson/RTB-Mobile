package com.rtb.rtb.view.components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rtb.rtb.databinding.FragmentInputBinding

class InputFragment : Fragment() {
    private lateinit var binding: FragmentInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInputBinding.inflate(inflater, container, false)

        return binding.root
    }


    fun setHint(hint: String) {
        binding.textInputLayout.hint = hint
    }

    fun setHeight(height: Float) {
        binding.editTextInput.minHeight = height.dpToFloat().toInt()
        binding.editTextInput.setSelection(0)
    }

    fun setWidth(width: Float) {
        binding.editTextInput.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT

        binding.editTextInput.minWidth = width.dpToFloat().toInt()
    }

    fun getText(): String {
        return binding.editTextInput.text.toString()
    }
    private fun Float.dpToFloat(): Float {
        val scale = resources.displayMetrics.density
        return (this * scale)
    }
}