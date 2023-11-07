package com.rtb.rtb.view

import android.content.Intent
import android.os.Bundle
import com.rtb.rtb.R
import com.rtb.rtb.model.User
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ActivitySignUpBinding
import com.rtb.rtb.util.ValidatorUtils
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment

class SignUp : BaseActivity() {
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
                    emailAddressText,
                    firstNameText,
                    lastNameText,
                    passwordText
                )

                val result = dao.save(user)
                if (result > 0) {
                    showMessage("Usuário cadastrado com sucesso!")
                    val intent = Intent(this, ProjectHome::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showMessage("Não foi possível cadastrar o usuário, tente novamente!")
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
            showMessage("O campo de endereço de e-mail não pode estar vazio!")
            return false
        } else if (!ValidatorUtils.isEmailValid(emailAddress)) {
            showMessage("O campo de endereço de e-mail não possui um formato válido!")
            return false
        } else if(firstName.isNullOrEmpty()) {
            showMessage("O campo de primeiro nome não pode estar vazio!")
            return false
        } else if(lastName.isNullOrEmpty()) {
            showMessage("O campo de último nome não pode estar vazio!")
            return false
        } else if(password.isNullOrEmpty()) {
            showMessage("O campo de senha não pode estar vazio!")
            return false
        } else if(password.length < 6) {
            showMessage("O campo de senha precisa ter pelo menos 6 caracteres!")
            return false
        } else if(confirmPassword.isNullOrEmpty()) {
            showMessage("O campo de confirmação da senha não pode estar vazio!")
            return false
        } else if(confirmPassword.length < 6) {
            showMessage("O campo de confirmação da senha precisa ter pelo menos 6 caracteres!")
            return false
        } else if (password != confirmPassword) {
            showMessage("A senha e a confirmação da senha precisam ser iguais!")
            return false
        }

        return true
    }
}