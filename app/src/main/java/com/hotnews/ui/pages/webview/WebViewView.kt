package com.hotnews.ui.pages.webview

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AddToHomeScreen
import androidx.compose.material.icons.automirrored.filled.InsertComment
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.PlaylistAddCheck
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hotnews.ui.pages.webview.WebViewViewModel.Event
import com.hotnews.ui.widget.ComposeWebView
import com.hotnews.util.compose.TitleBar
import com.hotnews.util.compose.Toast
import com.hotnews.util.compose.clickableSingle
import com.hotnews.viewmodel.CollectEvents
import com.hotnews.viewmodel.CollectState

/**
 * Created by thomas on 3/3/2025.
 */

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewView(
    url: String,
    title: String = "",
    navController: NavController,
    viewModel: WebViewViewModel = hiltViewModel(),
    onWebViewCreated: (WebView) -> Unit = {}
) {
    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(false) }
    val webView = remember { WebView(context) }
    var canGoBack by remember { mutableStateOf(false) }
    var progress by remember { mutableIntStateOf(0) }


    viewModel.CollectEvents {
        when (it) {
            Event.Back -> navController.popBackStack()
            is Event.Toast -> {
                Toast(it.msg)
            }

            else -> Unit
        }
    }

    viewModel.CollectState {
    }

    BackHandler(enabled = canGoBack) {
        webView.goBack()
    }

    webView.webViewClient = object : WebViewClient() {
        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
            canGoBack = webView.canGoBack()
        }
    }

    webView.webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            progress = newProgress
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else {
            Row {
                TitleBar(title, modifier = Modifier.weight(1f)) {
                    viewModel.send(Event.Back)
                }

                Icon(
                    Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .clickableSingle {
                            viewModel.addToFavorites(url, title)
                        }
                        .align(Alignment.CenterVertically)
                        .padding(end = 5.dp)
                )
            }
        }

        ComposeWebView(
            url = url,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            onPageStarted = { isLoading = true },
            onPageFinished = { isLoading = false },
            onError = { error ->
                Log.e("WebView", "Error: ${error?.description}")
            }
        )

    }
}