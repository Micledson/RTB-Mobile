package com.rtb.rtb.view.components

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
import com.rtb.rtb.databinding.FragmentInputBinding

class InputFragment : Fragment() {
    private lateinit var binding: FragmentInputBinding

    private var nextFocusField: View? = null

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

    fun configureTextPasswordInputType() {
        binding.editTextInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.textInputLayout.endIconMode = END_ICON_PASSWORD_TOGGLE
    }

    private fun Float.dpToFloat(): Float {
        val scale = resources.displayMetrics.density
        return (this * scale)
    }

    fun setNextFocus(nextField: View) {
        this.nextFocusField = nextField
        binding.editTextInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                this.nextFocusField?.requestFocus()
                true
            } else {
                false
            }
        }
    }
}