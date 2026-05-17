package com.yourname.finance.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val group: String, // "A", "B", "C"
    val subcategory: String? = null,
    val priority: Int = 1,
    val fixedVariable: String = "Variable", // "Fixed" or "Variable"
    val weight: String = "Medium",
    val isActive: Boolean = true,
    val notes: String? = null,
    val lastModified: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)