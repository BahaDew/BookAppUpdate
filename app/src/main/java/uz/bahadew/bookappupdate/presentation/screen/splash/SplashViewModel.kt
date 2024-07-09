package uz.bahadew.bookappupdate.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.bahadew.bookappupdate.navigation.AppNavigator
import uz.bahadew.bookappupdate.utils.myLogger
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appNavigator: AppNavigator
) : ViewModel(){
    fun openMain() {
        viewModelScope.launch {
            appNavigator.navigateTo(SplashScreenDirections.actionSplashScreenToMainScreen())
        }
    }
}