package com.yourname.finance.data.dao

import androidx.room.*
import com.yourname.finance.data.IncomeStream
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeStreamDao {
    @Query("SELECT * FROM income_streams WHERE isActive = 1 ORDER BY source")
    fun getActiveStreams(): Flow<List<IncomeStream>>

    @Query("SELECT * FROM income_streams ORDER BY source")
    fun getAll(): Flow<List<IncomeStream>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stream: IncomeStream)

    @Update
    suspend fun update(stream: IncomeStream)

    @Delete
    suspend fun delete(stream: IncomeStream)
}