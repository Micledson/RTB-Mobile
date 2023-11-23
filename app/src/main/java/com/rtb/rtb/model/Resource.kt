package com.rtb.rtb.model

import com.rtb.rtb.networks.dto.response.ResourceResponse

data class Resource(
    val origins: List<Origin>, val priorities: List<Priority>, val types: List<Type>
)

fun fromResponse(response: ResourceResponse): Resource {
    val origins = ArrayList<Origin>()

    response.origins?.forEach { origin ->
        origins.add(fromResponse(origin))
    }

    val types = ArrayList<Type>()

    response.types?.forEach { type ->
        types.add(fromResponse(type))
    }

    val priorities = ArrayList<Priority>()

    response.priorities?.forEach { priority ->
        priorities.add(fromResponse(priority))
    }
    return Resource(
        origins, priorities, types
    )
}