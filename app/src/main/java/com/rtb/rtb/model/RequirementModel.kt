package com.rtb.rtb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Parcelize
data class RequirementModel (
    val id: UUID,
    val code: String,
    val title: String,
    val description: String,
    val userStory: String,
    val type: String,
    val origin: String,
    val priority: String,
    val project: String,
    val createdAt: Date,
    val updatedAt: Date?
): Parcelable
