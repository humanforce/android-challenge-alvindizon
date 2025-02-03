package com.humanforce.humanforceandroidengineeringchallenge.features.details.di

import com.humanforce.humanforceandroidengineeringchallenge.features.details.usecase.GetForecastDetailsUseCase
import com.humanforce.humanforceandroidengineeringchallenge.features.details.usecase.GetForecastDetailsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FeaturesModule {

    @Binds
    abstract fun bindsGetForecastUseCase(getForecastDetailsUseCaseImpl: GetForecastDetailsUseCaseImpl): GetForecastDetailsUseCase
}