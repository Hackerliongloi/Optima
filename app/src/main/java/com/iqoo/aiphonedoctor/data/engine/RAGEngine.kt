package com.iqoo.aiphonedoctor.data.engine

import com.iqoo.aiphonedoctor.data.model.*
import com.iqoo.aiphonedoctor.data.repository.GeminiRepository
import java.util.UUID

class RAGEngine(private val geminiRepository: GeminiRepository? = null) {

    suspend fun generateRAGResponse(
        userQuery: String,
        telemetry: PhoneTelemetry,
        scenario: DemoScenario,
        history: List<HistoryItem>
    ): ChatMessage {
        val queryLower = userQuery.lowercase()

        // 1. RETRIEVAL STEP 1: Knowledge Base Document Matching
        val matchedDoc = PhoneKnowledgeBase.documents.find { doc ->
            doc.keywords.any { keyword -> queryLower.contains(keyword) }
        } ?: PhoneKnowledgeBase.documents.first()

        // 2. RETRIEVAL STEP 2: Live Device Telemetry Context
        val currentTemp = telemetry.temperatureCelsius
        val currentRam = telemetry.ramUsagePercent
        val currentDrain = telemetry.batteryDrainPerHour
        val normalDrain = telemetry.normalBatteryDrainPerHour
        val activeApp = telemetry.activeApp
        val cpuUsage = telemetry.cpuUsagePercent
        val storageUsage = telemetry.storageUsagePercent

        // 3. Build RAG Evidence Sources
        val ragSources = mutableListOf<RAGSource>()

        ragSources.add(RAGSource("Retrieved KB Doc", matchedDoc.topic))
        ragSources.add(RAGSource("Current RAM", "$currentRam%"))
        ragSources.add(RAGSource("Device Temp", "${currentTemp}°C"))
        ragSources.add(RAGSource("Active App", activeApp))

        if (queryLower.contains("battery") || queryLower.contains("drain")) {
            ragSources.add(RAGSource("Current Drain Rate", "$currentDrain% / hr"))
            ragSources.add(RAGSource("Baseline Normal Drain", "$normalDrain% / hr"))
        }

        // 4. GENERATION STEP: Query Gemini API with RAG Prompt if key exists
        val hasApiKey = !geminiRepository?.getApiKey().isNullOrBlank()
        if (hasApiKey) {
            val ragPrompt = """
                You are Optima AI Doctor, an expert device diagnostics AI assistant on an iQOO flagship phone.
                Answer the user's question accurately using Retrieval-Augmented Generation (RAG).

                === RETRIEVED KNOWLEDGE BASE ===
                - Topic: ${matchedDoc.topic}
                - Diagnostic Pattern: ${matchedDoc.explanationPattern}
                - Knowledge Fix: ${matchedDoc.recommendedFix}

                === LIVE DEVICE TELEMETRY CONTEXT ===
                - Model: ${telemetry.deviceModel} (${telemetry.androidVersion})
                - Temperature: ${currentTemp}°C (Normal baseline: 36.0°C)
                - RAM Usage: ${currentRam}%
                - Battery Drain: ${currentDrain}%/hr (Normal baseline: ${normalDrain}%/hr)
                - Foreground App: ${activeApp}
                - CPU Load: ${cpuUsage}%
                - Free Storage Usage: ${storageUsage}%
                - Active Scenario: ${scenario.title}

                === USER QUESTION ===
                "${userQuery}"

                === MANDATORY FORMAT RULES ===
                - Do NOT combine points onto a single line. Every numbered point (1., 2., 3.) or bullet point MUST start on its OWN NEW LINE.
                - Do NOT output raw asterisk symbols on a single continuous block. Use clear line breaks (\n).

                🔍 **Root Cause**
                (1-2 clear sentences explaining the issue using telemetry evidence: ${currentTemp}°C temp, ${currentRam}% RAM, ${currentDrain}%/hr drain).

                📊 **Live Telemetry Evidence**
                • Temp: ${currentTemp}°C (Normal ~36°C)
                • RAM Usage: ${currentRam}%
                • Drain Rate: ${currentDrain}%/hr
                • Active App: ${activeApp}

                🛠️ **Recommended Action**
                1. Apply ${matchedDoc.topic} fix: ${matchedDoc.recommendedFix}
                2. Close background apps to reduce thermal stress.
            """.trimIndent()

            val geminiAnswer = geminiRepository?.generateRAGAnswer(ragPrompt)
            if (!geminiAnswer.isNullOrBlank()) {
                return ChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = "AI",
                    text = geminiAnswer,
                    timestamp = "Just now",
                    ragSources = ragSources,
                    actionText = "Fix Now",
                    canTriggerFix = true
                )
            }
        }

        // 5. Offline Fallback RAG Engine (Cleanly Structured)
        val causeExplanation = when {
            queryLower.contains("battery") || queryLower.contains("drain") -> {
                "Your battery is draining at ${currentDrain}%/hr (28% higher than your ${normalDrain}%/hr baseline) due to high background CPU processing by $activeApp while device temp is ${currentTemp}°C."
            }
            queryLower.contains("hot") || queryLower.contains("heat") || queryLower.contains("warm") -> {
                "Device temperature sensors report ${currentTemp}°C (normal baseline is 36.0°C). Simultaneous high CPU background workload and heavy foreground processing are generating thermal buildup."
            }
            queryLower.contains("fps") || queryLower.contains("bgmi") || queryLower.contains("game") || queryLower.contains("gaming") -> {
                "During your active $activeApp gaming session, device temperature reached ${currentTemp}°C with RAM at ${currentRam}%. Performance stability is at 91%, causing potential frame drops."
            }
            else -> {
                "Your phone is currently using ${currentRam}% of available RAM and its temperature is ${currentTemp}°C (normal baseline is 36.0°C). Memory pressure and thermal load are causing slowdowns."
            }
        }

        val structuredAnswer = """
            🔍 **Root Cause**
            $causeExplanation

            📊 **Live Telemetry Evidence**
            • Device Temperature: ${currentTemp}°C (Baseline ~36°C)
            • RAM Utilization: ${currentRam}%
            • Battery Drain Rate: ${currentDrain}%/hr
            • Active App: $activeApp

            🛠️ **Recommended Action**
            • Apply ${scenario.title} optimization: ${matchedDoc.recommendedFix}
        """.trimIndent()

        return ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = "AI",
            text = structuredAnswer,
            timestamp = "Just now",
            ragSources = ragSources,
            actionText = "Fix Now",
            canTriggerFix = true
        )
    }
}

