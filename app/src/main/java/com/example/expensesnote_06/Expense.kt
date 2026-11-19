package com.example.expensesnote_06

data class Expense(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val amount: Double,
    val date: String,
)
