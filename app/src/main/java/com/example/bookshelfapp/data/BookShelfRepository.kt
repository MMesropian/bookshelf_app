package com.example.bookshelfapp.data

import com.example.bookshelfapp.model.Book
import com.example.bookshelfapp.model.BooksList
import com.example.bookshelfapp.network.BookShelfService

interface BookShelfRepository {
    suspend fun getBooks(query: String): BooksList
    suspend fun getSpecificBook(volumeId: String): Book
}

class DefaultBookShelfRepository(
    private val bookShelfService: BookShelfService
) : BookShelfRepository {
    override suspend fun getBooks(query: String): BooksList {
        return bookShelfService.getBooks(query)
    }
    override suspend fun getSpecificBook(volumeId: String): Book {
        return bookShelfService.getSpecificBook(volumeId)
    }
}