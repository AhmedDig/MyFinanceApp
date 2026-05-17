package com.yourname.finance.data.dao

import androidx.room.*
import com.yourname.finance.data.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC") fun getAll(): Flow<List<Transaction>>

    @Query(
        "SELECT * FROM transactions WHERE strftime('%Y-%m', date/1000, 'unixepoch') = :month ORDER BY date DESC"
    )
    fun getByMonth(month: String): Flow<List<Transaction>>

    @Update suspend fun update(transaction: Transaction)

    @Query("DELETE FROM transactions WHERE date BETWEEN :startDate AND :endDate")
    suspend fun deleteByDateRange(startDate: Date, endDate: Date)

    @Query("DELETE FROM transactions") suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(transaction: Transaction)

    @Delete suspend fun delete(transaction: Transaction)
}
