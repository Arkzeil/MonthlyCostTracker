package com.example.monthlycosttracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButton

import androidx.compose.material.icons.filled.Edit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: TransactionViewModel,
    onAddTransactionClick: () -> Unit,
    onEditTransactionClick: (Int) -> Unit
) {
    val transactions by viewModel.allTransactions.collectAsState(initial = emptyList())

    val totalCost = transactions.sumOf { it.amount }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Monthly Cost Tracker") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTransactionClick) {
                Icon(Icons.Filled.Add, "Add transaction")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TotalCostHeader(totalCost = totalCost)
            TransactionList(
                transactions = transactions,
                viewModel = viewModel,
                onEditTransactionClick = onEditTransactionClick
            )
        }
    }
}

@Composable
fun TotalCostHeader(totalCost: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Total Monthly Cost", style = MaterialTheme.typography.headlineMedium)
            Text(
                "$${DecimalFormatter.formatter.format(totalCost)}",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun TransactionList(
    transactions: List<Transaction>,
    viewModel: TransactionViewModel,
    onEditTransactionClick: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(transactions) { transaction ->
            TransactionItem(
                transaction = transaction,
                viewModel = viewModel,
                onEditTransactionClick = onEditTransactionClick
            )
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    viewModel: TransactionViewModel,
    onEditTransactionClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(transaction.description, style = MaterialTheme.typography.bodyLarge)
            Text("$${DecimalFormatter.formatter.format(transaction.amount)}", fontWeight = FontWeight.SemiBold)
            Row {
                IconButton(onClick = { onEditTransactionClick(transaction.id) }) {
                    Icon(Icons.Filled.Edit, "Edit transaction")
                }
                IconButton(onClick = { viewModel.delete(transaction) }) {
                    Icon(Icons.Filled.Delete, "Delete transaction")
                }
            }
        }
    }
}
