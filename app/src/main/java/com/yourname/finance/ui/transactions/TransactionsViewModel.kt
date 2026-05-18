package com.yourname.finance.ui.transactions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yourname.finance.FinanceApplication
import com.yourname.finance.data.Transaction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = (application as FinanceApplication).db

    private val _currentMonth = MutableStateFlow(getCurrentMonth())
    val currentMonth: StateFlow<String> = _currentMonth.asStateFlow()

    val transactions: StateFlow<List<Transaction>> = _currentMonth.flatMapLatest { month ->
        db.transactionDao().getByMonth(month)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun previousMonth() { _currentMonth.value = adjustMonth(_currentMonth.value, -1) }
    fun nextMonth() { _currentMonth.value = adjustMonth(_currentMonth.value, 1) }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            db.transactionDao().insert(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            db.transactionDao().update(transaction)
        }
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