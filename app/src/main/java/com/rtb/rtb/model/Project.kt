package com.rtb.rtb.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Entity
@Parcelize
data class Project (
    @PrimaryKey
    val id: UUID,
    val name: String,
    val alias: String,
    val description: String,
    val isActive: Boolean,
    val createdAt: Date,
    val updatedAt: Date?,
    val deletedAt: Date?
): Parcelable