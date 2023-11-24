package com.rtb.rtb.model

import com.rtb.rtb.networks.dto.response.OriginResponse
import com.rtb.rtb.networks.dto.response.TypeResponse
import java.sql.Timestamp
import java.util.UUID

data class Type(
    val id: UUID,
    val description: String,
    val name: String,
    val createdAt: Timestamp,
    val updatedAt: Timestamp
)


fun fromResponse(response: TypeResponse): Type {
    return Type(
        response.id!!,
        response.description!!,
        response.name!!,
        response.createdAt!!,
        response.updatedAt!!
    )
}