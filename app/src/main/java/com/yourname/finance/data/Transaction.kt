package com.yourname.finance.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val date: Date,
    val type: String, // "Income" or "Expense"
    val account: String? = null,
    val categoryId: String,
    val subcategory: String? = null,
    val amount: Double,
    val paymentMethod: String? = null,
    val notes: String? = null,
    val isRecurring: Boolean = false,
    val lastModified: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)