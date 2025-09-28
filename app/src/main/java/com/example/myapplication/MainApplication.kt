package com.example.myapplication

import android.app.Application
import com.facebook.react.ReactApplication
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.shell.MainReactPackage
import com.facebook.soloader.SoLoader
import com.reactnativecommunity.webview.RNCWebViewPackage

class MainApplication : Application(), ReactApplication {

    private val mReactNativeHost: ReactNativeHost = object : ReactNativeHost(this) {
        // Disable dev support to avoid missing JNI libs
        override fun getUseDeveloperSupport(): Boolean = false

        // Tell RN to load pre-bundled asset
        override fun getBundleAssetName(): String = "index.android.bundle"

        override fun getPackages(): List<ReactPackage> {
            val packages = mutableListOf<ReactPackage>()
            packages += MainReactPackage()
            packages += RNCWebViewPackage()
            return packages
        }
    }

    override val reactNativeHost: ReactNativeHost
        get() = mReactNativeHost

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)
    }
}
