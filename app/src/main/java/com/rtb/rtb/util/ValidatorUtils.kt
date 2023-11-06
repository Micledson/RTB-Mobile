package com.rtb.rtb.util

class ValidatorUtils {
    companion object {
        fun isEmailValid(email: String): Boolean {
            val regexPattern = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
            return email.matches(Regex(regexPattern))
        }
    }
}