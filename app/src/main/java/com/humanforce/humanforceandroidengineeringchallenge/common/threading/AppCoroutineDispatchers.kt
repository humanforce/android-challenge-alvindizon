package com.humanforce.humanforceandroidengineeringchallenge.common.threading

import kotlinx.coroutines.CoroutineDispatcher

/** Provides coroutines context. */
class AppCoroutineDispatchers(
    val main: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
)