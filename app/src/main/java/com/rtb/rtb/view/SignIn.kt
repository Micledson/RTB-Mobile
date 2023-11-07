package com.rtb.rtb.view

import com.rtb.rtb.database.DatabaseHelper
import android.content.Intent
import android.os.Bundle
import com.rtb.rtb.R
import com.rtb.rtb.database.preferences.SharedPrefs
import com.rtb.rtb.databinding.ActivitySignInBinding
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment

class SignIn : BaseActivity() {
    private val binding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        DatabaseHelper.getInstance(this).userDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val emailAddress = supportFragmentManager.findFragmentById(R.id.emailAddress) as InputFragment
        val password =
            supportFragmentManager.findFragmentById(R.id.password) as InputFragment
        val buttonFragment = supportFragmentManager.findFragmentById(R.id.signInBtn) as ButtonFragment

        configInputFields(emailAddress, password)

        configSignUpLabel()
        configSignInButton(buttonFragment, emailAddress, password)
    }

    private fun configInputFields(
        emailAddress: InputFragment,
        password: InputFragment
    ) {
        emailAddress.setHint(getString(R.string.email_address))
        password.setHint(getString(R.string.password))
        password.configureTextPasswordInputType()
    }

    private fun configSignUpLabel() {
        binding.signUpLabel.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun configSignInButton(
        buttonFragment: ButtonFragment,
        emailAddress: InputFragment,
        password: InputFragment
    ) {
        val button = buttonFragment.setupButton(getString(R.string.sign_in))
        button.setOnClickListener {
            dao.authenticate(emailAddress.getText(), password.getText())?.let {
                SharedPrefs(this).setUserValue(true)

                val intent = Intent(this, ProjectHome::class.java)
                startActivity(intent)
                finish()
            } ?: showMessage("Usuário/Senha Incorreto(s)")
        }
    }
}