package com.iqoo.aiphonedoctor.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.iqoo.aiphonedoctor.data.model.CauseBreakdownItem
import com.iqoo.aiphonedoctor.ui.theme.*
import com.iqoo.aiphonedoctor.ui.viewmodel.MainViewModel

@Composable
fun DiagnosisScreen(viewModel: MainViewModel) {
    val isScanning by viewModel.isScanning.collectAsState()
    val scanMessage by viewModel.scanMessage.collectAsState()
    val scanProgress by viewModel.scanProgress.collectAsState()
    val diagnosis by viewModel.diagnosisResult.collectAsState()
    val aiExplanation by viewModel.aiExplanation.collectAsState()
    val isFixing by viewModel.isFixing.collectAsState()
    val isVerified by viewModel.isVerified.collectAsState()
    val scrollState = rememberScrollState()

    if (isFixing || isVerified) {
        VerificationScreen(viewModel = viewModel)
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBlack)
            .padding(16.dp)
    ) {
        if (isScanning) {
            // Scanning Animation View
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    progress = { scanProgress },
                    modifier = Modifier.size(100.dp),
                    color = IqooOrange,
                    strokeWidth = 8.dp,
                    trackColor = DarkCardBg
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = scanMessage,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Optima is analyzing device telemetry signals...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        } else if (diagnosis != null) {
            // Diagnosis Results View
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AlertRed.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AlertRed.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "⚠️", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = diagnosis!!.issueTitle,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AlertRed
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = diagnosis!!.issueDescription,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                    }
                }

                // Root Cause Visualization
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
                            Text(
                                text = "Likely Causes",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimary
                            )
                            Text(
                                text = "Correlated AI Analysis",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = IqooOrange
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        diagnosis!!.causeBreakdown.forEach { cause ->
                            CauseProgressBar(cause = cause)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }

                // AI Explanation Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🧠", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI Explanation",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentCyan
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (aiExplanation.isNotBlank()) aiExplanation else diagnosis!!.aiExplanationText,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = TextPrimary
                        )
                    }
                }

                // Recommendation & Action Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IqooOrange.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Recommended Action",
                            style = MaterialTheme.typography.labelMedium,
                            color = IqooOrange
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = diagnosis!!.recommendedActionTitle,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = diagnosis!!.recommendedActionReason,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { viewModel.applyFix() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IqooOrange),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Fix Now",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberBlack
                                )
                            }

                            OutlinedButton(
                                onClick = { viewModel.selectTab(0) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
                            ) {
                                Text(
                                    text = "Not Now",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Initial state before triggering diagnosis
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "🩺", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ready to Diagnose",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap below to run multi-signal AI telemetry diagnostic.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { viewModel.startDiagnosis() },
                    colors = ButtonDefaults.buttonColors(containerColor = IqooOrange),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Start AI Scan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberBlack
                    )
                }
            }
        }
    }
}

@Composable
fun CauseProgressBar(cause: CauseBreakdownItem) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = cause.icon, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = cause.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            }
            Text(
                text = "${cause.percentage}%",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = IqooOrange
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { cause.percentage / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = if (cause.percentage > 50) IqooOrange else AccentCyan,
            trackColor = DarkSurface
        )
    }
}
