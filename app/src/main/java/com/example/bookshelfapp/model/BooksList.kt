package com.example.bookshelfapp.model

import kotlinx.serialization.Serializable

@Serializable
data class BooksList (
    val kind: String,
    val totalItems: Long,
    val items: List<Book>
)