package com.yourname.finance

import android.app.Application
import androidx.room.Room
import com.yourname.finance.data.*
import com.yourname.finance.data.dao.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FinanceApplication : Application() {
    lateinit var db: FinanceDatabase
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            FinanceDatabase::class.java,
            "finance.db"
        ).fallbackToDestructiveMigration().build()

        seedDataIfNeeded()
    }

    private fun seedDataIfNeeded() = scope.launch {
        val catDao = db.categoryDao()
        if (catDao.getAll().first().isEmpty()) {
            listOf(
                Category(name = "Electricity", group = "A", priority = 1, fixedVariable = "Fixed"),
                Category(name = "Internet", group = "A", priority = 1, fixedVariable = "Fixed"),
                Category(name = "Groceries", group = "A", priority = 1),
                Category(name = "Bread", group = "A", priority = 1),
                Category(name = "Transportation", group = "A", priority = 1),
                Category(name = "Cleaning supplies", group = "A", priority = 1),
                Category(name = "Doctor appointments", group = "A", priority = 1),
                Category(name = "Supplements", group = "A", priority = 1),
                Category(name = "Medicines", group = "A", priority = 1),
                Category(name = "Baby costs", group = "A", priority = 1),
                Category(name = "Wife allowance", group = "A", priority = 1),
                Category(name = "Barber", group = "A", priority = 2),
                Category(name = "Cigarettes", group = "B", priority = 3),
                Category(name = "Snacks", group = "B", priority = 3),
                Category(name = "Clothing", group = "B", priority = 3),
                Category(name = "Outings", group = "B", priority = 3),
                Category(name = "Phones", group = "C", priority = 2),
                Category(name = "Gas", group = "C", priority = 2),
                Category(name = "Eid", group = "C", priority = 3),
                Category(name = "Repairs", group = "C", priority = 1),
                Category(name = "Emergencies", group = "C", priority = 1),
                Category(name = "Pregnancy spikes", group = "C", priority = 1)
            ).forEach { catDao.insert(it) }
        }

        val incomeDao = db.incomeStreamDao()
        if (incomeDao.getAll().first().isEmpty()) {
            listOf(
                IncomeStream(source = "Salary", type = "Primary", stability = "Delayed", avgMonthly = 1860.0),
                IncomeStream(source = "Academy Income", type = "Secondary", stability = "Semi-stable", avgMonthly = 1300.0),
                IncomeStream(source = "Agency Income", type = "Secondary", stability = "Inactive", avgMonthly = 0.0)
            ).forEach { incomeDao.insert(it) }
        }

        val bufferDao = db.bufferLedgerDao()
        if (bufferDao.getByMonth("2026-05") == null) {
            bufferDao.insert(BufferLedger(month = "2026-05", startingBuffer = 0.0, incomeSurplus = 0.0, endingBuffer = 0.0))
        }
    }
}