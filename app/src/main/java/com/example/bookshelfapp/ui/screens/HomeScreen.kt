package com.example.bookshelfapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.Image
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.bookshelfapp.R
import com.example.bookshelfapp.model.Book
import com.example.bookshelfapp.model.BooksList
import java.nio.file.WatchEvent

@Composable
fun HomeScreen(
    bookViewMode: BookShelfViewModel,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val bookShelfUiState: BookShelfUiState = bookViewMode.bookShelfUiState

    when (bookShelfUiState) {
        is BookShelfUiState.Loading -> LoadingScreen()
        is BookShelfUiState.Success -> BooksGridScreen(
            bookViewMode,
            bookShelfUiState.books,
            modifier.padding(contentPadding)
        )

        is BookShelfUiState.Error -> ErrorScreen(retryAction)
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Image(
            painter = painterResource(R.drawable.connection_error),
            contentDescription = ""
        )
        Text(
            text = stringResource(R.string.failed_to_load),
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun BooksGridScreen(
    bookViewModel: BookShelfViewModel,
    books: BooksList,
    modifier: Modifier = Modifier,
) {
    val uiState by bookViewModel.queryUiState.collectAsState()

    if (uiState.isShowingBooksList) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp,),

            modifier = modifier
        ) {
            items(items = books.items, key = { book -> book.id }) { book ->
                BookCard(
                    book = book,
                    onClick = {
                        bookViewModel.updateCurrentBook(book)
                        bookViewModel.getSpecificBook()
                        bookViewModel.navigateToBooksDetailInfo()
                    }
                )
            }
        }
    } else {
        val specificBookUiState = bookViewModel.specificBookUiState
        when (specificBookUiState) {
            is SpecificBookUiState.Loading -> LoadingScreen()
            is SpecificBookUiState.Success -> {
                BookDetailInfo(
                    book = specificBookUiState.specificBook,
                    onBackPressed = { bookViewModel.navigateToBooksList() },
                    modifier = modifier
                )
            }

            is SpecificBookUiState.Error -> ErrorScreen({bookViewModel::specificBookUiState })
        }
    }
}

@Composable
fun BookCard(book: Book, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(book.volumeInfo.imageLinks.thumbnail.replace("http", "https"))
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.broken_image),
            error = painterResource(R.drawable.connection_error),
            contentDescription = stringResource(R.string.book_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
        )

    }
}

@Composable
fun BookDetailInfo(book: Book, onBackPressed: () -> Unit, modifier: Modifier = Modifier) {
    BackHandler {
        onBackPressed()
    }
    Column(
        modifier = modifier.padding(8.dp).verticalScroll(rememberScrollState()),

        //elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(book.volumeInfo.imageLinks.large?.replace("http", "https"))
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.broken_image),
            error = painterResource(R.drawable.connection_error),
            contentDescription = stringResource(R.string.book_image),
            contentScale = ContentScale.Crop,
            //modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = book.volumeInfo.title
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = book.volumeInfo.description.toString()
        )
    }

}