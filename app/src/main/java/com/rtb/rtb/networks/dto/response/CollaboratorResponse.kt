package com.rtb.rtb.networks.dto.response

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.util.UUID

data class CollaboratorResponse(
    @SerializedName("user_id")
    var userId: UUID? = null,
    var userEmail: String,
    var userFirstName: String? = null,
    var userLastName: String? = null,
    @SerializedName("created_at")
    var createdAt: Timestamp? = null,
    @SerializedName("updated_at")
    var updatedAt: Timestamp? = null
)