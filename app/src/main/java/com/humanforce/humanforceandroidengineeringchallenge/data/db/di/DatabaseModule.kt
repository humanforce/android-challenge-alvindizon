package com.humanforce.humanforceandroidengineeringchallenge.data.db.di

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.humanforce.humanforceandroidengineeringchallenge.data.db.SavedLocationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSqlDriver(@ApplicationContext context: Context): SqlDriver {
        return AndroidSqliteDriver(SavedLocationDatabase.Schema, context, "saved_locations.db",
            callback = object : AndroidSqliteDriver.Callback(SavedLocationDatabase.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.setForeignKeyConstraintsEnabled(true)
                }
            })
    }

    @Provides
    @Singleton
    fun provideSavedLocationDatabase(sqlDriver: SqlDriver): SavedLocationDatabase {
        return SavedLocationDatabase(sqlDriver)
    }
}