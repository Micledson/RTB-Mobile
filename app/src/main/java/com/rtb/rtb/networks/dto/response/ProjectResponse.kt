package com.rtb.rtb.networks.dto.response
import com.google.gson.annotations.SerializedName

import java.sql.Timestamp
import java.util.UUID

class ProjectResponse {
    var id: UUID? = null
    var name: String? = null
    var alias: String? = null
    var description: String? = null
    @SerializedName("is_active")
    var isActive: Boolean? = null
    @SerializedName("created_at")
    var createdAt: Timestamp? = null
    @SerializedName("updated_at")
    var updatedAt: Timestamp? = null
    @SerializedName("deleted_at")
    var deletedAt: Timestamp? = null
    @SerializedName("created_by_user_email")
    var createdByUserEmail: String? = null
}
