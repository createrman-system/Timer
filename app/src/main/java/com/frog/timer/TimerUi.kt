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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Timer
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
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()

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

@Composable
fun TimerMainScreen(viewModel: TimerViewModel) {
    val state by viewModel.state.collectAsState()
    TimerContent(
        state = state,
        onToggleMode = viewModel::toggleMode,
        onStart = viewModel::startTimer,
        onPause = viewModel::pauseTimer,
        onReset = viewModel::resetTimer,
        onSetTime = viewModel::setTimer
    )
}

@Composable
fun TimerContent(
    state: TimerState,
    onToggleMode: () -> Unit,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onSetTime: (Long) -> Unit
) {
    var showCustomDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = if (state.mode == TimerMode.TIMER) 0 else 1) {
                Tab(
                    selected = state.mode == TimerMode.TIMER,
                    onClick = { if (state.mode != TimerMode.TIMER) onToggleMode() },
                    text = { Text("Timer") }
                )
                Tab(
                    selected = state.mode == TimerMode.STOPWATCH,
                    onClick = { if (state.mode != TimerMode.STOPWATCH) onToggleMode() },
                    text = { Text("Secundemeter") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(80.dp)) // Move timer down to avoid camera cutout

            TimerDisplay(
                timeMillis = state.timeMillis,
                progress = state.progress,
                isStopwatch = state.mode == TimerMode.STOPWATCH
            )

            Spacer(modifier = Modifier.height(32.dp))

            TimerControls(
                status = state.status,
                mode = state.mode,
                onStart = onStart,
                onPause = onPause,
                onReset = onReset,
                onSetTime = onSetTime,
                onCustomClick = { showCustomDialog = true }
            )
        }
    }

    if (showCustomDialog) {
        CustomTimeDialog(
            onDismiss = { showCustomDialog = false },
            onConfirm = { minutes, seconds ->
                onSetTime((minutes * 60 + seconds) * 1000L)
                showCustomDialog = false
            }
        )
    }
}

@Composable
fun TimerDisplay(timeMillis: Long, progress: Float, isStopwatch: Boolean) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "timerProgress"
    )
    
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
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 16.dp,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                strokeCap = StrokeCap.Round
            )
        } else {
            // Indeterminate-like rotation for stopwatch or just a static ring
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.secondary,
                strokeWidth = 8.dp,
                trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
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
                color = if (isStopwatch) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
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
fun TimerControls(
    status: TimerStatus,
    mode: TimerMode,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onSetTime: (Long) -> Unit,
    onCustomClick: () -> Unit
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
                Icon(Icons.Rounded.Refresh, contentDescription = "Reset")
                Spacer(Modifier.size(8.dp))
                Text("Reset")
            }

            LargeFloatingActionButton(
                onClick = if (status == TimerStatus.RUNNING) onPause else onStart,
                containerColor = if (mode == TimerMode.TIMER) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                contentColor = if (mode == TimerMode.TIMER) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(16.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(
                    imageVector = if (status == TimerStatus.RUNNING) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    contentDescription = if (status == TimerStatus.RUNNING) "Pause" else "Start",
                    modifier = Modifier.size(40.dp)
                )
            }

            if (mode == TimerMode.TIMER) {
                OutlinedButton(
                    onClick = onCustomClick,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(Icons.Rounded.Edit, contentDescription = "Custom")
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
    onConfirm: (Int, Int) -> Unit
) {
    var minutes by remember { mutableStateOf("0") }
    var seconds by remember { mutableStateOf("0") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Custom Timer") },
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = minutes,
                    onValueChange = { minutes = it.filter { char -> char.isDigit() } },
                    label = { Text("Min") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(Modifier.width(8.dp))
                Text(":", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.width(8.dp))
                TextField(
                    value = seconds,
                    onValueChange = { seconds = it.filter { char -> char.isDigit() } },
                    label = { Text("Sec") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val min = minutes.toIntOrNull() ?: 0
                val sec = seconds.toIntOrNull() ?: 0
                onConfirm(min, sec)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
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
            onSetTime = {}
        )
    }
}
