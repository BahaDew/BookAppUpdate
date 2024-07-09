package uz.bahadew.bookappupdate

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.bahadew.bookappupdate.databinding.ActivityMainBinding
import uz.bahadew.bookappupdate.navigation.AppNavigationHandler
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var appNavigationHandler: AppNavigationHandler

    private val binding by viewBinding(ActivityMainBinding::bind)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main) as NavHostFragment
        val navController = navHostFragment.navController
        appNavigationHandler.buffer
            .onEach {
                it.invoke(navController)
            }.launchIn(lifecycleScope)
//        statusBarSetting()
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    private fun statusBarSetting() {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            val statusBarHeight = resources.getDimensionPixelSize(resourceId)
            binding.root.setPadding(0, statusBarHeight, 0, 0)
        } else {
            binding.root.setPadding(0, 30, 0, 0)
        }
    }
}