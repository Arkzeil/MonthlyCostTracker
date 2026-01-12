package com.example.monthlycosttracker

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
import com.example.monthlycosttracker.ui.theme.MonthlyCostTrackerTheme
import kotlinx.coroutines.runBlocking

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

        setContent {
            MonthlyCostTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(transactionDao = transactionDao)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(transactionDao: TransactionDao) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(
                transactionDao = transactionDao,
                onAddTransactionClick = { navController.navigate("add_transaction_screen") }
            )
        }
        composable("add_transaction_screen") {
            AddTransactionScreen(
                onBackClick = { navController.popBackStack() },
                onSaveTransaction = { transaction ->
                    runBlocking {
                        transactionDao.insertTransaction(transaction)
                    }
                    navController.popBackStack()
                }
            )
        }
    }
}
