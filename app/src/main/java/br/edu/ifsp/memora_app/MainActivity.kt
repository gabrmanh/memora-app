package br.edu.ifsp.memora_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.edu.ifsp.memora_app.ui.composable.home.MemoraHomeScreen
import br.edu.ifsp.memora_app.ui.theme.MemoraAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoraAppTheme {
                MemoraHomeScreen()
            }
        }
    }
}