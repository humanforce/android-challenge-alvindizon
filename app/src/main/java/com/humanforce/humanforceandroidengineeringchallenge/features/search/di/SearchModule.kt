package com.humanforce.humanforceandroidengineeringchallenge.features.search.di

import com.humanforce.humanforceandroidengineeringchallenge.features.search.usecase.SearchLocationUseCase
import com.humanforce.humanforceandroidengineeringchallenge.features.search.usecase.SearchLocationUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchModule {

    @Binds
    abstract fun bindSearchLocationUseCase(
        searchLocationUseCaseImpl: SearchLocationUseCaseImpl
    ): SearchLocationUseCase

}