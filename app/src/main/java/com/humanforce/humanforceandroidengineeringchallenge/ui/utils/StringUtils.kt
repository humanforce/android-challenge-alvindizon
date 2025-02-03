package com.humanforce.humanforceandroidengineeringchallenge.ui.utils

fun String.toFlagEmoji(): String =
    uppercase()
        .split("")
        .filter { it.isNotBlank() }
        .map { it.codePointAt(0) + 0x1F1A5 }
        .joinToString("") { String(Character.toChars(it)) }

fun String.getOpenWeatherIconUrl(): String =
    "https://openweathermap.org/img/wn/${this}@2x.png"
