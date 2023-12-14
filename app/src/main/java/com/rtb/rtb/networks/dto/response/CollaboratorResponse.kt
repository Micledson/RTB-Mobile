package com.rtb.rtb.networks.dto.response

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.util.UUID

data class CollaboratorResponse(
    @SerializedName("user_id")
    var userId: UUID? = null,
    @SerializedName("user_email")
    var userEmail: String,
    @SerializedName("user_first_name")
    var userFirstName: String? = null,
    @SerializedName("user_last_name")
    var userLastName: String? = null,
    @SerializedName("created_at")
    var createdAt: Timestamp? = null,
    @SerializedName("updated_at")
    var updatedAt: Timestamp? = null
)