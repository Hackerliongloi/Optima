package com.iqoo.aiphonedoctor.data.model

data class KnowledgeDocument(
    val topic: String,
    val keywords: List<String>,
    val explanationPattern: String,
    val recommendedFix: String
)

object PhoneKnowledgeBase {
    val documents = listOf(
        KnowledgeDocument(
            topic = "lag_and_performance",
            keywords = listOf("lag", "slow", "stutter", "hanging", "freeze", "performance"),
            explanationPattern = "Your phone is currently experiencing high RAM utilization (%RAM%) and elevated thermal temperature (%TEMP%°C). Normal baseline is %BASELINE_TEMP%°C.",
            recommendedFix = "Switch to Balanced Thermal Profile and restrict heavy background threads."
        ),
        KnowledgeDocument(
            topic = "battery_drain",
            keywords = listOf("battery", "drain", "draining", "power", "charge", "dying"),
            explanationPattern = "Your battery drain is %DRAIN%%/hr, which is 28% higher than your normal %NORMAL_DRAIN%%/hr baseline. %APP% is consuming excessive background CPU cycles.",
            recommendedFix = "Restrict Instagram background activity."
        ),
        KnowledgeDocument(
            topic = "heating_and_thermal",
            keywords = listOf("hot", "heat", "warm", "thermal", "temperature", "overheating"),
            explanationPattern = "Internal sensors report %TEMP%°C, exceeding your normal %BASELINE_TEMP%°C evening baseline. Simultaneous gaming and charging are generating compound thermal output.",
            recommendedFix = "Switch to Balanced Performance Mode."
        ),
        KnowledgeDocument(
            topic = "gaming_and_fps",
            keywords = listOf("gaming", "bgmi", "fps", "frame", "cod", "game"),
            explanationPattern = "During your active BGMI session, GPU thermal output has reached %TEMP%°C with RAM usage at %RAM%. FPS stability has dropped to %FPS% FPS.",
            recommendedFix = "Activate iQOO Game Ultra Balanced Mode."
        )
    )
}
