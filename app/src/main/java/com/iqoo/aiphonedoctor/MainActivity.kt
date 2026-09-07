package com.iqoo.aiphonedoctor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.iqoo.aiphonedoctor.ui.navigation.MainAppContainer
import com.iqoo.aiphonedoctor.ui.theme.AIPhoneDoctorTheme
import com.iqoo.aiphonedoctor.ui.theme.CyberBlack
import com.iqoo.aiphonedoctor.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIPhoneDoctorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CyberBlack
                ) {
                    MainAppContainer(viewModel = mainViewModel)
                }
            }
        }
    }
}
