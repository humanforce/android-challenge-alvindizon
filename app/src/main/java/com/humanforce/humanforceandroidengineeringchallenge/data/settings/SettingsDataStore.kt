package com.humanforce.humanforceandroidengineeringchallenge.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.humanforce.humanforceandroidengineeringchallenge.common.units.MeasurementUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SettingsDataStore {
    fun getUnits(): Flow<MeasurementUnit>
    suspend fun setUnits(measurementUnit: MeasurementUnit)
}


internal class SettingsDataStoreImpl(private val dataStore: DataStore<Preferences>) :
    SettingsDataStore {
    private val measurementUnitKey = stringPreferencesKey("measurementUnit")

    override fun getUnits(): Flow<MeasurementUnit> {
        return dataStore.data.map { preference ->
            preference[measurementUnitKey]?.let {
                enumValueOf<MeasurementUnit>(it)
            } ?: MeasurementUnit.Metric
        }
    }

    override suspend fun setUnits(measurementUnit: MeasurementUnit) {
        dataStore.edit { it[measurementUnitKey] = measurementUnit.name }
    }
}