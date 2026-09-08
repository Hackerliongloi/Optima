package com.iqoo.aiphonedoctor.data.repository

import com.iqoo.aiphonedoctor.data.model.DiagnosisResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiRepository {
    private var apiKey: String? = System.getenv("GEMINI_API_KEY")?.trim()?.ifEmpty { null }
        ?: System.getProperty("GEMINI_API_KEY")?.trim()?.ifEmpty { null }

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    fun setApiKey(key: String?) {
        this.apiKey = key?.trim()?.ifEmpty { null }
    }

    fun getApiKey(): String? = apiKey

    fun hasApiKey(): Boolean = !apiKey.isNullOrBlank()

    fun getMaskedApiKey(): String {
        val key = apiKey ?: return "Not Configured"
        if (key.length <= 8) return "••••••••"
        return key.take(4) + "••••••••" + key.takeLast(4)
    }

    suspend fun generateExplanation(diagnosis: DiagnosisResult): String = withContext(Dispatchers.IO) {
        val currentKey = apiKey
        if (currentKey.isNullOrBlank()) {
            return@withContext diagnosis.aiExplanationText
        }

        try {
            val prompt = """
                You are Optima, an intelligent device diagnostics assistant on an iQOO smartphone.
                Summarize this phone diagnosis in 2 concise, clear, human-readable sentences for the user:
                - Issue: ${diagnosis.issueTitle}
                - Primary Cause: ${diagnosis.primaryCause} (${diagnosis.confidencePercent}%)
                - Telemetry: Temp ${diagnosis.beforeTelemetry.temperatureCelsius}°C, Battery Drain ${diagnosis.beforeTelemetry.batteryDrainPerHour}%/hr, CPU ${diagnosis.beforeTelemetry.cpuUsagePercent}%
                - Recommendation: ${diagnosis.recommendedActionTitle}
                Keep it direct, helpful, and under 35 words.
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("maxOutputTokens", 220)
                    put("temperature", 0.2)
                })
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
            val modelsToTry = listOf("gemini-3.5-flash-lite", "gemini-3.5-flash", "gemini-3.6-flash")

            for (model in modelsToTry) {
                try {
                    val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent"
                    val request = Request.Builder()
                        .url(url)
                        .addHeader("x-goog-api-key", currentKey)
                        .post(requestBody)
                        .build()

                    val response = client.newCall(request).execute()
                    if (response.isSuccessful) {
                        val responseStr = response.body?.string()
                        if (responseStr != null) {
                            val text = parseGeminiResponseText(responseStr)
                            if (!text.isNullOrBlank()) {
                                return@withContext text
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Try next model
                }
            }
        } catch (e: Exception) {
            // Log generic exception without exposing key
        }

        return@withContext diagnosis.aiExplanationText
    }

    suspend fun generateRAGAnswer(prompt: String): String? = withContext(Dispatchers.IO) {
        val currentKey = apiKey
        if (currentKey.isNullOrBlank()) return@withContext null

        try {
            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("maxOutputTokens", 220)
                    put("temperature", 0.2)
                })
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
            val modelsToTry = listOf("gemini-3.5-flash-lite", "gemini-3.5-flash", "gemini-3.6-flash")

            for (model in modelsToTry) {
                try {
                    val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent"
                    val request = Request.Builder()
                        .url(url)
                        .addHeader("x-goog-api-key", currentKey)
                        .post(requestBody)
                        .build()

                    val response = client.newCall(request).execute()
                    if (response.isSuccessful) {
                        val responseStr = response.body?.string()
                        if (responseStr != null) {
                            val text = parseGeminiResponseText(responseStr)
                            if (!text.isNullOrBlank()) return@withContext text
                        }
                    }
                } catch (e: Exception) {
                    // Try next model
                }
            }
        } catch (e: Exception) {
            // Log generic exception without exposing key
        }

        return@withContext null
    }

    private fun parseGeminiResponseText(jsonStr: String): String? {
        try {
            val rootObj = JSONObject(jsonStr)
            val candidates = rootObj.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val content = candidates.getJSONObject(0).optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    val text = parts.getJSONObject(0).optString("text", "")
                    if (text.isNotBlank()) {
                        return text.trim()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

