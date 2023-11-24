package com.rtb.rtb.model

import com.rtb.rtb.networks.dto.response.PriorityResponse
import java.sql.Timestamp
import java.util.UUID

data class Priority (
    val id: UUID,
    val level: String,
    val description: String,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,

)

fun fromResponse(response: PriorityResponse): Priority {
    return Priority(
        response.id!!,
        response.level!!,
        response.description!!,
        response.createdAt!!,
        response.updatedAt!!
    )
}