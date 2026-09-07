package com.iqoo.aiphonedoctor.data.model

data class CauseBreakdownItem(
    val title: String,
    val percentage: Int,
    val icon: String // emoji or identifier
)

data class DiagnosisResult(
    val issueTitle: String,
    val issueDescription: String,
    val primaryCause: String,
    val confidencePercent: Int,
    val causeBreakdown: List<CauseBreakdownItem>,
    val aiExplanationText: String,
    val recommendedActionTitle: String,
    val recommendedActionReason: String,
    val beforeTelemetry: PhoneTelemetry,
    val afterTelemetryPreview: PhoneTelemetry,
    val predictedTimeUntilImpact: String? = null // e.g. "9 minutes"
)
