package com.rtb.rtb.networks.dto.request

data class UserRequest (
    var email: String? = null,
    var password: String? = null,
    var firstName: String? = null,
    var lastName: String? = null
)