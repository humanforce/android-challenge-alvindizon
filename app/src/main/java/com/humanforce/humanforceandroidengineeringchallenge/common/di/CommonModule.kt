package com.humanforce.humanforceandroidengineeringchallenge.common.di

import com.humanforce.humanforceandroidengineeringchallenge.common.threading.AppCoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun provideAppCoroutineDispatchers(): AppCoroutineDispatchers =
        AppCoroutineDispatchers(Dispatchers.Main, Dispatchers.IO, Dispatchers.Default)
}