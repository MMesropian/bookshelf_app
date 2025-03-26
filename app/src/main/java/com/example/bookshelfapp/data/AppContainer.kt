package com.example.bookshelfapp.data

import com.example.bookshelfapp.network.BookShelfService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val bookShelfRepository: BookShelfRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://www.googleapis.com/books/v1/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        //.addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
        .build()

    private val retrofitService: BookShelfService by lazy {
        retrofit.create(BookShelfService::class.java)
    }

    override val bookShelfRepository: BookShelfRepository by lazy {
        DefaultBookShelfRepository(retrofitService)
    }
}