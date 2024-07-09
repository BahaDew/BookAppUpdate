package uz.bahadew.bookappupdate.domain

import kotlinx.coroutines.flow.Flow
import uz.bahadew.bookappupdate.data.BookData
import uz.bahadew.bookappupdate.data.BooksByCategoryData


interface AppRepository {
    fun getAllBooksByCategory(): Flow<Result<List<BooksByCategoryData>>>

    fun getBooksByName(name: String): Flow<Result<List<BookData>>>

    fun getAllSavedBook(): Flow<Result<List<BookData>>>

    fun isSavedBook(bookData: BookData): Flow<Result<Boolean>>

    fun downloadESBook(bookData: BookData): Flow<Result<String>>

    fun favoriteChange(bookData: BookData, checked: Boolean)
}