package com.rtb.rtb.view

import com.rtb.rtb.database.DatabaseHelper
import android.content.Intent
import android.os.Bundle
import com.rtb.rtb.R
import com.rtb.rtb.database.preferences.SharedPrefs
import com.rtb.rtb.databinding.ActivitySignInBinding
import com.rtb.rtb.model.User
import com.rtb.rtb.model.toRequest
import com.rtb.rtb.networks.ApiService
import com.rtb.rtb.networks.BaseRepository
import com.rtb.rtb.networks.UserRepository
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
        emailAddressInput: InputFragment,
        passwordInput: InputFragment
    ) {
        val button = buttonFragment.setupButton(getString(R.string.sign_in))
        button.setOnClickListener {
            val apiService = ApiService(this)
            val userRepository = UserRepository(apiService)
            try {
                val email = emailAddressInput.getText().lowercase()
                val password = passwordInput.getText()
                val user = User(email, "", "", password)
                userRepository.login(user.toRequest()) { result ->
                    when (result) {
                        is BaseRepository.Result.Success -> {
                            SharedPrefs(this).setUserValue(true)
                            SharedPrefs(this).setUserEmail(email)
                            SharedPrefs(this).setAccessToken(result.data?.accessToken)

                            ResourcesManager.initialize(this)

                            val intent = Intent(this, ProjectHome::class.java)
                            startActivity(intent)
                            finish()
                        }

                        is BaseRepository.Result.Error -> {
                            showMessage("Incorrect(s) User/Password")
                        }
                    }
                }
            } catch (e: Exception) {
                showMessage("An unexpected error appeared")
            }
        }
    }
}