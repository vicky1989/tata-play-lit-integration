Moving to another machine: prerequisites & one-time setup

Must-have tools
- JDK: 17 (not 11/21)
- Android Studio (latest) with:
  • Android SDK 35 (or the compileSdk you set)
  • Build-tools (matching SDK), Platform-Tools (ADB)
- Node: 18+ LTS (recommend 18 or 20)
- Yarn or npm (pick one; you used Yarn)
- Watchman (optional, macOS)

Project-specific knobs you added (important!)
1) settings.gradle.kts
   - Repo for RN AARs:
       maven(uri("$rootDir/rn/node_modules/react-native/android"))
   - Include WebView:
       include(":react-native-webview")
       project(":react-native-webview").projectDir =
           File(rootDir, "rn/node_modules/react-native-webview/android")

2) Root build.gradle.kts
   - Global substitution for RN artifact rename:
       configurations.configureEach {
         resolutionStrategy.dependencySubstitution {
           substitute(module("com.facebook.react:react-native"))
             .using(module("com.facebook.react:react-android:0.74.3"))
         }
         resolutionStrategy.eachDependency {
           if (requested.group == "com.facebook.react" && requested.name == "react-native") {
             useTarget("com.facebook.react:react-android:0.74.3")
           }
         }
       }
   - Force Java/Kotlin 17 across modules (no options.release).

3) app/build.gradle.kts
   - Java/Kotlin 17:
       compileOptions {
         sourceCompatibility = JavaVersion.VERSION_17
         targetCompatibility = JavaVersion.VERSION_17
       }
       kotlinOptions { jvmTarget = "17" }
   - RN deps:
       implementation("com.facebook.react:react-android:0.74.3")
       implementation("com.facebook.react:hermes-android:0.74.3")
       implementation(project(":react-native-webview"))

4) MainApplication.kt (old arch)
   - Register WebView:
       override fun getPackages(): List<ReactPackage> {
         val packages = mutableListOf<ReactPackage>();
         packages += MainReactPackage();
         packages += RNCWebViewPackage();
         return packages;
       }

5) LitReactActivity
   - Either .addPackage(RNCWebViewPackage()) if you build a custom manager,
   - Or reuse the application’s reactNativeHost.reactInstanceManager (cleaner).

6) AndroidManifest.xml
   - Internet permission:
       <uses-permission android:name="android.permission.INTERNET" />

7) gradle.properties
   - Ensure Gradle sees JDK 17:
       org.gradle.java.home=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home  # macOS ARM
       org.gradle.java.installations.fromEnv=JAVA_HOME
       org.gradle.java.installations.auto-download=true

On a new machine, adjust org.gradle.java.home to that machine’s JDK-17 path (or just set JAVA_HOME to JDK 17 and keep auto-download on).

Build/run checklist on a new machine
0) clone
   git clone <your-repo-url>
   cd MyApplication

1) install JS deps inside rn/
   cd rn
   yarn install
   cd ..

2) set JAVA_HOME to 17 for this shell (sample mac paths)
   export JAVA_HOME=$(/usr/libexec/java_home -v 17 || echo "/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home")

3) bundle JS to assets (you’ve been doing this)
   cd rn
   npx react-native@0.74.3 bundle      --platform android      --dev false      --reset-cache      --entry-file entry.js      --bundle-output ../app/src/main/assets/index.android.bundle      --assets-dest ../app/src/main/res/
   cd ..

4) build & install
   ./gradlew clean
   ./gradlew :app:installDebug

5) logs
   adb logcat -c
   adb logcat -s ReactNativeJS,AndroidRuntime

Note: You don’t need yarn start because you’re loading the pre-bundled index.android.bundle from assets.
