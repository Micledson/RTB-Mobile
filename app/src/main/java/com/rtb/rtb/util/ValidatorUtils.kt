package com.rtb.rtb.util

class ValidatorUtils {
    companion object {
        fun isEmailValid(email: String): Boolean {
            val regexPattern = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
            return email.matches(Regex(regexPattern))
        }

        fun isPasswordValid(password: String): Boolean {
            val regexPattern = "^(?=.*[a-zA-Z])(?=.*\\d).{6,}\$"
            return password.matches(Regex(regexPattern))
        }
    }
}