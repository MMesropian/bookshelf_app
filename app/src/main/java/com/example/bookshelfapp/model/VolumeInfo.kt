package com.example.bookshelfapp.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class VolumeInfo(
    val title: String = "",
    //val authors: List<String>,
    val publisher: String? = "",
    val publisherDate: String? = "",
    val description: String? = "",
    val pageCount: Int  = 0,
    val printedPageCount: Int? = 0,
    //val dimensions: Dimension,
    //val category: List<String>,
    val imageLinks: ImageLink,
    val language: String = ""
)

