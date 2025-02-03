package com.humanforce.humanforceandroidengineeringchallenge.common.units

import kotlin.math.roundToInt

enum class MeasurementUnit(val value: String) {
    Metric("metric"),
    Standard("standard"),
    Imperial("imperial");
}

enum class Temperature(val sign: String) {
    Celsius("°C"),
    Fahrenheit("°F"),
    Kelvin("K");
}

fun Double.toTemperatureString(measurementUnit: MeasurementUnit): String =
    when (measurementUnit) {
        MeasurementUnit.Metric -> "${roundToInt()}${Temperature.Celsius.sign}"
        MeasurementUnit.Standard -> "${roundToInt()}${Temperature.Kelvin.sign}"
        MeasurementUnit.Imperial -> "${roundToInt()}${Temperature.Fahrenheit.sign}"
    }
