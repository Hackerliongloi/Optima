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
import com.iqoo.aiphonedoctor.data.model.DemoScenario
import com.iqoo.aiphonedoctor.ui.theme.*
import com.iqoo.aiphonedoctor.ui.viewmodel.MainViewModel

@Composable
fun ProfileScreen(viewModel: MainViewModel) {
    val currentScenario by viewModel.currentScenario.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBlack)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Column {
            Text(
                text = "PERSONALIZATION BASELINE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = IqooOrange
            )
            Text(
                text = "Device & User Profile",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
        }

        // Profile Card: AI Personal Baseline
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBg),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, IqooOrange.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(IqooOrange.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👤", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "iQOO 13 Flagship",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Personalized Baseline Profile",
                            fontSize = 12.sp,
                            color = AccentCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = DarkCardBorder)
                Spacer(modifier = Modifier.height(14.dp))

                ProfileDetailRow(icon = "📱", label = "Device Model", value = "iQOO 13 (16GB RAM)")
                ProfileDetailRow(icon = "🎮", label = "Usage Pattern", value = "Heavy Gaming & Multitasking")
                ProfileDetailRow(icon = "🔋", label = "Battery Habit", value = "Frequent Fast Charging")
                ProfileDetailRow(icon = "⚡", label = "Performance Mode", value = "Balanced / Monster Mode")
                ProfileDetailRow(icon = "🎯", label = "Primary Gaming App", value = "Game (60 FPS Ultra)")
                ProfileDetailRow(icon = "⏱️", label = "Typical Screen Time", value = "6.5 hours / day")
                ProfileDetailRow(icon = "📊", label = "Normal Drain Baseline", value = "6.4% / hour")

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurface)
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text(text = "🧠", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Why this matters: AI Phone Doctor compares your real-time telemetry against this baseline. Today's 8.2%/hr drain is flagged because it exceeds your normal 6.4%/hr pattern by 28%.",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Privacy & Security Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🔒", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "On-Device Telemetry Privacy",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "All baseline comparisons and multi-signal correlations are computed locally. No private app logs or personal data leave your phone.",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = TextSecondary
                )
            }
        }

        // Supporting Infrastructure: Judge Scenario Selector
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBg),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "⚙️ Simulation Scenario Selector",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = IqooOrange
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Switch active telemetry scenario for testing:",
                    fontSize = 12.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(10.dp))

                DemoScenario.values().forEach { scenario ->
                    val isSelected = scenario == currentScenario
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = scenario.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) IqooOrange else TextPrimary
                        )
                        RadioButton(
                            selected = isSelected,
                            onClick = { viewModel.updateScenario(scenario) },
                            colors = RadioButtonDefaults.colors(selectedColor = IqooOrange)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileDetailRow(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontSize = 13.sp, color = TextSecondary)
        }
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
    }
}
