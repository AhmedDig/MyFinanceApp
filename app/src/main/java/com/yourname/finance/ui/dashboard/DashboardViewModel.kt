package com.yourname.finance.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yourname.finance.FinanceApplication
import com.yourname.finance.data.Transaction
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
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
                    // Flow 1: income & expenses for the selected month
                    db.transactionDao().getByMonth(month).map { list ->
                        val income = list.filter { it.type == "Income" }.sumOf { it.amount }
                        val expenses = list.filter { it.type == "Expense" }.sumOf { it.amount }
                        income to expenses
                    },
                    // Flow 2: all transactions -> compute accumulated net (buffer) up to selected
                    // month
                    db.transactionDao().getAll().map { allTransactions ->
                        var buffer = 0.0
                        for (tx in allTransactions) {
                            val txMonth =
                                SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(tx.date)
                            if (txMonth <= month) {
                                if (tx.type == "Income") buffer += tx.amount
                                else buffer -= tx.amount
                            }
                        }
                        buffer
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
