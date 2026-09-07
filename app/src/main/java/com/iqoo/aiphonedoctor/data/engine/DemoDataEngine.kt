package com.iqoo.aiphonedoctor.data.engine

import com.iqoo.aiphonedoctor.data.model.DemoScenario
import com.iqoo.aiphonedoctor.data.model.PhoneTelemetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DemoDataEngine : TelemetryProvider {
    private var currentScenario: DemoScenario = DemoScenario.BATTERY_DRAIN
    private var isFixed: Boolean = false

    private val _telemetryState = MutableStateFlow(currentScenario.initialTelemetry)
    val telemetryState: Flow<PhoneTelemetry> = _telemetryState.asStateFlow()

    override fun getCurrentTelemetry(): PhoneTelemetry {
        return if (isFixed) currentScenario.improvedTelemetry else currentScenario.initialTelemetry
    }

    override fun getTelemetryStream(): Flow<PhoneTelemetry> = telemetryState

    override fun switchScenario(scenario: DemoScenario) {
        this.currentScenario = scenario
        this.isFixed = false
        _telemetryState.value = scenario.initialTelemetry
    }

    override fun applySimulatedFix(): PhoneTelemetry {
        this.isFixed = true
        _telemetryState.value = currentScenario.improvedTelemetry
        return currentScenario.improvedTelemetry
    }

    fun getCurrentScenario(): DemoScenario = currentScenario
    fun isScenarioFixed(): Boolean = isFixed
}
