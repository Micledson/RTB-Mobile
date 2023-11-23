package com.rtb.rtb.model

import com.rtb.rtb.networks.dto.response.OriginResponse
import java.sql.Timestamp
import java.util.UUID

data class Origin(
    val id: UUID,
    val description: String,
    val name: String,
    val createdAt: Timestamp,
    val updatedAt: Timestamp
)

fun fromResponse(response: OriginResponse): Origin {
    return Origin(
        response.id!!,
        response.description!!,
        response.name!!,
        response.createdAt!!,
        response.updatedAt!!
    )
}