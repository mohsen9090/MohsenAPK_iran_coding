package com.myoxford24.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var web: WebView
    private var fileCb: ValueCallback<Array<Uri>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        web = WebView(this)
        setContentView(web)

        with(web.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = false
            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        }

        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(v: WebView, r: WebResourceRequest): Boolean {
                val u = r.url.toString()
                return when {
                    u.startsWith("tel:") || u.startsWith("mailto:") || u.startsWith("intent:") -> {
                        try { startActivity(Intent(Intent.ACTION_VIEW, r.url)) } catch (_: Exception) {}
                        true
                    }
                    else -> false
                }
            }
        }

        web.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                wv: WebView?, cb: ValueCallback<Array<Uri>>?, params: FileChooserParams?
            ): Boolean {
                fileCb?.onReceiveValue(null)
                fileCb = cb
                return try { startActivityForResult(params?.createIntent(), 1001); true }
                catch (_: Exception) { fileCb = null; false }
            }
        }

        web.loadUrl("http://myoxford24.com")
    }

    override fun onActivityResult(req: Int, res: Int, data: Intent?) {
        if (req == 1001) {
            fileCb?.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(res, data))
            fileCb = null
            return
        }
        super.onActivityResult(req, res, data)
    }

    override fun onBackPressed() {
        if (::web.isInitialized && web.canGoBack()) web.goBack() else super.onBackPressed()
    }
}
