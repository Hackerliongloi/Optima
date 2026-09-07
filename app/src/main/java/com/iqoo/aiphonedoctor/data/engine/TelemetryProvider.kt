package com.iqoo.aiphonedoctor.data.engine

import com.iqoo.aiphonedoctor.data.model.DemoScenario
import com.iqoo.aiphonedoctor.data.model.PhoneTelemetry
import kotlinx.coroutines.flow.Flow

interface TelemetryProvider {
    fun getCurrentTelemetry(): PhoneTelemetry
    fun getTelemetryStream(): Flow<PhoneTelemetry>
    fun switchScenario(scenario: DemoScenario)
    fun applySimulatedFix(): PhoneTelemetry
}
