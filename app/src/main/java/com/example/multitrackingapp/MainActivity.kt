package com.example.multitrackingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.multitrackingapp.ui.theme.MultiTrackingAppTheme
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

enum class Screen {
    Home, History, Settings
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultiTrackingAppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val currentScreen = remember { mutableStateOf<Screen>(Screen.Home) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                currentScreen = currentScreen.value,
                onScreenSelected = { selectedScreen ->
                    currentScreen.value = selectedScreen
                }
            )
        }
    ) { innerPadding ->
        when (currentScreen.value) {
            Screen.Home -> HomeScreen(innerPadding)
            Screen.History -> HistoryScreen(innerPadding)
            Screen.Settings -> SettingsScreen(innerPadding)
        }
    }
}

@Composable
fun HomeScreen(innerPadding: PaddingValues) {
    Text(
        text = "Home Content",
        modifier = Modifier.padding(innerPadding)
    )
}

@Composable
fun HistoryScreen(innerPadding: PaddingValues) {
    Text(
        text = "History Content",
        modifier = Modifier.padding(innerPadding)
    )
}

@Composable
fun SettingsScreen(innerPadding: PaddingValues) {
    Text(
        text = "Settings Content",
        modifier = Modifier.padding(innerPadding)
    )
}

@Composable
private fun BottomNavigationBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.bottom_navigation_home)
                )
            },
            selected = currentScreen == Screen.Home,
            onClick = { onScreenSelected(Screen.Home) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.bottom_navigation_history)
                )
            },
            selected = currentScreen == Screen.History,
            onClick = { onScreenSelected(Screen.History) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.bottom_navigation_settings)
                )
            },
            selected = currentScreen == Screen.Settings,
            onClick = { onScreenSelected(Screen.Settings) }
        )
    }
}