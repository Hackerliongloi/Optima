package com.iqoo.aiphonedoctor.data.engine

import com.iqoo.aiphonedoctor.data.model.CauseBreakdownItem
import com.iqoo.aiphonedoctor.data.model.DemoScenario
import com.iqoo.aiphonedoctor.data.model.DiagnosisResult
import com.iqoo.aiphonedoctor.data.model.PhoneTelemetry

class DiagnosisEngine {

    fun analyze(scenario: DemoScenario, telemetry: PhoneTelemetry): DiagnosisResult {
        return when (scenario) {
            DemoScenario.BATTERY_DRAIN -> DiagnosisResult(
                issueTitle = "Abnormal Battery Drain",
                issueDescription = "Your battery is draining approximately 28% faster than your normal pattern.",
                primaryCause = "Background App Activity",
                confidencePercent = 62,
                causeBreakdown = listOf(
                    CauseBreakdownItem("Background App Activity", 62, "📱"),
                    CauseBreakdownItem("High Temperature", 24, "🌡️"),
                    CauseBreakdownItem("Poor Network Signal", 14, "📶")
                ),
                aiExplanationText = "Instagram has been using unusually high background activity while your device temperature is elevated. Poor network conditions may also be increasing power consumption.",
                recommendedActionTitle = "Restrict Instagram background activity",
                recommendedActionReason = "This should reduce unnecessary background processing and help lower battery consumption.",
                beforeTelemetry = scenario.initialTelemetry,
                afterTelemetryPreview = scenario.improvedTelemetry
            )

            DemoScenario.THERMAL_STRESS -> DiagnosisResult(
                issueTitle = "Thermal Stress Warning",
                issueDescription = "Your device temperature is rising unusually quickly during active usage.",
                primaryCause = "Gaming Workload + Charging",
                confidencePercent = 55,
                causeBreakdown = listOf(
                    CauseBreakdownItem("Gaming Workload", 55, "🎮"),
                    CauseBreakdownItem("Fast Battery Charging", 30, "⚡"),
                    CauseBreakdownItem("Background Processing", 15, "⚙️")
                ),
                aiExplanationText = "High CPU gaming demands coupled with fast charging are generating compound heat. Heavy background tasks are compounding thermal accumulation.",
                recommendedActionTitle = "Switch to Balanced Thermal Profile",
                recommendedActionReason = "Reduces peak CPU heat spike and regulates charging power to quickly lower temperature.",
                beforeTelemetry = scenario.initialTelemetry,
                afterTelemetryPreview = scenario.improvedTelemetry
            )

            DemoScenario.STORAGE_PRESSURE -> DiagnosisResult(
                issueTitle = "Storage Pressure Warning",
                issueDescription = "Internal storage is at 94% capacity, causing system RAM swap slowdowns.",
                primaryCause = "App Cache & Duplicate Media",
                confidencePercent = 52,
                causeBreakdown = listOf(
                    CauseBreakdownItem("App & System Caches", 52, "🗑️"),
                    CauseBreakdownItem("Duplicate Photos & Videos", 33, "🖼️"),
                    CauseBreakdownItem("Unused Download Packages", 15, "📦")
                ),
                aiExplanationText = "System cache accumulation has depleted free swap space, slowing down multitasking transitions and app launches.",
                recommendedActionTitle = "Purge 14.2 GB of Temporary Caches",
                recommendedActionReason = "Frees up swap space immediately and restores original system responsiveness.",
                beforeTelemetry = scenario.initialTelemetry,
                afterTelemetryPreview = scenario.improvedTelemetry
            )

            DemoScenario.GAMING_PERFORMANCE -> DiagnosisResult(
                issueTitle = "Gaming Performance Risk",
                issueDescription = "Predicted thermal throttling event during current gaming session.",
                primaryCause = "Sustained Peak GPU Thermal Load",
                confidencePercent = 58,
                causeBreakdown = listOf(
                    CauseBreakdownItem("Sustained GPU Load", 58, "🎮"),
                    CauseBreakdownItem("Background Network Sync", 27, "📡"),
                    CauseBreakdownItem("High Display Brightness", 15, "☀️")
                ),
                aiExplanationText = "Based on current thermal trend, gaming frame stability will drop within approximately 9 minutes.",
                recommendedActionTitle = "Activate iQOO Game Ultra Balanced Mode",
                recommendedActionReason = "Smooths out FPS spikes, caps thermal generation, and prevents thermal throttling.",
                beforeTelemetry = scenario.initialTelemetry,
                afterTelemetryPreview = scenario.improvedTelemetry,
                predictedTimeUntilImpact = "9 minutes"
            )
        }
    }
}
