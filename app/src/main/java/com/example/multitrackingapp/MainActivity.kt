package com.example.multitrackingapp

import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import androidx.core.app.ActivityCompat

enum class Screen {
    Home, History, Settings
}

class MainActivity : ComponentActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private fun checkPermissions() {
        if (!hasLocationPermissions()) {
            requestLocationPermissions()
            return
        }
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun hasLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermissions()
        enableEdgeToEdge()
        setContent {
            MultiTrackingAppTheme {
                MainScreen { onLocationFetched ->
                    fetchLocation(onLocationFetched)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun fetchLocation(onLocationFetched: (String) -> Unit) {
        if (!hasLocationPermissions()) {
            requestLocationPermissions()
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val locationString = "Lat: ${location.latitude}, Lng: ${location.longitude}"
                onLocationFetched(locationString)
            } else {
                onLocationFetched("Location unavailable")
            }
        }
    }
}


@Composable
fun MainScreen(fetchLocation: (onLocationFetched: (String) -> Unit) -> Unit) {
    val currentScreen = remember { mutableStateOf<Screen>(Screen.Home) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                currentScreen = currentScreen.value,
                onScreenSelected = { selectedScreen -> currentScreen.value = selectedScreen }
            )
        }
    ) { innerPadding ->
        when (currentScreen.value) {
            Screen.Home -> HomeScreen(innerPadding, fetchLocation)
            Screen.History -> HistoryScreen(innerPadding)
            Screen.Settings -> SettingsScreen(innerPadding)
        }
    }
}


@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    fetchLocation: (onLocationFetched: (String) -> Unit) -> Unit
) {
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
                                fetchLocation { location ->
                                    itemsList.add(location)
                                }
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
