package br.edu.ifsp.memora_app.ui.composable.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.edu.ifsp.memora_app.ui.composable.MemoraBottomNav
import br.edu.ifsp.memora_app.ui.composable.MemoraTopBar

@Composable
fun MemoraHomeScreen() {
    Scaffold(
        topBar = { MemoraTopBar() },
        bottomBar = {
            MemoraBottomNav(
                selectedItem = "home",
                onItemSelected = { /* handle nav */ })
        }
    ) { innerPadding ->
        // TODO: Add home screen content here
        Box(modifier = Modifier.padding(innerPadding)) {
            // Content goes here
        }
    }
}
