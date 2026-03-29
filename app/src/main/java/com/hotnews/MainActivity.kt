package com.hotnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.hotnews.ui.pages.home.HomeView
import com.hotnews.ui.theme.HotNewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HotNewsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeView(Modifier.padding(innerPadding))
                }
            }
        }
    }
}