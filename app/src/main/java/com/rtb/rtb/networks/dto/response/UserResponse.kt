package com.rtb.rtb.networks.dto.response

import com.google.gson.annotations.SerializedName

class UserResponse {
    @SerializedName("access_token")
    var accessToken: String? = null
}