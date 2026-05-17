package com.yourname.finance.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "income_streams")
data class IncomeStream(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val source: String,
    val type: String? = null,
    val stability: String? = null,
    val avgMonthly: Double? = null,
    val paymentCycle: String? = null,
    val isActive: Boolean = true,
    val notes: String? = null,
    val lastModified: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)