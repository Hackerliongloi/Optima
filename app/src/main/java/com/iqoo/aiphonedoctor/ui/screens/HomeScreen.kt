package com.iqoo.aiphonedoctor.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.iqoo.aiphonedoctor.data.model.DemoScenario
import com.iqoo.aiphonedoctor.ui.theme.*
import com.iqoo.aiphonedoctor.ui.viewmodel.MainViewModel

import android.app.Application
import android.provider.Settings

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AIPhoneDoctorTheme {
        HomeScreen(viewModel = MainViewModel(Application()))
    }
}

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val scenario by viewModel.currentScenario.collectAsState()
    val telemetry by viewModel.telemetry.collectAsState()
    val isVerified by viewModel.isVerified.collectAsState()
    val diagnosisResult by viewModel.diagnosisResult.collectAsState()
    val scrollState = rememberScrollState()

    val isDiagnosed = (diagnosisResult != null) || isVerified
    val healthScore = if (isVerified) 98 else scenario.initialHealthScore

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBlack)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Header Title (Clean Optima Title matching Web)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Optima",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
            Text(
                text = "⋮",
                fontSize = 20.sp,
                color = TextSecondary
            )
        }



        // Simulation Scenario Picker
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBg),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, DarkCardBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "⚡ SIMULATION SCENARIO SELECTOR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(DemoScenario.entries) { item ->
                        val isSelected = item == scenario
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) SuccessGreen else DarkSurface)
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

        // 1. Circular Health Gauge Component
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBg),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.4f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(SuccessGreen.copy(alpha = 0.15f), Color.Transparent),
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
                    Spacer(modifier = Modifier.height(14.dp))

                    // Inner Circular Gauge Frame
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(if (isVerified) SuccessGreen else if (isDiagnosed) WarningYellow else SuccessGreen)
                            .padding(10.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.White)
                        ) {
                            if (isDiagnosed) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = healthScore.toString(),
                                        fontSize = 44.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF0E1310)
                                    )
                                    Text(
                                        text = "pts",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4A564F)
                                    )
                                }
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "🩺",
                                        fontSize = 38.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "READY",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = when {
                            !isDiagnosed -> "Tap Diagnose to scan real-time phone health."
                            isVerified -> "✓ All issues fixed. System is in good condition."
                            else -> "⚠️ Issues detected. Optimization recommended."
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                }
            }
        }

        // Primary Dynamic Action CTA Button: Diagnose -> Optimise -> Re-scan
        val (buttonLabel, buttonOnClick) = when {
            isVerified -> "Re-scan Device" to { viewModel.startDiagnosis() }
            isDiagnosed -> "Optimise" to { viewModel.applyFix() }
            else -> "Diagnose" to { viewModel.startDiagnosis() }
        }

        Button(
            onClick = buttonOnClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
            shape = RoundedCornerShape(26.dp)
        ) {
            Text(
                text = buttonLabel,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0E1310)
            )
        }

        // 2. ASK OPTIMA AI Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.selectTab(2) }, // Navigate to AI Doctor Chat (Tab 2)
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Text(text = "🤖", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "ASK OPTIMA AI",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Wanna talk about your phone issue?",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SuccessGreen.copy(alpha = 0.18f))
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = "Chat →",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                }
            }
        }

        // 3. Gaming Health Component
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBg),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DarkCardBorder)
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
                            text = "54 FPS vs 60 FPS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = WarningYellow
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

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

        // 4. 2x2 Interactive Feature Grid
        var showStorageDialog by remember { mutableStateOf(false) }
        var showVirusDialog by remember { mutableStateOf(false) }
        var showBoostDialog by remember { mutableStateOf(false) }
        var showAppDialog by remember { mutableStateOf(false) }

        var isStorageCleaned by remember { mutableStateOf(false) }
        var isVirusScanned by remember { mutableStateOf(false) }
        var isSystemBoosted by remember { mutableStateOf(false) }
        var isAppsRestricted by remember { mutableStateOf(false) }

        if (showStorageDialog) {
            AlertDialog(
                onDismissRequest = { showStorageDialog = false },
                title = { Text(text = "🗑️ Storage Cleanup", fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = if (isStorageCleaned) "Storage optimized! Free space: 188 GB available." else "Deep scan found 14.2 GB of safe-to-delete files.",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "📦 App & System Cache: 3.4 GB", fontSize = 12.sp, color = TextMuted)
                        Text(text = "🖼️ Duplicate Media: 8.0 GB", fontSize = 12.sp, color = TextMuted)
                        Text(text = "📑 Installation Packages: 2.8 GB", fontSize = 12.sp, color = TextMuted)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            isStorageCleaned = true
                            showStorageDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Text(text = if (isStorageCleaned) "Done" else "Clean 14.2 GB Junk", color = CyberBlack, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = DarkCardBg
            )
        }

        if (showVirusDialog) {
            AlertDialog(
                onDismissRequest = { showVirusDialog = false },
                title = { Text(text = "🛡️ Viruses & Risks", fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Text(
                        text = if (isVirusScanned) "✓ Security scan passed! 0 threats detected across 124 installed app packages." else "Scan 124 installed apps & system packages for malware, scam links, and security risks.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            isVirusScanned = true
                            showVirusDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Text(text = if (isVirusScanned) "Done" else "Run Security Scan", color = CyberBlack, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = DarkCardBg
            )
        }

        if (showBoostDialog) {
            AlertDialog(
                onDismissRequest = { showBoostDialog = false },
                title = { Text(text = "⚡ System Boost", fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = if (isSystemBoosted) "✓ System boosted! Cleared 2.1 GB RAM and closed 12 redundant background threads." else "Clear RAM and terminate unnecessary background app threads to optimize system speed.",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            isSystemBoosted = true
                            viewModel.applyFix()
                            showBoostDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Text(text = if (isSystemBoosted) "Done" else "Boost Now", color = CyberBlack, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = DarkCardBg
            )
        }

        if (showAppDialog) {
            AlertDialog(
                onDismissRequest = { showAppDialog = false },
                title = { Text(text = "📱 App Management", fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Active background power consumers:", color = TextSecondary, fontSize = 13.sp)
                        Text(text = "📷 Instagram - 62% background drain", color = AlertRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(text = "🎮 Active Game - 24% thermal load", color = WarningYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            isAppsRestricted = true
                            showAppDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Text(text = if (isAppsRestricted) "Done" else "Restrict Background Power", color = CyberBlack, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = DarkCardBg
            )
        }

        // 2x2 Grid Rows
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showStorageDialog = true },
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "🗑️", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Storage Cleanup", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(
                        text = "${String.format("%.1f", (telemetry.totalStorageGB - telemetry.availableStorageGB).coerceAtLeast(0.0))} / ${String.format("%.1f", telemetry.totalStorageGB)} GB",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showVirusDialog = true },
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "🛡️", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Viruses & Risks", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(
                        text = if (isVirusScanned || isVerified) "Clean & protected" else "Scanned 5 days ago",
                        fontSize = 11.sp,
                        color = if (isVirusScanned || isVerified) SuccessGreen else AlertRed
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showBoostDialog = true },
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "⚡", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "System Boost", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(
                        text = if (isSystemBoosted || isVerified) "RAM Boosted" else "Clear RAM & apps",
                        fontSize = 11.sp,
                        color = if (isSystemBoosted || isVerified) SuccessGreen else TextMuted
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showAppDialog = true },
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "📱", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "App Management", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(
                        text = if (isAppsRestricted || isVerified) "All apps optimized" else "View app power usage",
                        fontSize = 11.sp,
                        color = if (isAppsRestricted || isVerified) SuccessGreen else AlertRed
                    )
                }
            }
        }

        // 5. Device Telemetry Health Cards
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
