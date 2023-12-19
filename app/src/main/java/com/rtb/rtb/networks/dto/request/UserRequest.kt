package com.rtb.rtb.networks.dto.request

import com.google.gson.annotations.SerializedName

data class UserRequest (
    var email: String? = null,
    var password: String? = null,
    @SerializedName("first_name")
    var firstName: String? = null,
    @SerializedName("last_name")
    var lastName: String? = null
)