package com.example.uts_android.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<DataMovie>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
) {
}