package com.iqoo.aiphonedoctor.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.aiphonedoctor.data.model.DemoScenario
import com.iqoo.aiphonedoctor.ui.theme.*
import com.iqoo.aiphonedoctor.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val scenario by viewModel.currentScenario.collectAsState()
    val telemetry by viewModel.telemetry.collectAsState()
    val isVerified by viewModel.isVerified.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBlack)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(DarkCardBg)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(SuccessGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE TELEMETRY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                }
            }
        }

        // Simulation Scenario Picker
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBg),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "⚡ SIMULATION SCENARIO SELECTOR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = IqooOrange
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(DemoScenario.values()) { item ->
                        val isSelected = item == scenario
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) IqooOrange else DarkSurface)
                                .clickable { viewModel.updateScenario(item) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = item.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) CyberBlack else TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Health Score Card
        val healthScore = if (isVerified) 98 else scenario.initialHealthScore
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBg),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, IqooOrange.copy(alpha = 0.4f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(IqooOrange.copy(alpha = 0.15f), Color.Transparent),
                            radius = 400f
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Phone Health Score",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Circular Health Score Meter
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(130.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { healthScore / 100f },
                            modifier = Modifier.fillMaxSize(),
                            color = if (healthScore > 80) IqooOrange else WarningYellow,
                            strokeWidth = 10.dp,
                            trackColor = DarkSurface
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$healthScore",
                                fontSize = 42.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "/ 100",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isVerified) SuccessGreen.copy(alpha = 0.15f) else AlertRed.copy(alpha = 0.15f))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = if (isVerified) SuccessGreen else AlertRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isVerified) "✓ All issues optimized" else "2 issues detected",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isVerified) SuccessGreen else AlertRed
                        )
                    }
                }
            }
        }

        // Primary Action CTA: Diagnose My Phone
        Button(
            onClick = { viewModel.startDiagnosis() },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IqooOrange),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🩺", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Diagnose My Phone",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberBlack
                )
            }
        }

        // Prominent Ask AI Doctor Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.selectTab(2) }, // Navigate to AI Doctor Chat
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Text(text = "💬", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "ASK AI DOCTOR CHATBOT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentCyan
                        )
                        Text(
                            text = "\"Why is my phone lagging?\"",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AccentCyan.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(text = "Chat →", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                }
            }
        }

        // Section 15: Gaming Health Card (iQOO Prototype Feature)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBg),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🎮", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Gaming Health",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(WarningYellow.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "54 FPS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = WarningYellow
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    GamingMetricItem("Temp", "${telemetry.temperatureCelsius}°C")
                    GamingMetricItem("RAM", "${telemetry.ramUsagePercent}%")
                    GamingMetricItem("Battery Drain", "${telemetry.batteryDrainPerHour}%/hr")
                    GamingMetricItem("Stability", "${telemetry.performanceStabilityScore}%")
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "⚠️ Performance may decrease if temperature continues rising.",
                    fontSize = 12.sp,
                    color = WarningYellow
                )
            }
        }

        // Telemetry Metrics Grid Pills
        Text(
            text = "Device Health Telemetry",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HealthMetricPill(
                modifier = Modifier.weight(1f),
                icon = "🔋",
                title = "Battery",
                value = "${telemetry.batteryLevelPercent}%",
                status = "Good",
                statusColor = SuccessGreen
            )
            HealthMetricPill(
                modifier = Modifier.weight(1f),
                icon = "🌡️",
                title = "Thermal",
                value = "${telemetry.temperatureCelsius}°C",
                status = if (telemetry.temperatureCelsius > 42) "Warning" else "Good",
                statusColor = if (telemetry.temperatureCelsius > 42) WarningYellow else SuccessGreen
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HealthMetricPill(
                modifier = Modifier.weight(1f),
                icon = "⚡",
                title = "Performance",
                value = "${telemetry.cpuUsagePercent}% CPU",
                status = "Good",
                statusColor = SuccessGreen
            )
            HealthMetricPill(
                modifier = Modifier.weight(1f),
                icon = "💾",
                title = "Storage",
                value = "${telemetry.storageUsagePercent}%",
                status = if (telemetry.storageUsagePercent > 90) "Warning" else "Good",
                statusColor = if (telemetry.storageUsagePercent > 90) WarningYellow else SuccessGreen
            )
            HealthMetricPill(
                modifier = Modifier.weight(1f),
                icon = "📶",
                title = "Network",
                value = telemetry.networkQuality,
                status = if (telemetry.networkQuality == "Poor") "Fair" else "Good",
                statusColor = if (telemetry.networkQuality == "Poor") WarningYellow else SuccessGreen
            )
        }
    }
}

@Composable
fun GamingMetricItem(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 11.sp, color = TextMuted)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
    }
}

@Composable
fun HealthMetricPill(
    modifier: Modifier = Modifier,
    icon: String,
    title: String,
    value: String,
    status: String,
    statusColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardBg),
        border = BorderStroke(1.dp, DarkCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = icon, fontSize = 14.sp)
                Text(text = title, fontSize = 11.sp, color = TextMuted)
            }
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = statusColor.copy(alpha = 0.15f)
            ) {
                Text(
                    text = status,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = statusColor,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
