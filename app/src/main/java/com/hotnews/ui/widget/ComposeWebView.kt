package com.hotnews.ui.widget

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Created by thomas on 3/3/2025.
 */

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ComposeWebView(
    url: String,
    modifier: Modifier = Modifier,
    onPageStarted: (String) -> Unit = {},
    onPageFinished: (String) -> Unit = {},
    onError: (WebResourceError?) -> Unit = {}
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    onPageStarted(url ?: "")
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    onPageFinished(url ?: "")
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    request?.let {
                        if (!it.url.toString().startsWith("http")) {
                            return true
                        }
                    }

                    return super.shouldOverrideUrlLoading(view, request)
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    onError(error)
                }
            }

            settings.apply {
                // enable Javascript
                javaScriptEnabled = true

                // enable http content
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
    }

    LaunchedEffect(url) {
        webView.loadUrl(url)
    }

    AndroidView(
        factory = { webView },
        modifier = modifier
    )

    DisposableEffect(Unit) {
        onDispose {
            webView.destroy()
        }
    }
}
