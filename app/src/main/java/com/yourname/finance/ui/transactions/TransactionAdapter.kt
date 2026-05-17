package com.yourname.finance.ui.transactions

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

class TransactionAdapter(private val onReceiptClick: (Transaction) -> Unit) :
    ListAdapter<Transaction, TransactionAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onReceiptClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemTransactionBinding,
        private val onReceiptClick: (Transaction) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            val context = binding.root.context
            // Category dot color (use green for income, red for expense for now)
            if (transaction.type == "Income") {
                binding.vCategoryDot.background.setTint(
                    context.getColor(com.yourname.finance.R.color.incomeGreen)
                )
            } else {
                binding.vCategoryDot.background.setTint(
                    context.getColor(com.yourname.finance.R.color.expenseRed)
                )
            }

            binding.tvCategoryName.text = transaction.categoryId // TODO: resolve category name
            binding.tvDateSubcategory.text =
                "${SimpleDateFormat("dd MMM", Locale.getDefault()).format(transaction.date)} · ${transaction.subcategory ?: ""}"
            val formattedAmount = formatCurrency(transaction.amount)
            binding.tvAmount.text =
                if (transaction.type == "Income") "+$formattedAmount" else "-$formattedAmount"
            binding.tvAmount.setTextColor(
                if (transaction.type == "Income")
                    context.getColor(com.yourname.finance.R.color.incomeGreen)
                else context.getColor(com.yourname.finance.R.color.expenseRed)
            )

            binding.btnReceipt.setOnClickListener { onReceiptClick(transaction) }
        }

        private fun formatCurrency(amount: Double): String {
            val format = NumberFormat.getCurrencyInstance()
            format.currency = Currency.getInstance("ILS")
            return format.format(amount)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction) =
            oldItem == newItem
    }
}
