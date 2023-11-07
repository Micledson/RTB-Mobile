package com.rtb.rtb.view

import android.content.Intent
import android.os.Bundle
import com.rtb.rtb.R
import com.rtb.rtb.model.User
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ActivitySignUpBinding
import com.rtb.rtb.messages.SignUpMessages
import com.rtb.rtb.util.ValidatorUtils
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment

class SignUp : BaseActivity() {
    private val signUpMessages = SignUpMessages()

    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        DatabaseHelper.getInstance(this).userDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val emailAddress = supportFragmentManager.findFragmentById(R.id.emailAddress) as InputFragment
        val firstName = supportFragmentManager.findFragmentById(R.id.firstName) as InputFragment
        val lastName = supportFragmentManager.findFragmentById(R.id.lastName) as InputFragment
        val password = supportFragmentManager.findFragmentById(R.id.password) as InputFragment
        val confirmPassword = supportFragmentManager.findFragmentById(R.id.confirmPassword) as InputFragment
        val buttonFragment = supportFragmentManager.findFragmentById(R.id.signInBtn) as ButtonFragment

        configInputFields(emailAddress, firstName, lastName, password, confirmPassword)
        configSignInLabel()
        configSignUpButton(buttonFragment, password, confirmPassword, emailAddress, firstName, lastName)
    }

    private fun configInputFields(
        emailAddress: InputFragment,
        firstName: InputFragment,
        lastName: InputFragment,
        password: InputFragment,
        confirmPassword: InputFragment
    ) {
        emailAddress.setHint(getString(R.string.email_address))
        firstName.setHint(getString(R.string.first_name))
        lastName.setHint(getString(R.string.last_name))
        lastName.view?.let { firstName.setNextFocus(it) }
        password.setHint(getString(R.string.password))
        password.configureTextPasswordInputType()
        confirmPassword.setHint(getString(R.string.confirm_password))
        confirmPassword.configureTextPasswordInputType()
    }

    private fun configSignInLabel() {
        binding.signInLabel.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun configSignUpButton(
        buttonFragment: ButtonFragment,
        password: InputFragment,
        confirmPassword: InputFragment,
        emailAddress: InputFragment,
        firstName: InputFragment,
        lastName: InputFragment
    ) {
        val button = buttonFragment.setupButton(getString(R.string.sign_up))
        button.setOnClickListener {
            val emailAddressText = emailAddress.getText()
            val firstNameText = firstName.getText()
            val lastNameText = lastName.getText()
            val passwordText = password.getText()
            val confirmPasswordText = confirmPassword.getText()

            val isUserValid = validateUser(emailAddressText, firstNameText, lastNameText, passwordText, confirmPasswordText)
            if (isUserValid) {
                val user = User(
                    emailAddressText.lowercase(),
                    firstNameText,
                    lastNameText,
                    passwordText
                )

                try {
                    dao.save(user)
                    showMessage("User created successfully!")
                    val intent = Intent(this, ProjectHome::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    showMessage("Unable to register user, please try again!")
                }
            }
        }
    }

    private fun validateUser(
        emailAddress: String?,
        firstName: String?,
        lastName: String?,
        password: String?,
        confirmPassword: String?
    ): Boolean {
        if (emailAddress.isNullOrEmpty()) {
            showMessage(signUpMessages.emailIsNullErrorMessage)
            return false
        } else if (!ValidatorUtils.isEmailValid(emailAddress)) {
            showMessage(signUpMessages.emailIsInvalidErrorMessage)
            return false
        } else if(firstName.isNullOrEmpty()) {
            showMessage(signUpMessages.firstNameIsNullErrorMessage)
            return false
        } else if(lastName.isNullOrEmpty()) {
            showMessage(signUpMessages.lastNameIsNullErrorMessage)
            return false
        } else if(password.isNullOrEmpty()) {
            showMessage(signUpMessages.passwordIsNullErrorMessage)
            return false
        } else if(password.length < 6) {
            showMessage(signUpMessages.passwordIsInvalidErrorMessage)
            return false
        } else if(confirmPassword.isNullOrEmpty()) {
            showMessage(signUpMessages.passwordConfirmationIsNullErrorMessage)
            return false
        } else if(confirmPassword.length < 6) {
            showMessage(signUpMessages.passwordConfirmationIsInvalidErrorMessage)
            return false
        } else if (password != confirmPassword) {
            showMessage(signUpMessages.passwordsIsNotEqualsErrorMessage)
            return false
        } else if (dao.getUserByEmail(emailAddress)) {
            showMessage(signUpMessages.emailIsAlreadyRegisteredErrorMessage)
            return false
        }

        return true
    }
}