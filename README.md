# 📱 Optima (AI Phone Doctor)

> **Proactive On-Device Diagnostics, Hardware Telemetry & Optimization System**

**Optima** (AI Phone Doctor) is an intelligent diagnostic and hardware telemetry optimization assistant designed for modern high-performance smartphones. It continuously monitors multi-signal telemetry (CPU load, RAM usage, storage space, battery drain rate, real-time temperature, and foreground app activity) directly on-device to predict thermal throttling, pinpoint battery drain root causes, and execute real system optimizations.

---

## ✨ Key Features

- **⚡ Real Device Hardware Telemetry**: Dynamically detects your phone's exact model (`Build.MODEL`), manufacturer, brand, Android OS release version, SoC chipset, real system RAM (GB), and internal storage space (GB).
- **🌡️ Live Sensor Monitoring**: Reads battery temperature (°C), charge state, signal strength, cellular carrier, and active foreground apps in real time.
- **🔍 Multi-Signal Correlation Engine**: Analyzes simultaneous workload patterns (e.g. fast charging combined with heavy GPU load or background app memory leaks) to diagnose root causes before performance drops.
- **🤖 Optima AI Assistant**: Sticky, intelligent AI Chatbot powered by local diagnostic RAG rules to answer queries like *"Why is my battery draining so fast?"* or *"Why is my phone getting hot?"*.
- **⚡ Real Hardware Optimization**: Performs real background process termination (`killBackgroundProcesses`), runtime Java garbage collection (`System.gc()`), and storage cache directory cleanup (`cacheDir.deleteRecursively()`).
- **🛡️ Silent Operation**: Operates silently on-device without intrusive permission dialog popups or intrusive banners.
- **📊 Centered Verification Flow**: Presents clear before/after telemetry verification metrics with centered optimization success indicators.
- **🌐 Dual Interface**:
  - **Native Android Application**: Modern Jetpack Compose UI with dark mode cyber aesthetics (`CyberBlack`, `IqooOrange`, `SuccessGreen`).
  - **Interactive Web Application**: HTML5/CSS3/JavaScript prototype for browser-based demonstrations.

---

## 🛠️ Technology Stack

| Layer | Technologies |
| :--- | :--- |
| **Android Framework** | Kotlin, Jetpack Compose, Material3, Coroutines, StateFlow, ViewModel |
| **Hardware APIs** | `ActivityManager`, `BatteryManager`, `StatFs`, `TelephonyManager`, `UsageStatsManager` |
| **Web Prototype** | HTML5, Vanilla CSS3 (Dark Cyber Theme), Modern ES6 JavaScript |
| **Build & Tooling** | Gradle 8.x, JDK 17, Android SDK 34 |

---

## 📁 Repository Structure

```text
AI-Phone-Doctor/
├── app/                                    # Native Android Application
│   ├── src/main/java/com/iqoo/aiphonedoctor/
│   │   ├── data/
│   │   │   ├── engine/                     # Diagnosis & RAG Engines
│   │   │   ├── model/                      # Telemetry & Scenario Models
│   │   │   ├── repository/                 # Gemini & History Repositories
│   │   │   └── telemetry/                  # RealTelemetryProvider (Hardware Sensors)
│   │   ├── ui/
│   │   │   ├── navigation/                 # Jetpack Compose Navigation & Containers
│   │   │   ├── screens/                    # Home, Diagnosis, Chat, Profile & Verification
│   │   │   ├── theme/                      # Cyber Dark Color Palette & Typography
│   │   │   └── viewmodel/                  # MainViewModel
│   │   └── MainActivity.kt                 # Application Entry Point
│   └── build.gradle.kts
├── web_demo/                               # Web Prototype Application
│   ├── index.html                          # Main Web Viewport
│   ├── style.css                           # Glassmorphism Dark CSS
│   ├── app.js                              # Web Logic & Telemetry Engine
│   └── manifest.json
├── build.gradle.kts                        # Root Gradle Build Configuration
└── settings.gradle.kts                     # Project Settings
```

---

## 🚀 How to Run & Build

### 1. Building the Native Android APK
You can build and assemble the debug APK directly via command line:
```bash
./gradlew assembleDebug
```
The generated APK file will be located at:
`app/build/outputs/apk/debug/app-debug.apk`

To install directly onto a connected Android device or emulator:
```bash
./gradlew installDebug
```

### 2. Running the Web Demo
Run a local HTTP web server in the `web_demo` directory:
```bash
python3 -m http.server 8085 --directory web_demo
```
Then navigate to **`http://localhost:8085`** in your browser.

---

## 🔒 On-Device Privacy & Security

All telemetry analytics, sensor readings, and rule correlations run **100% locally on-device**. No private app logs or personal files ever leave your phone.

---

## 📄 License
Licensed under the [MIT License](LICENSE).
