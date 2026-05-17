package com.yourname.finance.ui.transactions

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yourname.finance.R
import com.yourname.finance.data.Transaction
import com.yourname.finance.databinding.BottomSheetAddTransactionBinding
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionBottomSheet(
    private val existingTransaction: Transaction? = null,
    private val onSave: (Transaction) -> Unit,
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddTransactionBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pre-fill fields when editing
        existingTransaction?.let { tx ->
            binding.toggleType.check(if (tx.type == "Income") R.id.btn_income else R.id.btn_expense)
            binding.etAmount.setText(tx.amount.toString())
            binding.etDate.setText(
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(tx.date)
            )
            binding.etNotes.setText(tx.notes ?: "")
            // Category and subcategory will be set after adapters are ready
            updateSubcategorySpinner() // initial sub list

            // Set category selection after adapter is set (Spinner already has entries from XML)
            val catAdapter = binding.spinnerCategory.adapter as? ArrayAdapter<String>
            val catPos = catAdapter?.getPosition(tx.categoryId) ?: 0
            binding.spinnerCategory.setSelection(if (catPos >= 0) catPos else 0)

            // Subcategory will be updated once category is set – we'll set it after a short delay
            binding.spinnerCategory.post {
                binding.spinnerSubcategory.setSelection(
                    (binding.spinnerSubcategory.adapter as? ArrayAdapter<String>)?.getPosition(
                        tx.subcategory ?: ""
                    ) ?: 0
                )
            }
        }

        // Set up subcategory spinner based on initial selection
        updateSubcategorySpinner()

        // Listen for category changes
        binding.spinnerCategory.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    updateSubcategorySpinner()
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            }

        // When Income is selected, force category to "Income" and disable spinner
        binding.toggleType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.btn_income) {
                binding.spinnerCategory.setSelection(1) // index of "Income"
                binding.spinnerCategory.isEnabled = false
            } else {
                binding.spinnerCategory.isEnabled = true
                if (binding.spinnerCategory.selectedItem.toString() == "Income") {
                    binding.spinnerCategory.setSelection(0) // reset
                }
            }
            updateSubcategorySpinner()
        }

        // Date picker
        binding.etDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                    requireContext(),
                    { _, year, month, dayOfMonth ->
                        binding.etDate.setText(
                            String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                        )
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH),
                )
                .show()
        }

        // Save button
        binding.btnSave.setOnClickListener {
            val type =
                if (binding.toggleType.checkedRadioButtonId == R.id.btn_income) "Income"
                else "Expense"
            val amount = binding.etAmount.text.toString().toDoubleOrNull()
            val category = binding.spinnerCategory.selectedItem.toString().trim()
            val subcategory = binding.spinnerSubcategory.selectedItem.toString().trim()
            val dateStr = binding.etDate.text.toString()
            val notes = binding.etNotes.text.toString()

            if (amount == null || category == "Select category" || dateStr.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(dateStr) ?: Date()

            val transaction =
                if (existingTransaction != null) {
                    existingTransaction.copy(
                        date = date,
                        type = type,
                        categoryId = category,
                        subcategory = subcategory.ifEmpty { null },
                        amount = amount,
                        notes = notes,
                    )
                } else {
                    Transaction(
                        date = date,
                        type = type,
                        categoryId = category,
                        subcategory = subcategory.ifEmpty { null },
                        amount = amount,
                        notes = notes,
                    )
                }
            onSave(transaction)
            dismiss()
        }
    }

    private fun updateSubcategorySpinner() {
        val category = binding.spinnerCategory.selectedItem?.toString() ?: return
        val isIncome = binding.toggleType.checkedRadioButtonId == R.id.btn_income

        val subArrayResId =
            if (isIncome || category == "Income") {
                R.array.sub_income
            } else {
                when (category) {
                    "Food" -> R.array.sub_food
                    "Bills" -> R.array.sub_bills
                    "Household" -> R.array.sub_household
                    "Transport" -> R.array.sub_transport
                    "Medical" -> R.array.sub_medical
                    "Children" -> R.array.sub_children
                    "Personal" -> R.array.sub_personal
                    "Irregular" -> R.array.sub_irregular
                    else -> null
                }
            }

        if (subArrayResId != null) {
            val adapter =
                ArrayAdapter.createFromResource(
                    requireContext(),
                    subArrayResId,
                    android.R.layout.simple_spinner_item,
                )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSubcategory.adapter = adapter
        } else {
            binding.spinnerSubcategory.adapter = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
