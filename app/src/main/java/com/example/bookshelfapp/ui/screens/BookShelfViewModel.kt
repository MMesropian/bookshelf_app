package com.example.bookshelfapp.ui.screens

import androidx.lifecycle.ViewModel

class BookShelfViewModel: ViewModel() {

}
sealed interface BookShelfUiState {
    data class Success() : BookShelfUiState
    object Error : BookShelfUiState
    object Loading : BookShelfUiState
}