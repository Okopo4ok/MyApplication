package com.example.myapplication

import android.app.Application
import com.example.data.repositories.books.BooksRepositoryImpl
import com.example.domain.usecases.BookmarkBookUseCase
import com.example.domain.usecases.GetBookmarksUseCase
import com.example.domain.usecases.GetBooksUseCase
import com.example.domain.usecases.UnbookmarkBookUseCase
import com.example.myapplication.di.ServiceLocator
import com.example.myapplication.mappers.BookWithStatusMapper
import timber.log.Timber

class CleanArchitectureBlueprintsApplication : Application() {
    private val booksRepository: BooksRepositoryImpl
        get() = ServiceLocator.provideBooksRepository(this)

    val getBooksUseCase: GetBooksUseCase
        get() = GetBooksUseCase(booksRepository)

    val getBookmarksUseCase: GetBookmarksUseCase
        get() = GetBookmarksUseCase(booksRepository)

    val bookmarkBooksUseCase: BookmarkBookUseCase
        get() = BookmarkBookUseCase(booksRepository)

    val unbookmarkBookUseCase: UnbookmarkBookUseCase
        get() = UnbookmarkBookUseCase(booksRepository)

    val bookWithStatusMapper = BookWithStatusMapper()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}