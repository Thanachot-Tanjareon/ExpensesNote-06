package com.example.expensesnote_06

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var etAmount: EditText
    private lateinit var etNote: EditText
    private lateinit var btnAdd: Button
    private lateinit var tvTotal: TextView
    private lateinit var tvExpenseCount: TextView
    private lateinit var tvEmptyState: TextView
    private lateinit var rvExpenses: RecyclerView

    private val expenseList = mutableListOf<Expense>()
    private lateinit var expenseAdapter: ExpenseAdapter
    private var total: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etAmount = findViewById(R.id.etAmount)
        etNote = findViewById(R.id.etTitle)
        btnAdd = findViewById(R.id.btnAddExpense)
        tvTotal = findViewById(R.id.tvTotalAmount)
        tvExpenseCount = findViewById(R.id.tvExpenseCount)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        rvExpenses = findViewById(R.id.rvExpenses)

        expenseAdapter = ExpenseAdapter(expenseList) { expense ->
            deleteExpense(expense)
        }
        rvExpenses.layoutManager = LinearLayoutManager(this)
        rvExpenses.adapter = expenseAdapter

        updateUI()

        btnAdd.setOnClickListener {
            val amountText = etAmount.text.toString().trim()
            val noteText = etNote.text.toString().trim()

            if (amountText.isEmpty()) {
                Toast.makeText(this, "Please enter an amount of money.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if ((amount == null) || (amount <= 0)) {
                Toast.makeText(this, "Enter a valid positive number in Amount field.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dateFormat = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
            val currentDate = dateFormat.format(Date())

            val title = noteText.ifEmpty { "Expense" }
            val newExpense = Expense(
                title = title,
                amount = amount,
                date = currentDate,
            )

            expenseList.add(0, newExpense)
            expenseAdapter.notifyItemInserted(0)
            rvExpenses.scrollToPosition(0)

            total += amount
            etAmount.text.clear()
            etNote.text.clear()

            updateUI()
        }
    }

    private fun deleteExpense(expense: Expense) {
        val position = expenseList.indexOf(expense)
        if (position != -1) {
            expenseList.removeAt(position)
            expenseAdapter.notifyItemRemoved(position)
            total -= expense.amount
            if (total < 0) total = 0.0
            updateUI()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        tvTotal.text = "$%.2f".format(total)
        tvExpenseCount.text = getString(R.string.entries_count, expenseList.size)
        if (expenseList.isEmpty()) {
            tvEmptyState.visibility = View.VISIBLE
            rvExpenses.visibility = View.GONE
        } else {
            tvEmptyState.visibility = View.GONE
            rvExpenses.visibility = View.VISIBLE
        }
    }
}
