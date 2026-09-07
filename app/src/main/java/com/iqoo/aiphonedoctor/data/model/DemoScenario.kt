package com.iqoo.aiphonedoctor.data.model

enum class DemoScenario(
    val title: String,
    val subtitle: String,
    val initialHealthScore: Int,
    val initialTelemetry: PhoneTelemetry,
    val improvedTelemetry: PhoneTelemetry
) {
    BATTERY_DRAIN(
        title = "Battery Drain",
        subtitle = "Unusual background activity & network strain",
        initialHealthScore = 87,
        initialTelemetry = PhoneTelemetry(
            batteryLevelPercent = 48,
            temperatureCelsius = 43.0f,
            cpuUsagePercent = 78,
            ramUsagePercent = 82,
            storageUsagePercent = 68,
            networkQuality = "Poor",
            isCharging = false,
            activeApp = "Instagram",
            backgroundActivityLevel = "High",
            batteryDrainPerHour = 8.2f,
            normalBatteryDrainPerHour = 6.4f,
            performanceStabilityScore = 91
        ),
        improvedTelemetry = PhoneTelemetry(
            batteryLevelPercent = 48,
            temperatureCelsius = 39.0f,
            cpuUsagePercent = 61,
            ramUsagePercent = 69,
            storageUsagePercent = 68,
            networkQuality = "Good",
            isCharging = false,
            activeApp = "Instagram",
            backgroundActivityLevel = "Restricted",
            batteryDrainPerHour = 6.7f,
            normalBatteryDrainPerHour = 6.4f,
            performanceStabilityScore = 96
        )
    ),

    THERMAL_STRESS(
        title = "Thermal Stress",
        subtitle = "Gaming while charging + high CPU load",
        initialHealthScore = 74,
        initialTelemetry = PhoneTelemetry(
            batteryLevelPercent = 65,
            temperatureCelsius = 46.5f,
            cpuUsagePercent = 91,
            ramUsagePercent = 86,
            storageUsagePercent = 64,
            networkQuality = "Good",
            isCharging = true,
            activeApp = "BGMI",
            backgroundActivityLevel = "High",
            batteryDrainPerHour = 11.4f,
            normalBatteryDrainPerHour = 6.4f,
            performanceStabilityScore = 72
        ),
        improvedTelemetry = PhoneTelemetry(
            batteryLevelPercent = 65,
            temperatureCelsius = 40.2f,
            cpuUsagePercent = 68,
            ramUsagePercent = 71,
            storageUsagePercent = 64,
            networkQuality = "Good",
            isCharging = true,
            activeApp = "BGMI",
            backgroundActivityLevel = "Balanced",
            batteryDrainPerHour = 7.8f,
            normalBatteryDrainPerHour = 6.4f,
            performanceStabilityScore = 94
        )
    ),

    STORAGE_PRESSURE(
        title = "Storage Pressure",
        subtitle = "System cache & duplicate media accumulation",
        initialHealthScore = 79,
        initialTelemetry = PhoneTelemetry(
            batteryLevelPercent = 82,
            temperatureCelsius = 37.0f,
            cpuUsagePercent = 54,
            ramUsagePercent = 88,
            storageUsagePercent = 94,
            networkQuality = "Excellent",
            isCharging = false,
            activeApp = "Gallery",
            backgroundActivityLevel = "Medium",
            batteryDrainPerHour = 6.9f,
            normalBatteryDrainPerHour = 6.4f,
            performanceStabilityScore = 85
        ),
        improvedTelemetry = PhoneTelemetry(
            batteryLevelPercent = 82,
            temperatureCelsius = 35.5f,
            cpuUsagePercent = 42,
            ramUsagePercent = 64,
            storageUsagePercent = 78,
            networkQuality = "Excellent",
            isCharging = false,
            activeApp = "Gallery",
            backgroundActivityLevel = "Cleaned",
            batteryDrainPerHour = 6.2f,
            normalBatteryDrainPerHour = 6.4f,
            performanceStabilityScore = 97
        )
    ),

    GAMING_PERFORMANCE(
        title = "Gaming Performance",
        subtitle = "Predicted thermal throttling during BGMI session",
        initialHealthScore = 83,
        initialTelemetry = PhoneTelemetry(
            batteryLevelPercent = 67,
            temperatureCelsius = 41.0f,
            cpuUsagePercent = 85,
            ramUsagePercent = 80,
            storageUsagePercent = 60,
            networkQuality = "Good",
            isCharging = false,
            activeApp = "BGMI",
            backgroundActivityLevel = "High",
            batteryDrainPerHour = 9.8f,
            normalBatteryDrainPerHour = 6.4f,
            performanceStabilityScore = 91
        ),
        improvedTelemetry = PhoneTelemetry(
            batteryLevelPercent = 67,
            temperatureCelsius = 37.8f,
            cpuUsagePercent = 65,
            ramUsagePercent = 68,
            storageUsagePercent = 60,
            networkQuality = "Good",
            isCharging = false,
            activeApp = "BGMI",
            backgroundActivityLevel = "Optimized",
            batteryDrainPerHour = 7.1f,
            normalBatteryDrainPerHour = 6.4f,
            performanceStabilityScore = 98
        )
    )
}
