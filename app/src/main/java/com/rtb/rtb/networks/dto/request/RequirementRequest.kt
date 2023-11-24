package com.rtb.rtb.networks.dto.request

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class RequirementRequest(
    var description: String,
    @SerializedName("origin_id")
    var originId: UUID,
    @SerializedName("priority_id")
    var priorityId: UUID,
    @SerializedName("project_id")
    var projectId: UUID,
    var title: String,
    @SerializedName("type_id")
    var typeId: UUID,
    @SerializedName("user_story")
    var userStory: String
)