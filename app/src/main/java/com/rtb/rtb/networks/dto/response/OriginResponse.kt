package com.rtb.rtb.networks.dto.response

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.util.UUID

class OriginResponse {
    var id: UUID? = null
    var description: String? = null
    var name: String? = null
    @SerializedName("created_at")
    var createdAt: Timestamp? = null
    @SerializedName("updated_at")
    var updatedAt: Timestamp? = null
}