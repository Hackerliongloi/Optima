package com.iqoo.aiphonedoctor.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.aiphonedoctor.R
import com.iqoo.aiphonedoctor.ui.screens.AIDoctorChatScreen
import com.iqoo.aiphonedoctor.ui.screens.DiagnosisScreen
import com.iqoo.aiphonedoctor.ui.screens.HistoryScreen
import com.iqoo.aiphonedoctor.ui.screens.HomeScreen
import com.iqoo.aiphonedoctor.ui.screens.ProfileScreen
import com.iqoo.aiphonedoctor.ui.theme.CyberBlack
import com.iqoo.aiphonedoctor.ui.theme.DarkCardBg
import com.iqoo.aiphonedoctor.ui.theme.IqooOrange
import com.iqoo.aiphonedoctor.ui.theme.TextMuted
import com.iqoo.aiphonedoctor.ui.viewmodel.MainViewModel

data class NavItem(val label: String, val icon: ImageVector, val isCenter: Boolean = false)

@Composable
fun MainAppContainer(viewModel: MainViewModel) {
    val selectedTab by viewModel.selectedTab.collectAsState()

    // 5 Core Tabs: Home, Diagnose, AI Doctor Chat, History, Profile
    val navItems = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Diagnose", Icons.Default.MedicalServices),
        NavItem("AI Doctor", Icons.Default.SmartToy, isCenter = true),
        NavItem("History", Icons.Default.History),
        NavItem("Profile", Icons.Default.Person)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = DarkCardBg,
                tonalElevation = 8.dp,
                modifier = Modifier.height(72.dp)
            ) {
                navItems.forEachIndexed { index, item ->
                    val isSelected = selectedTab == index

                    if (item.isCenter) {
                        // Prominent Center AI Doctor Button with Optima Logo
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) IqooOrange else IqooOrange.copy(alpha = 0.85f))
                                    .border(2.dp, CyberBlack, CircleShape)
                                    .clickable { viewModel.selectTab(index) },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.app_logo),
                                    contentDescription = item.label,
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    } else {
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.selectTab(index) },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                    tint = if (isSelected) IqooOrange else TextMuted
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    fontSize = 11.sp,
                                    color = if (isSelected) IqooOrange else TextMuted
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = IqooOrange.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(CyberBlack)
        ) {
            when (selectedTab) {
                0 -> HomeScreen(viewModel = viewModel)
                1 -> DiagnosisScreen(viewModel = viewModel)
                2 -> AIDoctorChatScreen(viewModel = viewModel)
                3 -> HistoryScreen(viewModel = viewModel)
                4 -> ProfileScreen(viewModel = viewModel)
                else -> HomeScreen(viewModel = viewModel)
            }
        }
    }
}
