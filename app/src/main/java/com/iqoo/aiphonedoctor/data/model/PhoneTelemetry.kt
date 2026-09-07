package com.iqoo.aiphonedoctor.data.model

data class PhoneTelemetry(
    val batteryLevelPercent: Int = 48,
    val temperatureCelsius: Float = 43.0f,
    val cpuUsagePercent: Int = 78,
    val ramUsagePercent: Int = 82,
    val storageUsagePercent: Int = 68,
    val networkQuality: String = "Poor", // Excellent, Good, Fair, Poor
    val isCharging: Boolean = false,
    val activeApp: String = "Instagram",
    val backgroundActivityLevel: String = "High", // Low, Medium, High
    val batteryDrainPerHour: Float = 8.2f, // % per hour
    val normalBatteryDrainPerHour: Float = 6.4f,
    val performanceStabilityScore: Int = 88
)
