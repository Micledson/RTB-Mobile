package com.rtb.rtb.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rtb.rtb.networks.dto.request.CollaboratorRequest
import com.rtb.rtb.networks.dto.response.CollaboratorResponse
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Entity
@Parcelize
data class Collaborator (
    @PrimaryKey
    val userId: UUID,
    val userEmail: String,
    val userFirstName: String,
    val userLastName: String,
    val createdAt: Date,
    val updatedAt: Date?,
): Parcelable


fun fromResponse(response: CollaboratorResponse): Collaborator {
    return Collaborator(
        response.userId!!,
        response.userEmail,
        response.userFirstName!!,
        response.userLastName!!,
        response.createdAt!!,
        response.updatedAt
    )
}

fun Collaborator.toRequest(): CollaboratorRequest {
    return CollaboratorRequest(
        this.userEmail
    )
}