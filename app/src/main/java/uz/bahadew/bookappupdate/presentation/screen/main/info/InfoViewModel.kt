package uz.bahadew.bookappupdate.presentation.screen.main.info

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
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val appRepository: AppRepository
) : ViewModel() {
    val isFavoriteBook =
        MutableSharedFlow<Boolean>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    fun onClickBack() {
        viewModelScope.launch {
            appNavigator.navigateUp()
        }
    }

    fun onClickRead(bookData: BookData) {
        viewModelScope.launch {
            appNavigator.navigateTo(InfoScreenDirections.actionInfoScreen2ToReadScreen(bookData))
        }
    }

    fun isFavoriteBook(bookData: BookData) {
        appRepository.isSavedBook(bookData)
            .onEach {
                it.onSuccess { temp ->
                    isFavoriteBook.tryEmit(temp)
                }
                it.onFailure {

                }
            }.launchIn(viewModelScope)
    }

    fun favoriteChange(bookData: BookData, checked: Boolean) {
        appRepository.favoriteChange(bookData, checked)
    }

}