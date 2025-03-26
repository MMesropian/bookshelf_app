package com.example.bookshelfapp.ui.screens

import com.example.bookshelfapp.model.Book

data class QueryUiState(
    val query: String = "pop",
    val currentBook: Book,
    val isShowingBooksList: Boolean = true,
)