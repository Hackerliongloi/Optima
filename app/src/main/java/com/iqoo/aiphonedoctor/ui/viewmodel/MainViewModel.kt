package com.iqoo.aiphonedoctor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iqoo.aiphonedoctor.data.engine.DemoDataEngine
import com.iqoo.aiphonedoctor.data.engine.DiagnosisEngine
import com.iqoo.aiphonedoctor.data.engine.RAGEngine
import com.iqoo.aiphonedoctor.data.model.ChatMessage
import com.iqoo.aiphonedoctor.data.model.DemoScenario
import com.iqoo.aiphonedoctor.data.model.DiagnosisResult
import com.iqoo.aiphonedoctor.data.model.HistoryItem
import com.iqoo.aiphonedoctor.data.model.PhoneTelemetry
import com.iqoo.aiphonedoctor.data.repository.GeminiRepository
import com.iqoo.aiphonedoctor.data.repository.HistoryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel : ViewModel() {
    private val demoEngine = DemoDataEngine()
    private val diagnosisEngine = DiagnosisEngine()
    private val ragEngine = RAGEngine()
    private val geminiRepo = GeminiRepository()
    private val historyRepo = HistoryRepository()

    private val _currentScenario = MutableStateFlow(DemoScenario.BATTERY_DRAIN)
    val currentScenario: StateFlow<DemoScenario> = _currentScenario.asStateFlow()

    private val _telemetry = MutableStateFlow(demoEngine.getCurrentTelemetry())
    val telemetry: StateFlow<PhoneTelemetry> = _telemetry.asStateFlow()

    private val _selectedTab = MutableStateFlow(0) // 0=Home, 1=Diagnose, 2=AI Doctor Chat, 3=History, 4=Profile
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _scanMessage = MutableStateFlow("Initializing Telemetry Scan...")
    val scanMessage: StateFlow<String> = _scanMessage.asStateFlow()

    private val _scanProgress = MutableStateFlow(0f)
    val scanProgress: StateFlow<Float> = _scanProgress.asStateFlow()

    private val _diagnosisResult = MutableStateFlow<DiagnosisResult?>(null)
    val diagnosisResult: StateFlow<DiagnosisResult?> = _diagnosisResult.asStateFlow()

    private val _isFixing = MutableStateFlow(false)
    val isFixing: StateFlow<Boolean> = _isFixing.asStateFlow()

    private val _fixProgressMessage = MutableStateFlow("Applying recommended action...")
    val fixProgressMessage: StateFlow<String> = _fixProgressMessage.asStateFlow()

    private val _isVerified = MutableStateFlow(false)
    val isVerified: StateFlow<Boolean> = _isVerified.asStateFlow()

    val historyList: StateFlow<List<HistoryItem>> = historyRepo.historyItems

    private val _aiExplanation = MutableStateFlow("")
    val aiExplanation: StateFlow<String> = _aiExplanation.asStateFlow()

    private val _geminiApiKey = MutableStateFlow("")
    val geminiApiKey: StateFlow<String> = _geminiApiKey.asStateFlow()

    // Chatbot State
    private val initialGreeting = ChatMessage(
        id = "welcome-1",
        sender = "AI",
        text = "Hello! I am your AI Phone Doctor. I continuously monitor your iQOO device telemetry to detect thermal stress, battery drain, and memory pressure. How can I help you today?",
        timestamp = "Just now"
    )

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(listOf(initialGreeting))
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatTyping = MutableStateFlow(false)
    val isChatTyping: StateFlow<Boolean> = _isChatTyping.asStateFlow()

    val suggestedQuestions = listOf(
        "Why is my phone lagging?",
        "Why is my battery draining so fast?",
        "Why is my phone getting hot?",
        "Why is the game dropping FPS?"
    )

    init {
        updateScenario(DemoScenario.BATTERY_DRAIN)
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun updateScenario(scenario: DemoScenario) {
        _currentScenario.value = scenario
        demoEngine.switchScenario(scenario)
        _telemetry.value = demoEngine.getCurrentTelemetry()
        _diagnosisResult.value = null
        _isVerified.value = false
        _isScanning.value = false
    }

    fun startDiagnosis() {
        viewModelScope.launch {
            _selectedTab.value = 1 // Switch to Diagnose tab
            _isScanning.value = true
            _isVerified.value = false
            _diagnosisResult.value = null

            val scanSteps = listOf(
                "Analyzing battery drain patterns...",
                "Checking thermal sensor telemetry...",
                "Analyzing background app activity...",
                "Evaluating cellular & network quality...",
                "Running multi-signal correlation matrix...",
                "Finding root cause..."
            )

            for (i in scanSteps.indices) {
                _scanMessage.value = scanSteps[i]
                _scanProgress.value = (i + 1) / scanSteps.size.toFloat()
                delay(350)
            }

            val diagnosis = diagnosisEngine.analyze(_currentScenario.value, _telemetry.value)
            _diagnosisResult.value = diagnosis

            // Generate AI explanation via Gemini (with instant offline fallback)
            val explanation = geminiRepo.generateExplanation(diagnosis)
            _aiExplanation.value = explanation

            _isScanning.value = false
        }
    }

    fun applyFix() {
        viewModelScope.launch {
            _isFixing.value = true

            val fixSteps = listOf(
                "Applying recommended action...",
                "Restricting background process threads...",
                "Monitoring thermal & power draw stabilization...",
                "Comparing health trends..."
            )

            for (step in fixSteps) {
                _fixProgressMessage.value = step
                delay(450)
            }

            val updatedTelemetry = demoEngine.applySimulatedFix()
            _telemetry.value = updatedTelemetry
            _isFixing.value = false
            _isVerified.value = true

            val diag = _diagnosisResult.value
            if (diag != null) {
                val savingsStr = if (_currentScenario.value == DemoScenario.BATTERY_DRAIN) {
                    "-1.5% / hr drain"
                } else if (_currentScenario.value == DemoScenario.THERMAL_STRESS) {
                    "-6.3°C temp"
                } else if (_currentScenario.value == DemoScenario.GAMING_PERFORMANCE) {
                    "+7% stability"
                } else {
                    "+16 GB free"
                }

                historyRepo.addHistoryRecord(
                    issueTitle = diag.issueTitle,
                    cause = diag.primaryCause,
                    actionTaken = diag.recommendedActionTitle,
                    savings = savingsStr
                )
            }
        }
    }

    fun sendChatMessage(userText: String) {
        if (userText.isBlank()) return

        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = "USER",
            text = userText.trim(),
            timestamp = "Just now"
        )

        _chatMessages.value = _chatMessages.value + userMsg
        _isChatTyping.value = true

        viewModelScope.launch {
            delay(800) // Simulate AI thinking / RAG retrieval delay

            val aiMsg = ragEngine.generateRAGResponse(
                userQuery = userText,
                telemetry = _telemetry.value,
                scenario = _currentScenario.value,
                history = historyRepo.historyItems.value
            )

            _isChatTyping.value = false
            _chatMessages.value = _chatMessages.value + aiMsg
        }
    }

    fun triggerFixFromChat() {
        // Ensure diagnosis object exists first
        if (_diagnosisResult.value == null) {
            _diagnosisResult.value = diagnosisEngine.analyze(_currentScenario.value, _telemetry.value)
        }
        _selectedTab.value = 1 // Go to Diagnose screen to view Verification
        applyFix()
    }

    fun clearChat() {
        _chatMessages.value = listOf(initialGreeting)
    }

    fun setGeminiApiKey(key: String) {
        _geminiApiKey.value = key
        geminiRepo.setApiKey(key)
    }
}
