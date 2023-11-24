package com.rtb.rtb.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.rtb.rtb.networks.dto.request.ProjectRequest
import com.rtb.rtb.networks.dto.response.ProjectResponse
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("email"),
        childColumns = arrayOf("owner"),
        onDelete = ForeignKey.CASCADE
    )]
)
@Parcelize
data class Project(
    var id: UUID? = null,
    val name: String,
    val alias: String,
    val description: String,
    val isActive: Boolean,
    @PrimaryKey
    val createdAt: Date,
    val updatedAt: Date,
    val deletedAt: Date?,
    val owner: String?
) : Parcelable


fun fromResponse(response: ProjectResponse): Project {
    return Project(
        response.id!!,
        response.name!!,
        response.alias!!,
        response.description!!,
        response.isActive!!,
        response.createdAt!!,
        response.updatedAt!!,
        response.deletedAt,
        null
    )
}

fun Project.toRequest(): ProjectRequest {
    return ProjectRequest(
        this.alias,
        this.description,
        this.isActive,
        this.name,
    )
}
