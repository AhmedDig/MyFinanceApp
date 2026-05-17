package com.yourname.finance.data.dao

import androidx.room.*
import com.yourname.finance.data.BufferLedger
import kotlinx.coroutines.flow.Flow

@Dao
interface BufferLedgerDao {
    @Query("SELECT * FROM buffer_ledger WHERE month = :month")
    suspend fun getByMonth(month: String): BufferLedger?

    @Query("SELECT * FROM buffer_ledger ORDER BY month DESC")
    fun getAll(): Flow<List<BufferLedger>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ledger: BufferLedger)

    @Update
    suspend fun update(ledger: BufferLedger)
}