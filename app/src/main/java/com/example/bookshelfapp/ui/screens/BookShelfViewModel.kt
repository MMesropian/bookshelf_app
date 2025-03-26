package com.example.bookshelfapp.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil3.network.HttpException
import com.example.bookshelfapp.BookShelfApplication
import com.example.bookshelfapp.data.BookShelfRepository
import com.example.bookshelfapp.model.Book
import com.example.bookshelfapp.model.BooksList
import com.example.bookshelfapp.model.ImageLink
import com.example.bookshelfapp.model.VolumeInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException

class BookShelfViewModel(
    private val bookShelfRepository: BookShelfRepository
): ViewModel() {

    var bookShelfUiState: BookShelfUiState by mutableStateOf(BookShelfUiState.Loading)
        private set

    var specificBookUiState: SpecificBookUiState by mutableStateOf(SpecificBookUiState.Loading)
        private set

    private var _queryUiState: MutableStateFlow<QueryUiState> = MutableStateFlow(QueryUiState(
        currentBook = Book(
            volumeInfo = VolumeInfo(
                imageLinks = ImageLink()
            )
        )
    ))
    var queryUiState: StateFlow<QueryUiState> = _queryUiState

    fun updateQuery(query: String) {
        _queryUiState.update {
            it.copy(query = query)
        }
    }

    fun updateCurrentBook(book: Book) {
        _queryUiState.update {
            it.copy(currentBook = book)
        }
    }

    fun navigateToBooksList() {
        _queryUiState.update {
            it.copy(isShowingBooksList = true)
        }
    }

    fun navigateToBooksDetailInfo() {
        _queryUiState.update {
            it.copy(isShowingBooksList = false)
        }
    }

    init {
        getBooks()
    }

    fun getBooks() {
        viewModelScope.launch {
            bookShelfUiState = BookShelfUiState.Loading
            bookShelfUiState = try {
                BookShelfUiState.Success(bookShelfRepository.getBooks(queryUiState.value.query))
            } catch (e: IOException) {
                BookShelfUiState.Error
            } catch (e: HttpException){
                BookShelfUiState.Error
            }
        }
    }

    fun getSpecificBook() {
        viewModelScope.launch {
            specificBookUiState = SpecificBookUiState.Loading
            specificBookUiState = try {
                SpecificBookUiState.Success(bookShelfRepository.getSpecificBook(queryUiState.value.currentBook.id))
            } catch (e: IOException) {
                SpecificBookUiState.Error
            } catch (e: HttpException) {
                SpecificBookUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookShelfApplication)
                val bookShelfRepository = application.container.bookShelfRepository
                BookShelfViewModel(bookShelfRepository)
            }
        }
    }
}
sealed interface BookShelfUiState {
    data class Success(val books: BooksList) : BookShelfUiState
    object Error : BookShelfUiState
    object Loading : BookShelfUiState

}

sealed interface SpecificBookUiState {
    data class Success(val specificBook: Book) : SpecificBookUiState
    object Error : SpecificBookUiState
    object Loading : SpecificBookUiState
}