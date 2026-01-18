package com.example.monthlycosttracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TransactionViewModel(private val transactionDao: TransactionDao) : ViewModel() {

    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()

    fun insert(transaction: Transaction) = viewModelScope.launch {
        transactionDao.insertTransaction(transaction)
    }

    fun delete(transaction: Transaction) = viewModelScope.launch {
        transactionDao.deleteTransaction(transaction)
    }

    fun update(transaction: Transaction) = viewModelScope.launch {
        transactionDao.updateTransaction(transaction)
    }

    fun getTransactionById(id: Int): Flow<Transaction> {
        return transactionDao.getTransactionById(id)
    }

    fun getTransactionsByMonth(year: Int, month: Int): Flow<List<Transaction>> {
        val monthString = String.format("%d-%02d", year, month)
        return transactionDao.getTransactionsByMonth(monthString)
    }
}

class TransactionViewModelFactory(private val transactionDao: TransactionDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(transactionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
