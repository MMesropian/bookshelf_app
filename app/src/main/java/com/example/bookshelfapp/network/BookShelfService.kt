package com.example.bookshelfapp.network

import com.example.bookshelfapp.model.Book
import com.example.bookshelfapp.model.BooksList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookShelfService {
    @GET("volumes?maxResults=40")
    suspend fun getBooks(@Query("q") query: String): BooksList
    @GET("volumes/{volume_id}")
    suspend fun getSpecificBook(@Path("volume_id") volumeId: String): Book
}