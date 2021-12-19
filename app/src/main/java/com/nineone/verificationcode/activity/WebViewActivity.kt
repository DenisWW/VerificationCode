package com.nineone.verificationcode.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.nineone.verificationcode.R
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WebViewActivity : Activity() {
    var key: String? = null;
    var request: Request? = null;

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val webSettings = web_view.settings
        webSettings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true;
            databaseEnabled = true;
            useWideViewPort = true;
            cacheMode = WebSettings.LOAD_DEFAULT;
            loadsImagesAutomatically = true;
            loadWithOverviewMode = true;
        }
        web_view.addJavascriptInterface(JsInterface(), "jzsec");
        web_view.webChromeClient = object : WebChromeClient() {

        };
        web_view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (view?.progress == 100)
                    web_view.loadUrl("javascript:window.lifecycleCallback({action_type:'onPageCreate',data:null})")
//                    web_view.loadUrl("javascript:window.lifecycleCallback(\"{action_type:'onPageCreate',data:'test'}\")")
            }
        };
//        web_view.evaluateJavascript("",);
        web_view.loadUrl("http://172.16.101.171:8080/");

//        js_tv.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                TODO("Not yet implemented")
//            }
//        });
        js_tv.setOnClickListener {
//            web_view.loadUrl("javascript:window.jzsec.postMessage('test')")

        };
    }

    override fun onResume() {
        super.onResume()
        if (web_view.isShown)
            web_view.loadUrl("javascript:window.lifecycleCallback({action_type:'onPageResume',data:null})")
    }

    override fun onPause() {
        super.onPause()
        web_view.loadUrl("javascript:window.lifecycleCallback({action_type:'onPagePause',data:null})")
    }

    override fun onBackPressed() {
        if (web_view.canGoBack()) {
            web_view.goBack();
            return;
        }
        super.onBackPressed()
    }

    inner class JsInterface {
        var webViewIntent: WebViewIntent? = null

        @JavascriptInterface
        fun postMessage(message: String) {
            request = Gson().fromJson(message, Request::class.java);
            key = request?.callbackKey;
            request?.action;
            webViewIntent?.out(message);

            GlobalScope.launch(Dispatchers.Main) {
                request?.let {
                    val response = Response();
                    response.callbackKey = it.callbackKey;
                    response.errorCode = 100;
                    response.message = "测试callback";
                    val call = Gson().toJson(response);
                    web_view.loadUrl("javascript:window.__jsBridge.callback('$call')")
                }
            }
        }

    }

    interface WebViewIntent {
        fun out(json: String);
    }

    class Request {
        var action: String? = null
        var param: Any? = null
        var callbackKey: String? = null

    }

    class Response {
        var response: Any? = null
        var callbackKey: String? = null
        var errorCode: Int? = null
        var message: String? = null


    }

    enum class Lifecycle(var action: String, var any: Any?) {
        LIFECYCLE_PAGE_CREATE("onPageCreate", null),
        LIFECYCLE_PAGE_RESUME("onPageResume", null),
        LIFECYCLE_PAGE_PAUSE("onPagePause", null);
    }

}