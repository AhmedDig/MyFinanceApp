package com.yourname.finance.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourname.finance.databinding.FragmentDashboardBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var viewModel: DashboardViewModel
    private val recentAdapter = RecentTransactionAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        // Setup RecyclerView once
        binding.rvRecentTransactions.adapter = recentAdapter
        binding.rvRecentTransactions.layoutManager = LinearLayoutManager(requireContext())

        // Observe month directly from currentMonth flow
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentMonth.collectLatest { month ->
                val monthDisplay =
                    SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                        .format(SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse(month)!!)
                binding.tvMonthHeader.text = monthDisplay
            }
        }

        // Observe dashboard data (cards)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dashboardData.collectLatest { data ->
                binding.tvTotalIncome.text = formatCurrency(data.income)
                binding.tvTotalExpenses.text = formatCurrency(data.expenses)
                binding.tvNetResult.text = formatCurrency(data.netResult)
                binding.tvBuffer.text = formatCurrency(data.buffer)
                binding.tvRunway.text = data.runwayDays
            }
        }

        // Observe recent transactions separately
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recentTransactions.collectLatest { list -> recentAdapter.submitList(list) }
        }

        binding.btnPrevMonth.setOnClickListener { viewModel.previousMonth() }
        binding.btnNextMonth.setOnClickListener { viewModel.nextMonth() }
    }

    private fun formatCurrency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("ILS")
        return format.format(amount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
