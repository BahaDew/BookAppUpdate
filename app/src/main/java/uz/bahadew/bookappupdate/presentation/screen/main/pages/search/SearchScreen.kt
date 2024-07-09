package uz.bahadew.bookappupdate.presentation.screen.main.pages.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
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
import uz.bahadew.bookappupdate.databinding.ScreenSearchBinding
import uz.bahadew.bookappupdate.presentation.adapter.BooksBySavedAdapter

@AndroidEntryPoint
class SearchScreen : Fragment(R.layout.screen_search) {
    private val binding by viewBinding(ScreenSearchBinding::bind)
    private val viewModel by viewModels<SearchViewModel>()
    private val adapter = BooksBySavedAdapter()
    private var time = 0L
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initFlow()
    }

    private fun initView() = binding.apply {
        rvList.adapter = adapter
        viewModel.requestBooksByName("")
        rvList.layoutManager = LinearLayoutManager(requireContext())
        et.addTextChangedListener {
            viewModel.requestBooksByName(et.text.toString())
        }
        adapter.setOnClickBook {
            if (System.currentTimeMillis() - time >= 500) {
                time = System.currentTimeMillis()
                viewModel.onClickBook(it)
            }
        }
    }

    private fun initFlow() = binding.apply {
        viewModel.responseBooks
            .onEach {
                adapter.setQuery(et.text.toString(), it)
            }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }
}