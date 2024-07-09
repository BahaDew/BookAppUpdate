package uz.bahadew.bookappupdate.presentation.screen.main.pages.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.bahadew.bookappupdate.data.BookData
import uz.bahadew.bookappupdate.domain.AppRepository
import uz.bahadew.bookappupdate.navigation.AppNavigator
import uz.bahadew.bookappupdate.presentation.screen.main.MainScreenDirections
import uz.bahadew.bookappupdate.utils.myLogger
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val appRepository: AppRepository
) : ViewModel() {
    val responseBooks =
        MutableSharedFlow<List<BookData>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    fun requestBooksByName(name: String) {
        appRepository
            .getBooksByName(name)
            .onEach {
                it.onSuccess { list ->
                    responseBooks.tryEmit(list)
                }
                it.onFailure { thr ->
                    myLogger(thr.message ?: "Nomalum xatolik")
                }
            }.launchIn(viewModelScope)
    }

    fun onClickBook(bookData: BookData) {
        viewModelScope.launch {
            appNavigator.navigateTo(MainScreenDirections.actionMainScreenToInfoScreen2(bookData))
        }
    }

}