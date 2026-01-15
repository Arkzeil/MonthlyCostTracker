package com.example.monthlycosttracker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.monthlycosttracker.AddTransactionScreen
import com.example.monthlycosttracker.AppDatabase
import com.example.monthlycosttracker.MainScreen
import androidx.activity.viewModels
import com.example.monthlycosttracker.EditTransactionScreen
import com.example.monthlycosttracker.TransactionDao
import com.example.monthlycosttracker.TransactionViewModel
import com.example.monthlycosttracker.TransactionViewModelFactory
import com.example.monthlycosttracker.ui.theme.MonthlyCostTrackerTheme

class MainActivity : ComponentActivity() {
    private lateinit var transactionDao: TransactionDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "monthly-cost-tracker-db"
        ).build()
        transactionDao = database.transactionDao()

        val viewModel: TransactionViewModel by viewModels {
            TransactionViewModelFactory(transactionDao)
        }

        setContent {
            MonthlyCostTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: TransactionViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(
                viewModel = viewModel,
                onAddTransactionClick = { navController.navigate("add_transaction_screen") },
                onEditTransactionClick = { transactionId ->
                    navController.navigate("edit_transaction_screen/$transactionId")
                }
            )
        }
        composable("add_transaction_screen") {
            AddTransactionScreen(
                onBackClick = { navController.popBackStack() },
                onSaveTransaction = { transaction ->
                    viewModel.insert(transaction)
                    navController.popBackStack()
                }
            )
        }
        composable("edit_transaction_screen/{transactionId}") { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("transactionId")?.toIntOrNull()
            if (transactionId != null) {
                EditTransactionScreen(
                    viewModel = viewModel,
                    transactionId = transactionId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}