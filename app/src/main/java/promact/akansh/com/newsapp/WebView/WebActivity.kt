package promact.akansh.com.newsapp.WebView

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import promact.akansh.com.newsapp.R

class WebActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var webView: WebView
    private lateinit var url: String

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        toolbar = findViewById(R.id.toolbar)
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        url = intent.getStringExtra(getString(R.string.url))
        toolbar.title = getString(R.string.app_name)
        toolbar.setTitleTextColor(resources.getColor(R.color.colorNewsCard))
        toolbar.navigationIcon = ContextCompat.getDrawable(this@WebActivity, R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
