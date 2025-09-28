package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.hermes.reactexecutor.HermesExecutorFactory
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.common.LifecycleState
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.shell.MainReactPackage
import com.reactnativecommunity.webview.RNCWebViewPackage

class LitReactActivity : AppCompatActivity(), DefaultHardwareBackBtnHandler {

    private lateinit var reactRootView: ReactRootView
    private lateinit var reactInstanceManager: ReactInstanceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) Create a ReactRootView
        reactRootView = ReactRootView(this)

        // 2) Build ReactInstanceManager with EXPLICIT Hermes and EXPLICIT bundle
        reactInstanceManager = ReactInstanceManager.builder()
            .setApplication(application)
            .setCurrentActivity(this)
            // We already bundled to assets:
            .setBundleAssetName("index.android.bundle")
            // Add the main package (and any others your npm pkg needs)
            .addPackage(MainReactPackage())
            .addPackage(RNCWebViewPackage())
            // Force Hermes as the JS engine
            .setJavaScriptExecutorFactory(HermesExecutorFactory())
            // No dev support (avoids devsupport JNI)
            .setUseDeveloperSupport(false)
            // Name must match AppRegistry.registerComponent in rn/index.js
            .setInitialLifecycleState(LifecycleState.RESUMED)
            .build()

        // 3) Start the RN app; props are optional
        val initialProps = Bundle().apply {
            intent.getStringExtra("url")?.let { putString("url", it) }
            intent.getStringExtra("headersJson")?.let { putString("headers", it) }
        }
        reactRootView.startReactApplication(
            reactInstanceManager,
            "LitScreen", // <-- must match your AppRegistry name
            initialProps
        )

        setContentView(reactRootView)
    }

    override fun onResume() {
        super.onResume()
        reactInstanceManager.onHostResume(this, this)
    }

    override fun onPause() {
        super.onPause()
        reactInstanceManager.onHostPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        reactInstanceManager.onHostDestroy(this)
        reactRootView.unmountReactApplication()
    }

    override fun onBackPressed() {
        Log.d("BACK PRESSED","back is pressed")
        if (::reactInstanceManager.isInitialized) {
            reactInstanceManager.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }
    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed() // or: finish()
    }
}
