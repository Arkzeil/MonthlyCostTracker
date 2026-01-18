package com.example.monthlycosttracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import java.util.Calendar
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class, com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    viewModel: TransactionViewModel,
    onAddTransactionClick: () -> Unit,
    onEditTransactionClick: (Int) -> Unit
) {
    val calendar = remember { Calendar.getInstance() }
    var startYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var endYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var totalMonths by remember { mutableStateOf(12) }
    var initialPage by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(viewModel) {
        isLoading = true
        val transactions = viewModel.getAllTransactionsList()
        if (transactions.isNotEmpty()) {
            val minYear = transactions.minOf { it.date.substring(0, 4).toInt() }
            val maxYear = transactions.maxOf { it.date.substring(0, 4).toInt() }
            startYear = minYear
            endYear = maxYear
            totalMonths = (endYear - startYear + 1) * 12
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            initialPage = (currentYear - startYear) * 12 + currentMonth
        }
        isLoading = false
    }

    val pagerState = rememberPagerState(initialPage = initialPage)

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
            val currentSelectedYear = startYear + pagerState.currentPage / 12
            val currentSelectedMonth = pagerState.currentPage % 12
            calendar.set(currentSelectedYear, currentSelectedMonth, 1)
            val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, java.util.Locale.getDefault()) ?: ""

            Text(
                text = "$monthName ${currentSelectedYear}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            HorizontalPager(
                count = totalMonths,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val year = startYear + page / 12
                val month = page % 12 + 1
                val transactions by viewModel.getTransactionsByMonth(year, month).collectAsState(initial = emptyList())
                val totalCost = transactions.sumOf { it.amount }

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Column {
                        TotalCostHeader(totalCost = totalCost)
                        TransactionList(
                            transactions = transactions,
                            viewModel = viewModel,
                            onEditTransactionClick = onEditTransactionClick
                        )
                    }
                }
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
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
    if (transactions.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "No transactions",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text("No transactions yet. Add one to get started!")
        }
    } else {
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
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    viewModel: TransactionViewModel,
    onEditTransactionClick: (Int) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this transaction?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.delete(transaction)
                        showDialog = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

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
            Column {
                Text(transaction.description, style = MaterialTheme.typography.bodyLarge)
                Text(transaction.date, style = MaterialTheme.typography.bodySmall)
            }
            Text("$${DecimalFormatter.formatter.format(transaction.amount)}", fontWeight = FontWeight.SemiBold)
            Row {
                IconButton(onClick = { onEditTransactionClick(transaction.id) }) {
                    Icon(Icons.Filled.Edit, "Edit transaction")
                }
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Filled.Delete, "Delete transaction")
                }
            }
        }
    }
}
