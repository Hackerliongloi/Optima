package com.iqoo.aiphonedoctor.data.model

data class PhoneTelemetry(
    // Device & Hardware Info
    val deviceModel: String = "Unknown Model",
    val manufacturer: String = "Android Device",
    val brand: String = "Android",
    val androidVersion: String = "Android 14",
    val sdkVersion: Int = 34,
    val chipsetHardware: String = "Octa-Core Processor",
    val totalRamGB: Double = 16.0,
    val availableRamGB: Double = 4.0,
    val totalStorageGB: Double = 256.0,
    val availableStorageGB: Double = 80.0,

    // Telemetry Metrics
    val batteryLevelPercent: Int = 48,
    val temperatureCelsius: Float = 43.0f,
    val cpuUsagePercent: Int = 78,
    val ramUsagePercent: Int = 82,
    val storageUsagePercent: Int = 68,
    val networkQuality: String = "Poor",
    val isCharging: Boolean = false,
    val activeApp: String = "Instagram",
    val backgroundActivityLevel: String = "High",
    val batteryDrainPerHour: Float = 8.2f,
    val normalBatteryDrainPerHour: Float = 6.4f,
    val performanceStabilityScore: Int = 88,

    // Permission States
    val isStoragePermissionGranted: Boolean = false,
    val isPhoneStatePermissionGranted: Boolean = false,
    val isUsageAccessGranted: Boolean = false
)
