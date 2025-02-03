package com.humanforce.humanforceandroidengineeringchallenge.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Applies an overlay with [overlayColor] together with a [CircularProgressIndicator] */
@Composable
fun LoadingOverlay(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    overlayColor: Color = MaterialTheme.colorScheme.surface,
    indicatorColor: Color = MaterialTheme.colorScheme.primary,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        content()
        AnimatedVisibility(enter = fadeIn(), exit = fadeOut(), visible = isLoading) {
            Canvas(
                modifier = Modifier.fillMaxSize(),
            ) {
                drawRect(overlayColor)
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(modifier = Modifier.size(40.dp), color = indicatorColor)
            }
        }
    }
}
