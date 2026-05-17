package com.yourname.finance.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yourname.finance.FinanceApplication
import com.yourname.finance.data.BufferLedger
import com.yourname.finance.data.Transaction
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

data class DashboardData(
    val month: String,
    val income: Double,
    val expenses: Double,
    val netResult: Double,
    val buffer: Double,
    val runwayDays: String,
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val db = (application as FinanceApplication).db

    private val _currentMonth = MutableStateFlow(getCurrentMonth())
    val currentMonth: StateFlow<String> = _currentMonth.asStateFlow()

    val dashboardData: StateFlow<DashboardData> =
        _currentMonth
            .flatMapLatest { month ->
                combine(
                    db.transactionDao().getByMonth(month).map { list ->
                        list.filter { it.type == "Income" }.sumOf { it.amount } to
                            list.filter { it.type == "Expense" }.sumOf { it.amount }
                    },
                    flow {
                        val ledger = db.bufferLedgerDao().getByMonth(month)
                        emit(ledger?.endingBuffer ?: 0.0)
                    },
                ) { (income, expenses), buffer ->
                    val net = income - expenses
                    val runway =
                        if (expenses == 0.0) "∞" else "${(buffer / expenses * 30).toInt()} days"
                    DashboardData(month, income, expenses, net, buffer, runway)
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                DashboardData(getCurrentMonth(), 0.0, 0.0, 0.0, 0.0, "∞"),
            )

    // Add recent transactions
    val recentTransactions: StateFlow<List<Transaction>> =
        _currentMonth
            .flatMapLatest { month -> db.transactionDao().getByMonth(month).map { it.take(5) } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun previousMonth() {
        _currentMonth.value = adjustMonth(_currentMonth.value, -1)
    }

    fun nextMonth() {
        _currentMonth.value = adjustMonth(_currentMonth.value, 1)
    }

    private fun adjustMonth(month: String, delta: Int): String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val date = sdf.parse(month)!!
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MONTH, delta)
        return sdf.format(cal.time)
    }

    private fun getCurrentMonth(): String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        return sdf.format(Date())
    }
}
