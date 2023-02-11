package com.example.myapplication.di

import android.content.Context
import androidx.viewbinding.BuildConfig
import com.example.data.api.NetworkModule
import com.example.data.db.BooksDatabase
import com.example.data.mappers.BookApiResponseMapper
import com.example.data.mappers.BookEntityMapper
import com.example.data.repositories.books.BooksLocalDataSource
import com.example.data.repositories.books.BooksLocalDataSourceImpl
import com.example.data.repositories.books.BooksRemoteDataSourceImpl
import com.example.data.repositories.books.BooksRepositoryImpl
import kotlinx.coroutines.Dispatchers


object ServiceLocator {
    private var database: BooksDatabase? = null
    private val networkModule by lazy {
        NetworkModule()
    }
    private val bookEntityMapper by lazy {
        BookEntityMapper()
    }

    @Volatile
    var booksRepository: BooksRepositoryImpl? = null

    fun provideBooksRepository(context: Context): BooksRepositoryImpl {
        // useful because this method can be accessed by multiple threads
        synchronized(this) {
            return booksRepository ?: createBooksRepository(context)
        }
    }

    private fun createBooksRepository(context: Context): BooksRepositoryImpl {
        val newRepo =
            BooksRepositoryImpl(
                createBooksLocalDataSource(context),
                BooksRemoteDataSourceImpl(
                    networkModule.createBooksApi(BuildConfig.GOOGLE_APIS_ENDPOINT),
                    BookApiResponseMapper()
                )
            )
        booksRepository = newRepo
        return newRepo
    }

    private fun createBooksLocalDataSource(context: Context): BooksLocalDataSource {
        val database = database ?: createDataBase(context)
        return BooksLocalDataSourceImpl(
            database.bookDao(),
            Dispatchers.IO,
            bookEntityMapper
        )
    }

    private fun createDataBase(context: Context): BooksDatabase {
        val result = BooksDatabase.getDatabase(context)
        database = result
        return result
    }
}