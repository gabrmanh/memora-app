package br.edu.ifsp.memora_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifsp.memora_app.data.local.AppDatabase
import br.edu.ifsp.memora_app.ui.composable.auth.AuthScreen
import br.edu.ifsp.memora_app.ui.composable.home.MemoraHomeScreen
import br.edu.ifsp.memora_app.ui.config.SessionManager
import br.edu.ifsp.memora_app.ui.theme.MemoraAppTheme
import br.edu.ifsp.memora_app.ui.viewmodel.AuthViewModel
import br.edu.ifsp.memora_app.ui.viewmodel.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.init(this)

        enableEdgeToEdge()
        setContent {
            MemoraAppTheme {
                MemoraApp()
            }
        }
    }
}

@Composable
fun MemoraApp() {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(database.userDao())
    )
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    if (isLoggedIn) {
        MemoraHomeScreen()
    } else {
        AuthScreen(viewModel = authViewModel)
    }
}
