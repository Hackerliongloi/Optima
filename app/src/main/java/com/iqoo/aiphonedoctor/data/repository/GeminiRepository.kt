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
    private var apiKey: String? = null
    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    fun setApiKey(key: String?) {
        this.apiKey = key?.trim()?.ifEmpty { null }
    }

    fun getApiKey(): String? = apiKey

    suspend fun generateExplanation(diagnosis: DiagnosisResult): String = withContext(Dispatchers.IO) {
        val currentKey = apiKey
        if (currentKey.isNullOrBlank()) {
            return@withContext diagnosis.aiExplanationText
        }

        try {
            val prompt = """
                You are AI Phone Doctor, an intelligent device diagnostics assistant on an iQOO smartphone.
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
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$currentKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseStr = response.body?.string()
                if (responseStr != null) {
                    val rootObj = JSONObject(responseStr)
                    val candidates = rootObj.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val content = candidates.getJSONObject(0).optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val text = parts.getJSONObject(0).optString("text", "")
                            if (text.isNotBlank()) {
                                return@withContext text.trim()
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Graceful fallback on network error, invalid key, or offline state
            e.printStackTrace()
        }

        return@withContext diagnosis.aiExplanationText
    }
}
