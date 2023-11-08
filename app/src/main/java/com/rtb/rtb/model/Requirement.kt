package com.rtb.rtb.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Entity
@Parcelize
data class Requirement (
    @PrimaryKey
    val id: UUID,
    val code: Int,
    val type: String,
    val origin: String,
    val priority: String,
    val title: String,
    val userStory: String,
    val notes: String,
    val projectId: UUID,
    val createdAt: Date,
    val updatedAt: Date?,
    val deletedAt: Date?
): Parcelable