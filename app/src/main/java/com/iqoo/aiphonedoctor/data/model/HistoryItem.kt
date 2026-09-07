package com.iqoo.aiphonedoctor.data.model

data class HistoryItem(
    val id: String,
    val timestamp: String,
    val timeCategory: String, // "Today", "Yesterday", "3 days ago"
    val issueTitle: String,
    val issueSeverity: String, // "RED", "YELLOW", "GREEN"
    val cause: String,
    val actionTaken: String,
    val resultStatus: String, // "Improved", "Optimized", "Resolved"
    val batteryDrainSaved: String
)
