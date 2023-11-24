package com.rtb.rtb.networks.dto.response

import com.rtb.rtb.model.Origin
import com.rtb.rtb.model.Priority
import com.rtb.rtb.model.Type

class ResourceResponse {
    var origins: List<OriginResponse>? = null
    var priorities: List<PriorityResponse>? = null
    var types: List<TypeResponse>? = null
}