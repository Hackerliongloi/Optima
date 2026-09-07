package com.iqoo.aiphonedoctor.data.repository

import com.iqoo.aiphonedoctor.data.model.HistoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class HistoryRepository {
    private val initialHistory = listOf(
        HistoryItem(
            id = "hist-1",
            timestamp = "10:42 AM",
            timeCategory = "Today",
            issueTitle = "Battery Drain Detected",
            issueSeverity = "RED",
            cause = "Background app activity (Instagram)",
            actionTaken = "Background activity restricted",
            resultStatus = "Improved",
            batteryDrainSaved = "-1.5% / hr"
        ),
        HistoryItem(
            id = "hist-2",
            timestamp = "Yesterday 08:15 PM",
            timeCategory = "Yesterday",
            issueTitle = "Thermal Stress Warning",
            issueSeverity = "YELLOW",
            cause = "Gaming + fast charging",
            actionTaken = "Balanced performance profile active",
            resultStatus = "Improved",
            batteryDrainSaved = "-6.3°C lowered"
        ),
        HistoryItem(
            id = "hist-3",
            timestamp = "Sep 04, 03:20 PM",
            timeCategory = "3 days ago",
            issueTitle = "RAM & Cache Congestion",
            issueSeverity = "YELLOW",
            cause = "12.4 GB system cache accumulation",
            actionTaken = "Automated cache purge",
            resultStatus = "Optimized",
            batteryDrainSaved = "+1.8 GB free swap"
        )
    )

    private val _historyItems = MutableStateFlow<List<HistoryItem>>(initialHistory)
    val historyItems: StateFlow<List<HistoryItem>> = _historyItems.asStateFlow()

    fun addHistoryRecord(
        issueTitle: String,
        cause: String,
        actionTaken: String,
        savings: String
    ) {
        val newItem = HistoryItem(
            id = UUID.randomUUID().toString(),
            timestamp = "Just Now",
            timeCategory = "Today",
            issueTitle = issueTitle,
            issueSeverity = "RED",
            cause = cause,
            actionTaken = actionTaken,
            resultStatus = "Improved",
            batteryDrainSaved = savings
        )
        _historyItems.value = listOf(newItem) + _historyItems.value
    }
}
