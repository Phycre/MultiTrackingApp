package com.example.multitrackingapp

import android.os.Bundle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.multitrackingapp.ui.theme.MultiTrackingAppTheme
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

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
    val scope = rememberCoroutineScope()
    var isRunning by remember { mutableStateOf(false) }
    val itemsList = remember { mutableStateListOf<String>() }

    Row(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    if (!isRunning) {
                        isRunning = true
                        scope.launch {
                            while (isRunning) {
                                addLocation(itemsList)
                                delay(1000L)
                            }
                        }
                    }
                }
            ) {
                Text(text = "Start Function")
            }

            Button(
                onClick = {
                    isRunning = false
                }
            ) {
                Text(text = "Stop Function")
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // right column
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(itemsList) { item ->
                Text(text = item)
            }
        }
    }
}

fun addLocation(itemsList: MutableList<String>) {
    val timestamp = System.currentTimeMillis()
    itemsList.add("Function executed at: $timestamp")
}

@Composable
fun HistoryScreen(innerPadding: PaddingValues) {
    Text(
        text = "History Cont    ent",
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