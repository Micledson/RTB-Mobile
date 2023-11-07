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
    val code: String,
    val title: String,
    val description: String,
    val userStory: String,
    val type: String,
    val origin: String,
    val priority: String,
    val projectID: UUID,
    val createdAt: Date,
    val updatedAt: Date?
): Parcelable