package com.yourname.finance.ui.transactions

import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yourname.finance.data.Transaction
import com.yourname.finance.databinding.FragmentTransactionsBinding
import java.io.File
import java.io.FileOutputStream
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

        adapter =
            TransactionAdapter(
                onReceiptClick = { generateReceipt(it) },
                onItemClick = { showTransactionDetails(it) },
            )
        binding.rvTransactions.adapter = adapter
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
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
        val pdfDir = File(context.cacheDir, "receipts")
        if (!pdfDir.exists()) pdfDir.mkdirs()
        val file = File(pdfDir, "receipt_${transaction.id}.pdf")

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paint =
            android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 14f
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }
        var y = 40f
        val lineHeight = 24f
        fun drawLine(label: String, value: String) {
            canvas.drawText("$label: $value", 40f, y, paint)
            y += lineHeight
        }

        paint.typeface = android.graphics.Typeface.DEFAULT_BOLD
        canvas.drawText("Transaction Receipt", 40f, y, paint)
        y += lineHeight * 2
        paint.typeface = android.graphics.Typeface.DEFAULT
        drawLine(
            "Date",
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(transaction.date),
        )
        drawLine("Type", transaction.type)
        drawLine("Category", transaction.categoryId)
        drawLine("Subcategory", transaction.subcategory ?: "—")
        drawLine("Amount", "₪${transaction.amount}")
        drawLine("Notes", transaction.notes ?: "—")

        pdfDocument.finishPage(page)
        try {
            FileOutputStream(file).use { out -> pdfDocument.writeTo(out) }
        } finally {
            pdfDocument.close()
        }

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val shareIntent =
            Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        startActivity(Intent.createChooser(shareIntent, "Share Receipt"))
    }

    private fun showTransactionDetails(transaction: Transaction) {
        val type = if (transaction.type == "Income") "Income" else "Expense"
        val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(transaction.date)
        val message =
            """
        Type: $type
        Category: ${transaction.categoryId}
        Subcategory: ${transaction.subcategory ?: "—"}
        Amount: ₪${transaction.amount}
        Date: $date
        Notes: ${transaction.notes ?: "—"}
    """
                .trimIndent()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Transaction Details")
            .setMessage(message)
            .setPositiveButton("Edit") { _, _ ->
                AddTransactionBottomSheet(transaction) { updated ->
                        viewModel.updateTransaction(updated)
                    }
                    .show(parentFragmentManager, "EditTransaction")
            }
            .setNeutralButton("Close") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
