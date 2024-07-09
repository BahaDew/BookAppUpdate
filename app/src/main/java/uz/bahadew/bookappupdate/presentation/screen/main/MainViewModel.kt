package uz.bahadew.bookappupdate.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.bahadew.bookappupdate.domain.AppRepository
import uz.bahadew.bookappupdate.navigation.AppNavigator
import uz.bahadew.bookappupdate.utils.myLogger
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    private val appRepository: AppRepository
) : ViewModel() {
}