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
