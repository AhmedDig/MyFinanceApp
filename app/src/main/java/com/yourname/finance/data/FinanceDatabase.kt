package com.yourname.finance.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Transaction::class, Category::class, IncomeStream::class, BufferLedger::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun transactionDao(): com.yourname.finance.data.dao.TransactionDao

    abstract fun categoryDao(): com.yourname.finance.data.dao.CategoryDao

    abstract fun incomeStreamDao(): com.yourname.finance.data.dao.IncomeStreamDao

    abstract fun bufferLedgerDao(): com.yourname.finance.data.dao.BufferLedgerDao
}
