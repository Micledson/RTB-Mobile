package com.rtb.rtb.networks.dto.request

import com.google.gson.annotations.SerializedName

data class CollaboratorRequest (
    @SerializedName("user_email")
    var email: String
)
