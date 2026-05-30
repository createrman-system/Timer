package com.frog.timer

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Switch
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frog.timer.ui.theme.TimerTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TimerApp(viewModel: TimerViewModel) {
    val state by viewModel.state.collectAsState()
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    
    val darkTheme = when (state.isDarkMode) {
        true -> true
        false -> false
        null -> isSystemInDarkTheme()
    }

    TimerTheme(darkTheme = darkTheme) {
        ListDetailPaneScaffold(
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                TimerMainScreen(viewModel)
            },
            detailPane = {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Timer Details", style = MaterialTheme.typography.titleLarge)
                }
            }
        )
    }
}

@Composable
fun TimerMainScreen(viewModel: TimerViewModel) {
    val state by viewModel.state.collectAsState()
    TimerContent(
        state = state,
        onToggleMode = viewModel::toggleMode,
        onStart = viewModel::startTimer,
        onPause = viewModel::pauseTimer,
        onReset = viewModel::resetTimer,
        onSetTime = viewModel::setTimer,
        onAddLap = viewModel::addLap,
        onToggleCity = viewModel::toggleCity,
        availableCities = viewModel.getAvailableCityNames(),
        onSetAlarmSound = viewModel::setAlarmSound,
        onToggleDarkMode = viewModel::toggleDarkMode,
        onSetLanguage = viewModel::setLanguage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerContent(
    state: TimerState,
    onToggleMode: () -> Unit,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onSetTime: (Long) -> Unit,
    onAddLap: () -> Unit,
    onToggleCity: (String) -> Unit,
    availableCities: List<String>,
    onSetAlarmSound: (AlarmSound) -> Unit,
    onToggleDarkMode: (Boolean?) -> Unit,
    onSetLanguage: (AppLanguage) -> Unit
) {
    var showCustomDialog by remember { mutableStateOf(value = false) }
    var showCityDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(stringResource("universal_timer", state.language.code)) },
                    actions = {
                        if (state.mode == TimerMode.WORLD_TIME) {
                            IconButton(onClick = { showCityDialog = true }) {
                                Icon(Icons.Rounded.Add, contentDescription = stringResource("add_city", state.language.code))
                            }
                        }
                        IconButton(onClick = { showSettingsDialog = true }) {
                            Icon(Icons.Rounded.Settings, contentDescription = stringResource("settings", state.language.code))
                        }
                    }
                )
                TabRow(selectedTabIndex = when (state.mode) {
                    TimerMode.TIMER -> 0
                    TimerMode.STOPWATCH -> 1
                    TimerMode.WORLD_TIME -> 2
                }) {
                    Tab(
                        selected = state.mode == TimerMode.TIMER,
                        onClick = { if (state.mode != TimerMode.TIMER) onToggleMode() },
                        text = { Text(stringResource("timer", state.language.code)) }
                    )
                    Tab(
                        selected = state.mode == TimerMode.STOPWATCH,
                        onClick = { if (state.mode != TimerMode.STOPWATCH) onToggleMode() },
                        text = { Text(stringResource("stopwatch", state.language.code)) }
                    )
                    Tab(
                        selected = state.mode == TimerMode.WORLD_TIME,
                        onClick = { if (state.mode != TimerMode.WORLD_TIME) onToggleMode() },
                        text = { Text(stringResource("world", state.language.code)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        if (state.mode == TimerMode.WORLD_TIME) {
            WorldTimeScreen(state.worldTimes, innerPadding, state.language.code)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                if (state.mode == TimerMode.TIMER) {
                    TimerDisplay(
                        timeMillis = state.timeMillis,
                        progress = state.progress,
                        isStopwatch = false
                    )
                } else {
                    TimerDisplay(
                        timeMillis = state.timeMillis,
                        progress = 0f,
                        isStopwatch = true
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                TimerControls(
                    status = state.status,
                    mode = state.mode,
                    onStart = onStart,
                    onPause = onPause,
                    onReset = onReset,
                    onSetTime = onSetTime,
                    onAddLap = onAddLap,
                    onCustomClick = { showCustomDialog = true },
                    languageCode = state.language.code
                )

                if (state.mode == TimerMode.STOPWATCH && state.laps.isNotEmpty()) {
                    LapsList(state.laps, state.language.code)
                }
            }
        }
    }

    if (showCustomDialog) {
        CustomTimeDialog(
            onDismiss = { showCustomDialog = false },
            onConfirm = { minutes, seconds ->
                onSetTime(((minutes * 60L) + seconds) * 1000L)
                showCustomDialog = false
            },
            languageCode = state.language.code
        )
    }

    if (showCityDialog) {
        CitySelectionDialog(
            availableCities = availableCities,
            activeCities = state.worldTimes.map { it.cityName }.toSet(),
            onDismiss = { showCityDialog = false },
            onToggleCity = onToggleCity,
            languageCode = state.language.code
        )
    }

    if (showSettingsDialog) {
        SettingsDialog(
            state = state,
            onDismiss = { showSettingsDialog = false },
            onSetAlarmSound = onSetAlarmSound,
            onToggleDarkMode = onToggleDarkMode,
            onSetLanguage = onSetLanguage
        )
    }
}

@Composable
fun SettingsDialog(
    state: TimerState,
    onDismiss: () -> Unit,
    onSetAlarmSound: (AlarmSound) -> Unit,
    onToggleDarkMode: (Boolean?) -> Unit,
    onSetLanguage: (AppLanguage) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource("settings", state.language.code)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(stringResource("theme", state.language.code), style = MaterialTheme.typography.labelLarge)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Text(stringResource("system_default", state.language.code), modifier = Modifier.weight(1f))
                    Switch(
                        checked = state.isDarkMode == null,
                        onCheckedChange = { if (it) onToggleDarkMode(null) else onToggleDarkMode(false) }
                    )
                }
                if (state.isDarkMode != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        Text(stringResource("dark_mode", state.language.code), modifier = Modifier.weight(1f))
                        Switch(
                            checked = state.isDarkMode == true,
                            onCheckedChange = { onToggleDarkMode(it) }
                        )
                    }
                }
                
                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                Text(stringResource("alarm_sound", state.language.code), style = MaterialTheme.typography.labelLarge)
                var expandedSound by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    OutlinedButton(
                        onClick = { expandedSound = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(state.selectedAlarmSound.name)
                    }
                    DropdownMenu(expanded = expandedSound, onDismissRequest = { expandedSound = false }) {
                        AlarmSound.entries.forEach { sound ->
                            DropdownMenuItem(
                                text = { Text(sound.name) },
                                onClick = {
                                    onSetAlarmSound(sound)
                                    expandedSound = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                Text(stringResource("language", state.language.code), style = MaterialTheme.typography.labelLarge)
                var expandedLang by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    OutlinedButton(
                        onClick = { expandedLang = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(state.language.displayName)
                    }
                    DropdownMenu(expanded = expandedLang, onDismissRequest = { expandedLang = false }) {
                        AppLanguage.entries.forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(lang.displayName) },
                                onClick = {
                                    onSetLanguage(lang)
                                    expandedLang = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource("close", state.language.code)) }
        }
    )
}

@Composable
fun WorldTimeScreen(worldTimes: List<WorldTime>, paddingValues: androidx.compose.foundation.layout.PaddingValues, languageCode: String) {
    if (worldTimes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
            Text(stringResource("select_cities", languageCode))
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(worldTimes) { worldTime ->
                ListItem(
                    headlineContent = { Text(worldTime.cityName, fontWeight = FontWeight.Bold) },
                    supportingContent = { Text(worldTime.zoneId) },
                    trailingContent = {
                        Text(
                            worldTime.time,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
fun TimerDisplay(
    timeMillis: Long, 
    progress: Float, 
    isStopwatch: Boolean
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "timerProgress"
    )
    
    val color = when {
        isStopwatch -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.primary
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .aspectRatio(1f)
            .padding(16.dp)
    ) {
        if (!isStopwatch) {
            CircularProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxSize(),
                color = color,
                strokeWidth = 16.dp,
                trackColor = color.copy(alpha = 0.1f),
                strokeCap = StrokeCap.Round
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                color = color,
                strokeWidth = 8.dp,
                trackColor = color.copy(alpha = 0.1f),
                strokeCap = StrokeCap.Round
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formatTime(timeMillis),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 80.sp,
                    letterSpacing = (-2).sp
                ),
                color = color
            )
            Text(
                text = formatMillis(timeMillis),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun LapsList(laps: List<Long>, languageCode: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(stringResource("lap", languageCode), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        laps.forEachIndexed { index, lapTime ->
            ListItem(
                headlineContent = { Text("${stringResource("lap", languageCode)} ${laps.size - index}") },
                trailingContent = { Text(formatTime(lapTime) + formatMillis(lapTime)) }
            )
            if (index < laps.size - 1) HorizontalDivider()
        }
    }
}

@Composable
fun CitySelectionDialog(
    availableCities: List<String>,
    activeCities: Set<String>,
    onDismiss: () -> Unit,
    onToggleCity: (String) -> Unit,
    languageCode: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Column {
                Text(stringResource("select_cities", languageCode))
                Text(
                    "Select up to 5 favorites", // Could also be localized
                    style = MaterialTheme.typography.bodySmall,
                    color = if (activeCities.size >= 5) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            LazyColumn {
                items(availableCities) { city ->
                    val isChecked = city in activeCities
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { onToggleCity(city) },
                            enabled = isChecked || activeCities.size < 5
                        )
                        Text(
                            city, 
                            modifier = Modifier.padding(start = 8.dp),
                            color = if (!isChecked && activeCities.size >= 5) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource("done", languageCode)) }
        }
    )
}

@Composable
fun TimerControls(
    status: TimerStatus,
    mode: TimerMode,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onSetTime: (Long) -> Unit,
    onAddLap: () -> Unit,
    onCustomClick: () -> Unit,
    languageCode: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onReset,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(Icons.Rounded.Refresh, contentDescription = stringResource("reset", languageCode))
                Spacer(Modifier.size(8.dp))
                Text(stringResource("reset", languageCode))
            }

            LargeFloatingActionButton(
                onClick = if (status == TimerStatus.RUNNING) onPause else onStart,
                containerColor = when (mode) {
                    TimerMode.TIMER -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.secondary
                },
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(16.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(
                    imageVector = if (status == TimerStatus.RUNNING) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    contentDescription = if (status == TimerStatus.RUNNING) stringResource("pause", languageCode) else stringResource("start", languageCode),
                    modifier = Modifier.size(40.dp)
                )
            }

            if (mode == TimerMode.TIMER) {
                OutlinedButton(
                    onClick = onCustomClick,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(Icons.Rounded.Edit, contentDescription = stringResource("custom", languageCode))
                }
            } else if (mode == TimerMode.STOPWATCH) {
                OutlinedButton(
                    onClick = onAddLap,
                    enabled = status == TimerStatus.RUNNING,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(Icons.Rounded.Flag, contentDescription = stringResource("lap", languageCode))
                }
            }
        }

        if (mode == TimerMode.TIMER) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilledTonalButton(onClick = { onSetTime(60000L) }) { Text("1m") }
                FilledTonalButton(onClick = { onSetTime(300000L) }) { Text("5m") }
                FilledTonalButton(onClick = { onSetTime(600000L) }) { Text("10m") }
            }
        }
    }
}

@Composable
fun CustomTimeDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
    languageCode: String
) {
    var minutes by remember { mutableStateOf("0") }
    var seconds by remember { mutableStateOf("0") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource("custom", languageCode)) },
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = minutes,
                    onValueChange = { minutes = it.filter { char -> char.isDigit() } },
                    label = { Text(stringResource("min", languageCode)) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(Modifier.width(8.dp))
                Text(":", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.width(8.dp))
                TextField(
                    value = seconds,
                    onValueChange = { seconds = it.filter { char -> char.isDigit() } },
                    label = { Text(stringResource("sec", languageCode)) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val min = minutes.toIntOrNull() ?: 0
                    val sec = seconds.toIntOrNull() ?: 0
                    onConfirm(min, sec)
                },
            ) {
                Text(stringResource("ok", languageCode))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource("cancel", languageCode))
            }
        }
    )
}

private fun formatTime(timeMillis: Long): String {
    val totalSeconds = timeMillis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}

private fun formatMillis(timeMillis: Long): String {
    val millis = (timeMillis % 1000) / 10
    return String.format(Locale.getDefault(), ".%02d", millis)
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun TimerPreview() {
    TimerTheme {
        TimerContent(
            state = TimerState(timeMillis = 300000L, initialTimeMillis = 300000L),
            onToggleMode = {},
            onStart = {},
            onPause = {},
            onReset = {},
            onSetTime = {},
            onAddLap = {},
            onToggleCity = {},
            availableCities = listOf("London", "New York"),
            onSetAlarmSound = {},
            onToggleDarkMode = {},
            onSetLanguage = {}
        )
    }
}
