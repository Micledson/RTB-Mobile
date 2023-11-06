package com.rtb.rtb.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.rtb.rtb.R
import com.rtb.rtb.model.User
import com.rtb.rtb.database.DatabaseHelper
import com.rtb.rtb.databinding.ActivitySignUpBinding
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment

class SignUp : AppCompatActivity() {
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
        val button = buttonFragment.setupButton(getString(R.string.sign_in))
        button.setOnClickListener {
            if (password.getText() != confirmPassword.getText()) {
                showMessage("A senha e a confirmação da senha precisam ser iguais!")
            } else {
                val user = User(
                    emailAddress.getText(),
                    firstName.getText(),
                    lastName.getText(),
                    password.getText()
                )

                dao.save(user)
                showMessage("Usuário cadastrado com sucesso!")
                val intent = Intent(this, ProjectHome::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG,
        ).show()
    }
}