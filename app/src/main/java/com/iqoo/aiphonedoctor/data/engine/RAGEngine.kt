package com.iqoo.aiphonedoctor.data.engine

import com.iqoo.aiphonedoctor.data.model.*
import java.util.UUID

class RAGEngine {

    fun generateRAGResponse(
        userQuery: String,
        telemetry: PhoneTelemetry,
        scenario: DemoScenario,
        history: List<HistoryItem>
    ): ChatMessage {
        val queryLower = userQuery.lowercase()

        // 1. Identify Topic match from Knowledge Base
        val matchedDoc = PhoneKnowledgeBase.documents.find { doc ->
            doc.keywords.any { keyword -> queryLower.contains(keyword) }
        } ?: PhoneKnowledgeBase.documents.first() // Default to general lag/performance

        // 2. Retrieve Phone Context & Baseline
        val currentTemp = telemetry.temperatureCelsius
        val currentRam = telemetry.ramUsagePercent
        val currentDrain = telemetry.batteryDrainPerHour
        val normalDrain = telemetry.normalBatteryDrainPerHour
        val activeApp = telemetry.activeApp

        // 3. Build RAG Evidence Sources
        val ragSources = mutableListOf<RAGSource>()
        ragSources.add(RAGSource("Current RAM Usage", "$currentRam%"))
        ragSources.add(RAGSource("Device Temperature", "${currentTemp}°C"))
        ragSources.add(RAGSource("Normal Temp Baseline", "36.0°C"))
        ragSources.add(RAGSource("Active App", activeApp))

        if (queryLower.contains("battery") || queryLower.contains("drain")) {
            ragSources.add(RAGSource("Current Drain", "$currentDrain% / hr"))
            ragSources.add(RAGSource("Normal Baseline Drain", "$normalDrain% / hr"))
        }

        // 4. Synthesize Grounded AI Explanation
        val explanationText = when {
            queryLower.contains("battery") || queryLower.contains("drain") -> {
                "Your battery is currently draining at ${currentDrain}%/hr, which is 28% higher than your normal ${normalDrain}%/hr baseline. $activeApp is using unusually high background processing while device temperature is ${currentTemp}°C."
            }
            queryLower.contains("hot") || queryLower.contains("heat") || queryLower.contains("warm") -> {
                "Your device sensors report ${currentTemp}°C (normal baseline is ~36.0°C). Simultaneous high CPU background workload and elevated ambient heat are compounding thermal accumulation."
            }
            queryLower.contains("fps") || queryLower.contains("bgmi") || queryLower.contains("game") || queryLower.contains("gaming") -> {
                "During your active $activeApp session, temperature has reached ${currentTemp}°C with RAM at ${currentRam}%. Performance stability is at 91%, and thermal throttling is predicted within ~9 minutes."
            }
            else -> { // Default: Lag / slow
                "Your phone is currently using ${currentRam}% of available RAM and its temperature is ${currentTemp}°C (normal baseline is 36.0°C). Thermal stress and memory pressure are the primary causes of slowdown."
            }
        }

        val fullAnswer = """
            $explanationText

            Recommended Action: ${scenario.title} optimization (${matchedDoc.recommendedFix}).
        """.trimIndent()

        return ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = "AI",
            text = fullAnswer,
            timestamp = "Just now",
            ragSources = ragSources,
            actionText = "Fix Now",
            canTriggerFix = true
        )
    }
}
