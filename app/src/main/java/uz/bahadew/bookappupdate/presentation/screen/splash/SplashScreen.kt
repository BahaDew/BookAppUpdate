package uz.bahadew.bookappupdate.presentation.screen.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.bahadew.bookappupdate.R

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {
    private val viewModel by viewModels<SplashViewModel>()
    private var isResume = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(800)
            viewModel.openMain()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isResume) {
            viewModel.openMain()
        }
    }

    override fun onStop() {
        super.onStop()
        isResume = true
    }
}