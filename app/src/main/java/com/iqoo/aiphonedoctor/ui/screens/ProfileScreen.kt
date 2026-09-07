package com.iqoo.aiphonedoctor.ui.screens

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.aiphonedoctor.ui.theme.*
import com.iqoo.aiphonedoctor.ui.viewmodel.MainViewModel

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    AIPhoneDoctorTheme {
        ProfileScreen(viewModel = MainViewModel(Application()))
    }
}

@Composable
fun ProfileScreen(viewModel: MainViewModel) {
    val telemetry by viewModel.telemetry.collectAsState()
    val scrollState = rememberScrollState()

    val usedRamGB = String.format("%.1f", (telemetry.totalRamGB - telemetry.availableRamGB).coerceAtLeast(0.0))
    val totalRamGB = String.format("%.1f", telemetry.totalRamGB)
    
    val usedStorageGB = String.format("%.1f", (telemetry.totalStorageGB - telemetry.availableStorageGB).coerceAtLeast(0.0))
    val totalStorageGB = String.format("%.1f", telemetry.totalStorageGB)

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
                text = "SYSTEM HARDWARE & TELEMETRY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = IqooOrange
            )
            Text(
                text = "Real Device Specs",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
        }

        // Profile Card: Real Hardware & Telemetry Specs
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCardBg),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, IqooOrange.copy(alpha = 0.3f))
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
                        Text(text = "📱", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = telemetry.deviceModel,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = telemetry.androidVersion,
                            fontSize = 12.sp,
                            color = AccentCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = DarkCardBorder)
                Spacer(modifier = Modifier.height(14.dp))

                ProfileDetailRow(icon = "📱", label = "Device Model", value = telemetry.deviceModel)
                ProfileDetailRow(icon = "⚙️", label = "Manufacturer & Brand", value = "${telemetry.manufacturer} (${telemetry.brand})")
                ProfileDetailRow(icon = "🤖", label = "Android OS", value = telemetry.androidVersion)
                ProfileDetailRow(icon = "🧠", label = "SoC Hardware", value = telemetry.chipsetHardware)
                ProfileDetailRow(icon = "⚡", label = "Real System RAM", value = "$usedRamGB / $totalRamGB GB (${telemetry.ramUsagePercent}%)")
                ProfileDetailRow(icon = "💾", label = "Internal Storage", value = "$usedStorageGB / $totalStorageGB GB (${telemetry.storageUsagePercent}%)")

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "SYSTEM TELEMETRY ENGINE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = IqooOrange
                )
                Spacer(modifier = Modifier.height(8.dp))

                ProfileDetailRow(
                    icon = "📁",
                    label = "Storage Analytics",
                    value = "Active ✓"
                )
                ProfileDetailRow(
                    icon = "📞",
                    label = "Hardware Telephony State",
                    value = "Active ✓"
                )
                ProfileDetailRow(
                    icon = "📊",
                    label = "Memory & Thermal Sensor",
                    value = "Active ✓"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurface)
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text(text = "🛡️", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Optima dynamically reads your device hardware signals (${telemetry.deviceModel}) to analyze thermal thresholds, storage pressure, and RAM allocation.",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // On-Device Telemetry Privacy Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, DarkCardBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🔒", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "On-Device Hardware Privacy",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "All device sensor readings, storage analytics, and system metrics are processed locally on your phone without asking for user permissions.",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun ProfileDetailRow(
    icon: String,
    label: String,
    value: String
) {
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
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}
