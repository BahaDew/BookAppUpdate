package uz.bahadew.bookappupdate.presentation.screen.read

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.bahadew.bookappupdate.data.BookData
import uz.bahadew.bookappupdate.domain.AppRepository
import uz.bahadew.bookappupdate.navigation.AppNavigator
import uz.bahadew.bookappupdate.utils.myLogger
import javax.inject.Inject

@HiltViewModel
class ReadViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val appRepository: AppRepository
) : ViewModel() {

    val responseFilePath =
        MutableSharedFlow<String>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    val progressState = MutableSharedFlow<Boolean>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    fun requestDownloadBook(bookData: BookData) {
        progressState.tryEmit(true)
        appRepository.downloadESBook(bookData)
            .onEach {
                progressState.tryEmit(false)
                it.onSuccess { path ->
                    responseFilePath.tryEmit(path)
                }
                it.onFailure { thr ->
                    myLogger(thr.message ?: "Nomalum xatolik")
                }
            }
            .launchIn(viewModelScope)
    }

}