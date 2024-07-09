package uz.bahadew.bookappupdate.domain.impl

import android.os.Environment
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import uz.bahadew.bookappupdate.data.BookData
import uz.bahadew.bookappupdate.data.BooksByCategoryData
import uz.bahadew.bookappupdate.data.MyShared
import uz.bahadew.bookappupdate.domain.AppRepository
import uz.bahadew.bookappupdate.utils.myLogger
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.text.StringBuilder

@Singleton
class AppRepositoryIml @Inject constructor(
    private val myShared: MyShared
) : AppRepository {
    private val fireStore = Firebase.firestore
    private val storage = Firebase.storage
    private val booksCN = "books_info"
    private val categoryCN = "categories"
    private val bookFieldCategory = "categoryId"
    private val savedBookId = "SAVED_BOOK_ID"
    private val savedBookIdPrefix = "#"
    private var allBooksList = ArrayList<BookData>()
    private var tokenKey = "BOOK_APP_TOKEN"
    private var token = myShared.getString(tokenKey, "")
    init {
        if(token.isEmpty()) {
            token = UUID.randomUUID().toString()
            myShared.putString(tokenKey, token)
        }
    }
    override fun getAllBooksByCategory() =
        callbackFlow<Result<List<BooksByCategoryData>>> {
            var count = 0
            fireStore.collection(categoryCN)
                .get()
                .addOnSuccessListener { categories ->
                    allBooksList.clear()
                    val categoriesList = ArrayList<BooksByCategoryData>()
                    categories.forEach { qds ->
                        fireStore.collection(booksCN)
                            .whereEqualTo(bookFieldCategory, qds.id)
                            .get()
                            .addOnSuccessListener { books ->
                                val booksList = ArrayList<BookData>()
                                count++
                                books.forEach {
                                    val book = BookData(
                                        docId = it.id,
                                        name = it.data.getOrDefault("name", "") as String,
                                        author = it.data.getOrDefault("author", "") as String,
                                        bookPath = it.data.getOrDefault("bookPath", "") as String,
                                        imgPath = it.data.getOrDefault("imgPath", "") as String,
                                        categoryId = it.data.getOrDefault(
                                            "categoryId",
                                            ""
                                        ) as String,
                                        overview = it.data.getOrDefault("overview", "") as String,
                                    )
                                    allBooksList.add(book)
                                    booksList.add(book)
                                }
                                categoriesList
                                    .add(
                                        BooksByCategoryData(
                                            docId = qds.id,
                                            name = qds.data.getOrDefault("name", "") as String,
                                            books = booksList
                                        )
                                    )
                                if (count == categories.size()) {
                                    trySend(Result.success(categoriesList))
                                }
                            }
                            .addOnFailureListener {

                            }

                    }
                }
                .addOnFailureListener {
                    trySend(Result.failure(it))
                }
            awaitClose()
        }.flowOn(Dispatchers.IO).catch { emit(Result.failure(it)) }

    override fun getBooksByName(name: String) = callbackFlow {
        /* fireStore
             .collection(booksCN)
             .get()
             .addOnSuccessListener {
                 val list = ArrayList<BookData>()
                 it.forEach { book ->
                     val bookName = book.data.getOrDefault("name", "") as String
                     if (bookName.contains(name, true)) {
                         val newBook = BookData(
                             docId = book.id,
                             name = book.data.getOrDefault("name", "") as String,
                             author = book.data.getOrDefault("author", "") as String,
                             bookPath = book.data.getOrDefault("bookPath", "") as String,
                             imgPath = book.data.getOrDefault("imgPath", "") as String,
                             categoryId = book.data.getOrDefault(
                                 "categoryId",
                                 ""
                             ) as String,
                             overview = book.data.getOrDefault("overview", "") as String,
                         )
                         list.add(newBook)
                     }
                     trySend(Result.success(list))
                 }
             }*/
        val list = allBooksList.filter { it.name.contains(name, true) }
        trySend(Result.success(list))
        awaitClose()
    }.flowOn(Dispatchers.IO).catch { emit(Result.failure(it)) }

    override fun getAllSavedBook() = callbackFlow {
        /*fireStore.collection(booksCN)
            .whereEqualTo(bookFieldCategory, "")
            .get()
            .addOnSuccessListener { books ->
                val booksList = ArrayList<BookData>()
                val savedBooksId = getSavedBookIdList()
                books.forEach {
                    if (savedBooksId.indexOf(it.id) != -1) {
                        val book = BookData(
                            docId = it.id,
                            name = it.data.getOrDefault("name", "") as String,
                            author = it.data.getOrDefault("author", "") as String,
                            bookPath = it.data.getOrDefault("bookPath", "") as String,
                            imgPath = it.data.getOrDefault("imgPath", "") as String,
                            categoryId = it.data.getOrDefault(
                                "categoryId",
                                ""
                            ) as String,
                            overview = it.data.getOrDefault("overview", "") as String,
                        )
                        booksList.add(book)
                    }
                }
                trySend(Result.success(booksList))
            }
            .addOnFailureListener {

            }*/
        val oldSavedIdList = getSavedBookIdList()
        val list = allBooksList.filter { oldSavedIdList.indexOf(it.docId) != -1 }
        trySend(Result.success(list))
        awaitClose()
    }.flowOn(Dispatchers.IO).catch { emit(Result.failure(it)) }

    override fun isSavedBook(bookData: BookData) = callbackFlow {
        trySend(Result.success(hasSaved(bookData.docId)))
        awaitClose()
    }.flowOn(Dispatchers.IO).catch { emit(Result.failure(it)) }

    private fun putSavedBookIdList(list: ArrayList<String>) {
        myShared.putString(savedBookId, list.joinToString(separator = savedBookIdPrefix))
    }

    private fun hasSaved(bookId: String): Boolean {
        return getSavedBookIdList().indexOf(bookId) != -1
    }

    private fun getSavedBookIdList(): ArrayList<String> {
        val list = ArrayList<String>()
        val str = myShared.getString(savedBookId, "")
        myLogger(str, "LIST")
        if (str.isEmpty()) return arrayListOf()
        str.split(savedBookIdPrefix).forEach {
            list.add(it)
        }
        return list
    }

    override fun downloadESBook(bookData: BookData) = callbackFlow<Result<String>> {
        val file =
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath,
                bookData.name + token + ".pdf"
            )
        if (file.exists()) {
            trySend(Result.success(file.absolutePath))
        } else {
            storage
                .getReferenceFromUrl(bookData.bookPath)
                .getFile(file)
                .addOnSuccessListener {
                    trySend(Result.success(file.absolutePath))
                }
                .addOnFailureListener {
                    trySend(Result.failure(it))
                }
        }
        awaitClose()
    }.flowOn(Dispatchers.IO).catch { emit(Result.failure(it)) }

    override fun favoriteChange(bookData: BookData, checked: Boolean) {
        val list = getSavedBookIdList()
        myLogger(list.toString(), "LIST")
        myLogger(list.size.toString(), "LIST")
        val index = list.indexOf(bookData.docId)
        if (index == -1 && checked) {
            list.add(bookData.docId)
            putSavedBookIdList(list)
        } else if (!checked) {
            list.removeAt(index)
            putSavedBookIdList(list)
        }
    }
}