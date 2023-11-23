package com.rtb.rtb.networks.dto.response

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.util.UUID

class PriorityResponse {
    var id: UUID? = null
    var level: String? = null
    var description: String? = null
    @SerializedName("created_at")
    var createdAt: Timestamp? = null
    @SerializedName("updated_at")
    var updatedAt: Timestamp? = null
}