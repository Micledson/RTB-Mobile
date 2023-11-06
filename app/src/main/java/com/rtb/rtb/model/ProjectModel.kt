package com.rtb.rtb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Parcelize
data class ProjectModel (
    val id: UUID,
    val name: String,
    val alias: String,
    val description: String,
    val isActive: Boolean,
    val createdAt: Date,
    val updatedAt: Date?,
    val deletedAt: Date?
): Parcelable