package com.rtb.rtb.networks.dto.request

data class ProjectRequest (
    var alias: String? = null,
    var description: String? = null,
    var isActive: Boolean? = null,
    var name: String? = null
)