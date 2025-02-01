package com.humanforce.humanforceandroidengineeringchallenge.features.search.usecase

import com.humanforce.humanforceandroidengineeringchallenge.data.remote.OpenWeatherApi
import com.humanforce.humanforceandroidengineeringchallenge.features.search.mapper.toSearchResult
import com.humanforce.humanforceandroidengineeringchallenge.features.search.model.SearchResult
import javax.inject.Inject
import javax.inject.Singleton

interface SearchLocationUseCase {

    suspend fun invoke(query: String): List<SearchResult>
}

@Singleton
class SearchLocationUseCaseImpl @Inject constructor(
    private val openWeatherApi: OpenWeatherApi
) : SearchLocationUseCase {


    override suspend fun invoke(query: String): List<SearchResult> {
        val resp = openWeatherApi.getCities(
            query = query,
            limit = RESULT_LIMIT,
        )
        return resp.map { it.toSearchResult() }
    }

    companion object {
        private const val RESULT_LIMIT = "20"
    }
}