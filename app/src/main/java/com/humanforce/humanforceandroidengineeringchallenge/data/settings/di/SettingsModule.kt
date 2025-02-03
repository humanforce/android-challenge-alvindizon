package com.humanforce.humanforceandroidengineeringchallenge.data.settings.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.humanforce.humanforceandroidengineeringchallenge.data.settings.SettingsDataStore
import com.humanforce.humanforceandroidengineeringchallenge.data.settings.SettingsDataStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hf_weather_settings")

    @Provides
    @Singleton
    fun providesSettingDataStore(@ApplicationContext context: Context): SettingsDataStore =
        SettingsDataStoreImpl(context.dataStore)
}