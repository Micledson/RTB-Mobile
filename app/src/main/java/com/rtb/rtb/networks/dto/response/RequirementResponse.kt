package com.rtb.rtb.networks.dto.response

import com.google.gson.annotations.SerializedName
import java.sql.Time
import java.sql.Timestamp
import java.util.UUID

data class RequirementResponse(
    var code: Int? = null,
    var description: String? = null,
    var id: UUID? = null,
    @SerializedName("origin_id")
    var originId: UUID? = null,
    @SerializedName("priority_id")
    var priorityId: UUID? = null,
    @SerializedName("project_id")
    var projectId: UUID? = null,
    var title: String? = null,
    @SerializedName("type_id")
    var typeId: UUID? = null,
    @SerializedName("created_at")
    var createdAt: Timestamp? = null,
    @SerializedName("updated_at")
    var updatedAt: Timestamp? = null,
    @SerializedName("user_story")
    var userStory: String? = null
)