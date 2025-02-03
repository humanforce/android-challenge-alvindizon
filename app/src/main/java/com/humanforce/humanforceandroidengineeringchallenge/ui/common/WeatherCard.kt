package com.humanforce.humanforceandroidengineeringchallenge.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.humanforce.humanforceandroidengineeringchallenge.R

@Composable
fun WeatherCard(
    iconUrl: String,
    description: String,
    temperatureString: String,
    feelsLikeTempString: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    OutlinedCard(
        onClick = { onClick?.invoke() },
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors()
            .copy(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = stringResource(R.string.current_weather_label),
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = iconUrl,
                contentDescription = description,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .size(100.dp)
            )
            Text(
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                text = description,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                style = MaterialTheme.typography.headlineSmall,
                text = temperatureString,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = stringResource(
                    R.string.feels_like_label,
                    feelsLikeTempString
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}