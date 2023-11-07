package com.rtb.rtb.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Entity
@Parcelize
data class Requirement (
    @PrimaryKey
    val id: UUID,
    val type: String,
    val title: String,
    val description: String,
    val userStory: String,
    val priority: String,
    val projectId: UUID,
    val isActive: Boolean,
    val createdAt: Date,
    val updatedAt: Date?,
    val deletedAt: Date?,
): Parcelable