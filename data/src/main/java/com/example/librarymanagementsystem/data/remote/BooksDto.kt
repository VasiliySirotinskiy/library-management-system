package com.example.librarymanagementsystem.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VolumeResponse(
    val items: List<VolumeItem>?
)

@JsonClass(generateAdapter = true)
data class VolumeItem(
    val id: String?,
    @Json(name = "volumeInfo") val info: VolumeInfo?
)

@JsonClass(generateAdapter = true)
data class VolumeInfo(
    val title: String?,
    val authors: List<String>?,
    val pageCount: Int?,
    @Json(name = "industryIdentifiers") val identifiers: List<IndustryIdentifier>?
)

@JsonClass(generateAdapter = true)
data class IndustryIdentifier(
    @Json(name = "type") val type: String?,
    @Json(name = "identifier") val identifier: String?
)
