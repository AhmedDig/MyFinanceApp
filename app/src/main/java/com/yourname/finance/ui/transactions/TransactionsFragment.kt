package com.yourname.finance.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.yourname.finance.data.Transaction
import com.yourname.finance.databinding.FragmentTransactionsBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TransactionsFragment : Fragment() {
    private var _binding: FragmentTransactionsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var viewModel: TransactionsViewModel
    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TransactionsViewModel::class.java]

        adapter = TransactionAdapter { transaction -> generateReceipt(transaction) }
        binding.rvTransactions.adapter = adapter

        lifecycleScope.launch {
            viewModel.transactions.collectLatest { list -> adapter.submitList(list) }
        }

        lifecycleScope.launch {
            viewModel.currentMonth.collectLatest { month ->
                val monthDisplay =
                    SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                        .format(SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse(month)!!)
                binding.tvMonthHeader.text = monthDisplay
            }
        }
        binding.btnPrevMonth.setOnClickListener { viewModel.previousMonth() }
        binding.btnNextMonth.setOnClickListener { viewModel.nextMonth() }

        binding.fabAddTransaction.setOnClickListener {
            AddTransactionBottomSheet { transaction -> viewModel.addTransaction(transaction) }
                .show(parentFragmentManager, "AddTransaction")
        }
    }

    private fun generateReceipt(transaction: Transaction) {
        val context = requireContext()
        val file = File(context.cacheDir, "receipt_${transaction.id}.pdf")
        // Placeholder: we will implement PDF generation later
        val shareText =
            "Transaction Receipt\nDate: ${transaction.date}\nCategory: ${transaction.categoryId}\nAmount: ₪${transaction.amount}\nType: ${transaction.type}"
        val sendIntent =
            android.content.Intent().apply {
                action = android.content.Intent.ACTION_SEND
                putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
        startActivity(android.content.Intent.createChooser(sendIntent, "Share receipt"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
