package com.myoxford24.app

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)

        // تنظیمات لازم
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        // داخل اپ باز کن، نه با مرورگر خارجی
        webView.webViewClient = WebViewClient()

        // آدرس سایتت
        webView.loadUrl("https://myoxford24.com/")
    }

    // دکمه Back داخل وب‌ویو برگرده
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
