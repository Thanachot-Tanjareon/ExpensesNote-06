package com.example.expensesnote_06

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(
    private val expenses: List<Expense>,
    private val onDeleteClick: (Expense) -> Unit,
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvExpenseTitle)
        val tvDate: TextView = itemView.findViewById(R.id.tvExpenseDate)
        val tvAmount: TextView = itemView.findViewById(R.id.tvExpenseAmount)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteExpense)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.tvTitle.text = expense.title
        holder.tvDate.text = expense.date
        holder.tvAmount.text = holder.itemView.context.getString(R.string.formatted_amount, expense.amount)

        holder.btnDelete.setOnClickListener {
            onDeleteClick(expense)
        }
    }

    override fun getItemCount(): Int = expenses.size
}
