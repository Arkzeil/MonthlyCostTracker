package com.example.monthlycosttracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartScreen(
    viewModel: TransactionViewModel
) {
    val transactions by viewModel.allTransactions.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Charts") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val spendingByCategory = transactions
                .groupBy { it.category }
                .mapValues { (_, transactions) -> transactions.sumOf { it.amount } }

            val entries = spendingByCategory.map { (category, amount) ->
                PieEntry(amount.toFloat(), category)
            }

            AndroidView(
                factory = { context ->
                    PieChart(context).apply {
                        description.isEnabled = false
                        isDrawHoleEnabled = true
                        holeRadius = 58f
                        transparentCircleRadius = 61f
                        setUsePercentValues(true)
                        setEntryLabelColor(android.graphics.Color.BLACK)
                        setEntryLabelTextSize(12f)
                    }
                },
                update = { chart ->
                    val dataSet = PieDataSet(entries, "Spending by category")
                    dataSet.colors = com.github.mikephil.charting.utils.ColorTemplate.MATERIAL_COLORS.toList()
                    val pieData = PieData(dataSet)
                    pieData.setValueTextSize(12f)
                    pieData.setValueTextColor(android.graphics.Color.BLACK)
                    chart.data = pieData
                    chart.invalidate()
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
