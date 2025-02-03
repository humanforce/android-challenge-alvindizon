package com.humanforce.humanforceandroidengineeringchallenge.features.settings.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.humanforce.humanforceandroidengineeringchallenge.R
import com.humanforce.humanforceandroidengineeringchallenge.common.units.MeasurementUnit
import com.humanforce.humanforceandroidengineeringchallenge.ui.common.LoadingOverlay
import com.humanforce.humanforceandroidengineeringchallenge.ui.common.SnackbarHandler
import com.humanforce.humanforceandroidengineeringchallenge.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarHandler(
        messageFlow = viewModel.snackbarMessage,
        snackbarHostState = snackbarHostState,
        onShowSnackbar = {}
    )
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp),
                title = {
                    Text(
                        text = stringResource(R.string.settings_label),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LoadingOverlay(
            modifier = Modifier.padding(paddingValues),
            isLoading = state.isLoading
        ) {
            SettingsContent(
                state = state,
                onUnitClick = viewModel::onUnitClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onUnitClick: (MeasurementUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MeasurementUnitSection(
            initialUnit = state.preferredMeasurementUnit ?: MeasurementUnit.Metric,
            modifier = Modifier.fillMaxWidth(),
            onUnitClick = onUnitClick
        )
    }
}

@Composable
private fun MeasurementUnitSection(
    initialUnit: MeasurementUnit,
    onUnitClick: (MeasurementUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableIntStateOf(MeasurementUnit.valueOf(initialUnit.name).ordinal) }
    LaunchedEffect(initialUnit) {
        selectedIndex = MeasurementUnit.valueOf(initialUnit.name).ordinal
    }
    val units = MeasurementUnit.entries.toList()
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column (
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.measurement_unit_label),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
            )
            SingleChoiceSegmentedButtonRow {
                units.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = units.size
                        ),
                        onClick = {
                            selectedIndex = index
                            onUnitClick(MeasurementUnit.valueOf(label.name))
                        },
                        selected = index == selectedIndex,
                        label = {
                            Text(
                                text = label.name,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    AppTheme {
        SettingsContent(
            state = SettingsUiState(
                isLoading = false,
                preferredMeasurementUnit = MeasurementUnit.Metric
            ),
            onUnitClick = {}
        )
    }
}