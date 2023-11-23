package com.rtb.rtb.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rtb.rtb.networks.dto.request.RequirementRequest
import com.rtb.rtb.networks.dto.response.RequirementResponse
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp
import java.util.Date
import java.util.UUID

@Entity
@Parcelize
data class Requirement (
    @PrimaryKey
    val id: UUID,
    val code: Int,
    val typeId: UUID,
    val originId: UUID,
    val priorityId: UUID,
    val title: String,
    val userStory: String,
    val description: String,
    val projectId: UUID,
    val createdAt: Date,
    val updatedAt: Date?,
): Parcelable


fun fromResponse(response: RequirementResponse): Requirement {
    return Requirement(
        response.id!!,
        response.code!!,
        response.typeId!!,
        response.originId!!,
        response.priorityId!!,
        response.title!!,
        response.userStory!!,
        response.description!!,
        response.projectId!!,
        response.createdAt!!,
        response.updatedAt
    )
}

fun Requirement.toRequest(): RequirementRequest {
    return RequirementRequest(
        this.description,
        this.originId,
        this.priorityId,
        this.projectId,
        this.title,
        this.typeId,
        this.userStory
    )
}