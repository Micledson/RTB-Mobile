package com.rtb.rtb.networks.dto.response

import java.sql.Timestamp
import java.util.UUID

class ProjectResponse {
    var id: UUID? = null
    var name: String? = null
    var alias: String? = null
    var description: String? = null
    var isActive: Boolean? = null
    var createdAt: Timestamp? = null
    var updatedAt: Timestamp? = null
    var deletedAt: Timestamp? = null
}
