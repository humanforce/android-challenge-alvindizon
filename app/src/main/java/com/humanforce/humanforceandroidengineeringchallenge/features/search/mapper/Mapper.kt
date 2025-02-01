package com.humanforce.humanforceandroidengineeringchallenge.features.search.mapper

import com.humanforce.humanforceandroidengineeringchallenge.data.remote.response.GetCitiesResponseItem
import com.humanforce.humanforceandroidengineeringchallenge.features.search.model.SearchResult

fun GetCitiesResponseItem.toSearchResult(): SearchResult {
    return SearchResult(
        name = name.orEmpty(),
        state = state.orEmpty(),
        country = country.orEmpty(),
        lat = lat ?: 0.0,
        lon = lon ?: 0.0
    )
}