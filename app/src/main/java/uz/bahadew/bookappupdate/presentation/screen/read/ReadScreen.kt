package uz.bahadew.bookappupdate.presentation.screen.read

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.bahadew.bookappupdate.R
import uz.bahadew.bookappupdate.data.MyShared
import uz.bahadew.bookappupdate.databinding.ScreenReadBinding
import uz.bahadew.bookappupdate.navigation.AppNavigator
import javax.inject.Inject

@AndroidEntryPoint
class ReadScreen : Fragment(R.layout.screen_read) {
    private val binding by viewBinding(ScreenReadBinding::bind)
    private val navArgs by navArgs<ReadScreenArgs>()
    private val viewModel by viewModels<ReadViewModel>()

    @Inject
    lateinit var myShared: MyShared

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initFlow()
    }

    private fun initView() = binding.apply {
        viewModel.requestDownloadBook(navArgs.bookData)
    }

    private fun initFlow() = binding.apply {
        viewModel.responseFilePath
            .onEach {
                val page = myShared.getInt(navArgs.bookData.docId, 0)
                pdfView
                    .fromUri(Uri.parse("file:///$it"))
                    .defaultPage(page)
                    .load()
            }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.progressState
            .onEach {
                progressBar.isVisible = it
            }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    override fun onStop() {
        super.onStop()
        myShared.putInt(navArgs.bookData.docId, binding.pdfView.currentPage)
    }
}