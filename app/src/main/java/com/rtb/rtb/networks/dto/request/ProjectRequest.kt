package com.rtb.rtb.networks.dto.request

import com.google.gson.annotations.SerializedName

data class ProjectRequest (
    var alias: String? = null,
    var description: String? = null,
    @SerializedName("is_active")
    var isActive: Boolean? = null,
    var name: String? = null
)