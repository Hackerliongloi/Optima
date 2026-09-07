package com.iqoo.aiphonedoctor.data.model

data class RAGSource(
    val title: String,
    val value: String
)

data class ChatMessage(
    val id: String,
    val sender: String, // "USER" or "AI"
    val text: String,
    val timestamp: String,
    val ragSources: List<RAGSource> = emptyList(),
    val actionText: String? = null,
    val canTriggerFix: Boolean = false,
    val isTyping: Boolean = false
)
