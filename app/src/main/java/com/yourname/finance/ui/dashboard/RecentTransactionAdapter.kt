package com.yourname.finance.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yourname.finance.data.Transaction
import com.yourname.finance.databinding.ItemTransactionBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class RecentTransactionAdapter :
    ListAdapter<Transaction, RecentTransactionAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            val ctx = binding.root.context
            // Dot color
            if (transaction.type == "Income") {
                binding.vCategoryDot.background.setTint(
                    ctx.getColor(com.yourname.finance.R.color.incomeGreen)
                )
            } else {
                binding.vCategoryDot.background.setTint(
                    ctx.getColor(com.yourname.finance.R.color.expenseRed)
                )
            }

            binding.tvCategoryName.text = transaction.categoryId
            binding.tvDateSubcategory.text =
                "${SimpleDateFormat("dd MMM", Locale.getDefault()).format(transaction.date)} · ${transaction.subcategory ?: ""}"
            val formatted =
                NumberFormat.getCurrencyInstance()
                    .apply { currency = Currency.getInstance("ILS") }
                    .format(transaction.amount)
            binding.tvAmount.text =
                if (transaction.type == "Income") "+$formatted" else "-$formatted"
            binding.tvAmount.setTextColor(
                if (transaction.type == "Income")
                    ctx.getColor(com.yourname.finance.R.color.incomeGreen)
                else ctx.getColor(com.yourname.finance.R.color.expenseRed)
            )

            // Hide receipt icon on dashboard (we can still allow tap to open transaction)
            binding.btnReceipt.visibility = android.view.View.GONE
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction) =
            oldItem == newItem
    }
}
