package com.yourname.finance.ui.setup

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yourname.finance.FinanceApplication
import com.yourname.finance.data.Transaction
import com.yourname.finance.databinding.FragmentSetupBinding
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SetupFragment : Fragment() {

    private var _binding: FragmentSetupBinding? = null
    private val binding
        get() = _binding!!

    private var startDate: Date? = null
    private var endDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etStartDate.setOnClickListener { showDatePicker(true) }
        binding.etEndDate.setOnClickListener { showDatePicker(false) }

        binding.btnDownloadReport.setOnClickListener { downloadReport() }
        binding.btnDeleteRange.setOnClickListener { confirmDeleteRange() }
    }

    private fun showDatePicker(isStart: Boolean) {
        val cal = Calendar.getInstance()
        DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val date =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .parse("$year-${month + 1}-$dayOfMonth") ?: Date()
                    if (isStart) {
                        startDate = date
                        binding.etStartDate.setText(
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
                        )
                    } else {
                        endDate = date
                        binding.etEndDate.setText(
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
                        )
                    }
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
            )
            .show()
    }

    private fun confirmDeleteRange() {
        if (startDate == null || endDate == null) {
            Toast.makeText(requireContext(), "Please select both dates", Toast.LENGTH_SHORT).show()
            return
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Transactions")
            .setMessage(
                "Delete all transactions between ${binding.etStartDate.text} and ${binding.etEndDate.text}?"
            )
            .setPositiveButton("Delete") { _, _ -> deleteRange() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteRange() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val db = (requireActivity().application as FinanceApplication).db
                db.transactionDao().deleteByDateRange(startDate!!, endDate!!)
            }
            withContext(Dispatchers.Main) {
                binding.tvStatus.text =
                    "Transactions deleted from ${binding.etStartDate.text} to ${binding.etEndDate.text}"
                binding.tvStatus.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun downloadReport() {
        if (startDate == null || endDate == null) {
            Toast.makeText(requireContext(), "Please select both dates", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            val transactions =
                withContext(Dispatchers.IO) {
                    val db = (requireActivity().application as FinanceApplication).db
                    db.transactionDao().getAll().first().filter {
                        it.date in startDate!!..endDate!!
                    }
                }
            generatePdfStatement(transactions, startDate!!, endDate!!)
        }
    }

    private fun generatePdfStatement(transactions: List<Transaction>, from: Date, to: Date) {
        val file = File(requireContext().cacheDir, "financial_statement.pdf")
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val paint =
            Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 12f
                typeface = Typeface.DEFAULT
            }
        val boldPaint =
            Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 12f
                typeface = Typeface.DEFAULT_BOLD
            }
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val nf =
            NumberFormat.getNumberInstance().apply {
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }

        var y = 40f
        val lineHeight = 20f
        val leftMargin = 40f
        val colWidths = arrayOf(80f, 60f, 80f, 100f, 80f)

        // Title
        canvas.drawText("Financial Statement", leftMargin, y, boldPaint)
        y += lineHeight * 2
        canvas.drawText(
            "Period: ${dateFormat.format(from)} – ${dateFormat.format(to)}",
            leftMargin,
            y,
            paint,
        )
        y += lineHeight * 2

        // Table header
        val headers = arrayOf("Date", "Type", "Category", "Subcategory", "Amount")
        var x = leftMargin
        for (h in headers) {
            canvas.drawText(h, x, y, boldPaint)
            x += colWidths[headers.indexOf(h)]
        }
        y += lineHeight

        // Rows
        for (tx in transactions) {
            x = leftMargin
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(tx.date)
            val type = tx.type
            val category = tx.categoryId
            val sub = tx.subcategory ?: ""
            val amount = "₪${nf.format(tx.amount)}"
            val row = listOf(date, type, category, sub, amount)
            for (i in row.indices) {
                canvas.drawText(row[i], x, y, paint)
                x += colWidths[i]
            }
            y += lineHeight
        }

        // Summary
        y += lineHeight
        val totalIncome = transactions.filter { it.type == "Income" }.sumOf { it.amount }
        val totalExpense = transactions.filter { it.type == "Expense" }.sumOf { it.amount }
        val net = totalIncome - totalExpense

        canvas.drawText("Total Income:", leftMargin, y, boldPaint)
        canvas.drawText(
            "₪${nf.format(totalIncome)}",
            leftMargin + colWidths.take(4).sum(),
            y,
            boldPaint,
        )
        y += lineHeight
        canvas.drawText("Total Expenses:", leftMargin, y, boldPaint)
        canvas.drawText(
            "₪${nf.format(totalExpense)}",
            leftMargin + colWidths.take(4).sum(),
            y,
            boldPaint,
        )
        y += lineHeight
        canvas.drawText("Net Result:", leftMargin, y, boldPaint)
        canvas.drawText("₪${nf.format(net)}", leftMargin + colWidths.take(4).sum(), y, boldPaint)

        pdfDocument.finishPage(page)
        // Fix FileOutputStream ambiguity by specifying type
        val fos: FileOutputStream = FileOutputStream(file)
        pdfDocument.writeTo(fos)
        fos.close()
        pdfDocument.close()

        val uri =
            FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                file,
            )
        val shareIntent =
            Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        startActivity(Intent.createChooser(shareIntent, "Download Statement"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
