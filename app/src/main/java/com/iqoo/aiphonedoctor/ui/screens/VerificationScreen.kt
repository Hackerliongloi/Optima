package com.iqoo.aiphonedoctor.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.iqoo.aiphonedoctor.ui.theme.*
import com.iqoo.aiphonedoctor.ui.viewmodel.MainViewModel

@Composable
fun VerificationScreen(viewModel: MainViewModel) {
    val isFixing by viewModel.isFixing.collectAsState()
    val fixMessage by viewModel.fixProgressMessage.collectAsState()
    val diagnosis by viewModel.diagnosisResult.collectAsState()
    val currentTelemetry by viewModel.telemetry.collectAsState()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBlack)
            .padding(16.dp)
    ) {
        if (isFixing) {
            // Fix Simulation Progress
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(80.dp),
                    color = SuccessGreen,
                    strokeWidth = 6.dp,
                    trackColor = DarkCardBg
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = fixMessage,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Applying optimization & collecting post-fix telemetry...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        } else if (diagnosis != null) {
            // Verification Results
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Verified Banner Header
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(SuccessGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "✓", fontSize = 28.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Optimization Successful",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SuccessGreen,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Phone health restored & telemetry normalized",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // BEFORE / AFTER Comparison Grid
                Text(
                    text = "Before / After Telemetry Verification",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // BEFORE Card
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AlertRed.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "BEFORE FIX",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlertRed
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            MetricComparisonItem(
                                label = "Temperature",
                                value = "${diagnosis!!.beforeTelemetry.temperatureCelsius}°C",
                                color = AlertRed
                            )
                            MetricComparisonItem(
                                label = "Battery Drain",
                                value = "${diagnosis!!.beforeTelemetry.batteryDrainPerHour}% / hr",
                                color = AlertRed
                            )
                            MetricComparisonItem(
                                label = "CPU Load",
                                value = "${diagnosis!!.beforeTelemetry.cpuUsagePercent}%",
                                color = AlertRed
                            )
                        }
                    }

                    // AFTER Card
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "AFTER FIX",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            MetricComparisonItem(
                                label = "Temperature",
                                value = "${currentTelemetry.temperatureCelsius}°C",
                                color = SuccessGreen
                            )
                            MetricComparisonItem(
                                label = "Battery Drain",
                                value = "${currentTelemetry.batteryDrainPerHour}% / hr",
                                color = SuccessGreen
                            )
                            MetricComparisonItem(
                                label = "CPU Load",
                                value = "${currentTelemetry.cpuUsagePercent}%",
                                color = SuccessGreen
                            )
                        }
                    }
                }

                // AI Verification Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🤖", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI Verification",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentCyan
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Battery drain decreased by approximately 18% after the recommended intervention.",
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "✓ Issue Resolved & Health Restored",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }
                    }
                }

                // Action Buttons: Go Back & View History Log
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.selectTab(0) }, // Return to Home
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IqooOrange),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "← Go Back",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberBlack
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.selectTab(3) }, // View History Log (Tab 3)
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, IqooOrange)
                    ) {
                        Text(
                            text = "📜 History Log",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = IqooOrange
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MetricComparisonItem(label: String, value: String, color: Color) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, fontSize = 11.sp, color = TextMuted)
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
