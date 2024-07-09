package uz.bahadew.bookappupdate.presentation.screen.main.pages.saved

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.bahadew.bookappupdate.R
import uz.bahadew.bookappupdate.databinding.ScreenSavedBinding
import uz.bahadew.bookappupdate.presentation.adapter.BooksBySavedAdapter

@AndroidEntryPoint
class SavedScreen : Fragment(R.layout.screen_saved) {
    private val binding by viewBinding(ScreenSavedBinding::bind)
    private val viewModel by viewModels<SavedViewModel>()
    private val adapter = BooksBySavedAdapter()
    private var time = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initFlow()
    }

    private fun initView() = binding.apply {
        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(requireContext())
        adapter.setOnClickBook {
            if (System.currentTimeMillis() - time >= 500) {
                time = System.currentTimeMillis()
                viewModel.onClickBook(it)
            }
        }

        adapter.setOnClickCheckBox { book, isChecked ->
            if (System.currentTimeMillis() - time >= 500) {
                time = System.currentTimeMillis()
                viewModel.favoriteChange(book, isChecked)
            }
        }
        viewModel.requestAllSavedBooks()
    }

    private fun initFlow() = binding.apply {
        viewModel.responseAllSavedBooks
            .onEach {
                adapter.submitList(it)
                emptyState.isVisible = it.isEmpty()
            }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestAllSavedBooks()
    }
}