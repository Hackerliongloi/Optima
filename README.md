# 📱 iQOO AI Phone Doctor

> **Proactive On-Device Diagnostics & Thermal Optimization System**

**iQOO AI Phone Doctor** is an intelligent diagnostic and telemetry optimization assistant designed for modern high-performance smartphones. It continuously monitors multi-signal telemetry (CPU, GPU, RAM, battery drain rate, temperature, and background app activity) locally on-device to predict thermal throttling, pinpoint battery drain causes, and recommend instant one-tap fixes.

---

## ✨ Key Features

- **⚡ Live Telemetry Monitoring**: Tracks real-time device stats including core temperatures, CPU/RAM usage percentages, storage swap pressure, and battery drain per hour.
- **🔍 Multi-Signal Correlation Engine**: Analyzes simultaneous workload patterns (e.g., fast charging coupled with high GPU load) to diagnose root causes before performance degrades.
- **🤖 On-Device RAG AI Assistant**: Interactive AI Chatbot powered by local diagnostic knowledge rules to answer queries like *"Why is my battery draining so fast?"* or *"Why is my phone getting hot?"*.
- **📊 Personalization & Baseline Profiling**: Compares current real-time telemetry against historical usage baselines to detect anomalous spikes early.
- **🌐 Dual Interface**:
  - **Native Android Application**: Modern Jetpack Compose UI with dark mode cyber aesthetics tailored for iQOO devices.
  - **Interactive Web Demo**: Pure HTML5/CSS3/JS web prototype for live web demonstrations.

---

## 🛠️ Technology Stack

| Layer | Technologies |
| :--- | :--- |
| **Android App** | Kotlin, Jetpack Compose, Material3, Kotlin Coroutines & Flow, ViewModel |
| **Web Prototype** | HTML5, Vanilla CSS3 (Custom Dark Theme & Glassmorphism), Modern JavaScript |
| **Build & Tooling** | Gradle 8.x, JDK 17, Android SDK 34 |

---

## 📁 Repository Structure

```text
AI-Phone-Doctor/
├── app/                                    # Native Android Project
│   ├── src/main/java/com/iqoo/aiphonedoctor/
│   │   ├── data/
│   │   │   ├── engine/                     # Diagnosis & RAG Engines
│   │   │   ├── model/                      # Telemetry & Scenario Models
│   │   │   └── repository/                 # History & Gemini Repositories
│   │   ├── ui/
│   │   │   ├── navigation/                 # Jetpack Compose Navigation
│   │   │   ├── screens/                    # Home, Diagnosis, Chat, Profile screens
│   │   │   ├── theme/                      # Color palettes & typography
│   │   │   └── viewmodel/                  # MainViewModel
│   │   └── MainActivity.kt
│   └── build.gradle.kts
├── web_demo/                               # Web Prototype Application
│   ├── index.html                          # Main Web UI
│   ├── style.css                           # Glassmorphism Dark CSS Theme
│   ├── app.js                              # Web Application Logic & Telemetry Simulator
│   └── manifest.json
├── build.gradle.kts                        # Root Gradle Config
└── settings.gradle.kts                     # Project Settings
```

---

## 🚀 How to Run

### 1. Running the Android Application
1. Open **Android Studio**.
2. Select **Open** and choose this project directory (`AI-Phone-Doctor`).
3. Allow Gradle to sync dependencies.
4. Select your connected device or emulator and click **Run ▶** (`Control + R` / `Shift + F10`).

Alternatively, build the debug APK via command line:
```bash
./gradlew assembleDebug
```
The compiled APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

### 2. Running the Web Prototype
Run a local HTTP server inside the `web_demo` directory:
```bash
python3 -m http.server 8080 --directory web_demo
```
Open **`http://localhost:8080`** in any modern web browser.

---

## 🔒 Privacy & Security

All telemetry processing, rule matching, and baseline comparisons run **100% locally on-device**. No private app logs, user data, or personal files leave your device.

---

## 📄 License
Licensed under the [MIT License](LICENSE).
