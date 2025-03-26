package com.example.bookshelfapp.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class ImageLink(
    val smallThumbnail: String = "",
    val thumbnail: String = "",
    val small: String? = "",
    val medium: String? = "",
    val large: String? = ""
)