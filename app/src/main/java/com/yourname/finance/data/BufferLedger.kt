package com.yourname.finance.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buffer_ledger")
data class BufferLedger(
    @PrimaryKey val month: String, // "YYYY-MM"
    val startingBuffer: Double = 0.0,
    val incomeSurplus: Double = 0.0,
    val endingBuffer: Double = 0.0
)